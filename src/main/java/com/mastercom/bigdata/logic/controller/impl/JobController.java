package com.mastercom.bigdata.logic.controller.impl;

import com.mastercom.bigdata.UI.panel.LogPanel;
import com.mastercom.bigdata.model.impl.Job;
import com.mastercom.bigdata.logic.controller.ModelWrapper;
import com.mastercom.bigdata.tools.TimeUtil;
import com.mastercom.bigdata.tools.sql.DBType;
import com.mastercom.bigdata.tools.sql.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import static com.mastercom.bigdata.logic.Constants.*;

/**
 * Created by Kwong on 2017/9/23.
 */
class JobController extends AbstractController<Job> {

    private static final Logger LOG = LoggerFactory.getLogger(JobController.class);

    private JobExecutor jobExecutor;

    public JobController(){
        super();

        jobExecutor = new JobExecutor();

    }

    @Override
    public ModelWrapper<Job> put(final Job model) {
        if (model.getId() != null){

            Job tmp = null;
            try {
                tmp = service.findById(model.getId());
            } catch (Exception e) {
                LOG.error("", e);
                return new ModelWrapper<>(ModelWrapper.OPERA_UNDEFINED, ModelWrapper.FAILED, null, "Job 查询失败\n\t"+e.getMessage());
            }
            if (tmp != null && JOB_STATE_RUNNING.equals(tmp.getStates())){
                return new ModelWrapper<>(ModelWrapper.OPERA_MODIFY, ModelWrapper.FAILED, null, "Job--【" + tmp.getJobName() + "】正在运行态，请稍候再更新!");
            }
            if (tmp != null && !tmp.getJobName().equals(model.getJobName())){
                return new ModelWrapper<>(ModelWrapper.OPERA_MODIFY, ModelWrapper.FAILED, null, "Job 修改失败，请勿修改jobname");
            }
        }
        ModelWrapper<Job> result = super.put(model);
        if (result.getReturnCode() == ModelWrapper.SUCCESS){
            if (result.getOperation() == ModelWrapper.OPERA_NEW){
                consolePrintln("Job 新增成功！");
            }else if (result.getOperation() == ModelWrapper.OPERA_MODIFY){
                consolePrintln("Job 修改成功！");
            }
            if (model.getStatus() == JOB_STATUS_ENABLE){
                if (result.getOperation() == ModelWrapper.OPERA_MODIFY)
                    jobExecutor.removeFuture(model);
                if (result.getOperation() == ModelWrapper.OPERA_NEW)
                    try {
                        model.setId(service.find(model).getId());
                    } catch (Exception e) {
                        LOG.error("", e);
                    }
                jobExecutor.addFuture(model);
            }else{
                jobExecutor.removeFuture(model);
            }
        }else{
            if (result.getOperation() == ModelWrapper.OPERA_NEW){
                consolePrintln("Job 新增失败！");
            }else if (result.getOperation() == ModelWrapper.OPERA_MODIFY){
                consolePrintln("Job 修改失败！");
            }
        }
        return result;
    }

    @Override
    public ModelWrapper<Job> delete(Job model) {
        Job tmp = null;
        try {
            tmp = service.findById(model.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelWrapper<>(ModelWrapper.OPERA_UNDEFINED, ModelWrapper.FAILED, null, "Job 查询失败\n\t"+e.getMessage());
        }
        if (tmp != null && JOB_STATE_RUNNING.equals(tmp.getStates())){
            return new ModelWrapper<>(ModelWrapper.OPERA_MODIFY, ModelWrapper.FAILED, null, "Job--【" + tmp.getJobName() + "】正在运行态，请稍候再删除!。");
        }
        ModelWrapper<Job> result = super.delete(model);
        if (result.getReturnCode() == ModelWrapper.SUCCESS){
            jobExecutor.removeFuture(model);
            consolePrintln("Job 删除成功！");
        }else{
            consolePrintln("Job 删除失败！");
        }
        return result;
    }

    private void consolePrintln(String msg){
        for (ViewRef viewRef : views){
            if (viewRef.viewClass == LogPanel.class){
                ((LogPanel)viewRef.view).output(msg);
                return;
            }
        }
        LOG.debug("没有找到日志输出面板。\n\t待输出内容为：" + msg);

    }

    @Override
    public void init(){
        LOG.info("启动所有状态为“启用”的任务");

        List<Job> jobs = null;
        try {
            jobs = service.list(new Job.Builder().withStatus(JOB_STATUS_ENABLE).build());
        } catch (Exception e) {
            LOG.error("", e);
            System.exit(1);
        }

        for (Job job : jobs){
            jobExecutor.addFuture(job);
        }
    }

    /**
     * Job 执行器， 负责job的执行，和状态维护
     */
    private class JobExecutor{

        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(2);

        private Map<Integer, ScheduledFuture>  futureMap = new ConcurrentHashMap<>();

        private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        private boolean execute(Job model){
            String sql = model.getOrderContent().replace(";","");
            String url = model.getUrl();
            String username = model.getUsername();
            String password = model.getPassword();
            Connection conn = null;
            try {
                conn = DBUtil.getConnectionByType(DBType.fromName(model.getDatabaseType()), url, username, password);
                DBUtil.execute(sql, conn);
                return true;
            } catch (SQLException e) {
                if (!e.getMessage().equals("已生成用于更新的结果集。"))
                {
                    e.printStackTrace();
                    consolePrintln(e.getMessage());
                    return false;
                }else{
                    return true;
                }
            } catch (Exception e) {
                consolePrintln(e.getMessage());
                e.printStackTrace();
                return false;
            }finally {
                DBUtil.close(conn, null, null);
            }
        }

        /**
         * TODO
         * 由于存在月定时，而月的天数又是不固定的，无法通过固定的period去实现。
         * 又由于不想引入更多的包来实现，如quartz。
         * 折衷的解决方案有：1、定时任务按最小粒度（天）来定，每天进入任务时比较一下当前的日期与任务设定的日期（每月/周的第n天）,如果对应得上就做任务，否则啥都不干
         *                   2、每执行完一次delay的任务就将其移除，并添加下一次delay的任务进缓存，相当于在future执行完后将自己cancel，然后schedule下一个delay的任务
         * 暂时采用1，弊端：如果系统时间被修改，就变成不按周期了
         * @param model
         */
        public void addFuture(final Job model){
            ScheduledFuture future = futureMap.get(model.getId());
            if (future == null){
                consolePrintln(model.toString());

                //获取距离第一次的时间间隔
                String[] hhmmss = model.getDailyTime().split(":");
                Date currentTime = new Date();
                Date todayTimeToRun = new Date(currentTime.getYear(), currentTime.getMonth(), currentTime.getDate(), Integer.parseInt(hhmmss[0]), Integer.parseInt(hhmmss[1]), Integer.parseInt(hhmmss[2]));
                LOG.debug(String.format("当前时间%d:%d:%d",currentTime.getHours(), currentTime.getMinutes(), currentTime.getSeconds()));
                LOG.debug(String.format("目标时间%s:%s:%s",hhmmss[0],hhmmss[1],hhmmss[2]));
                long delay = 0L;
                if (currentTime.before(todayTimeToRun)){
                    delay = (todayTimeToRun.getHours() * 3600 + todayTimeToRun.getMinutes() * 60 + todayTimeToRun.getSeconds()) - (currentTime.getHours() * 3600 + currentTime.getMinutes() * 60 + currentTime.getSeconds());
                    LOG.debug("今天还需运行，延时为：" + delay + "s");
                }else {
                    delay = (todayTimeToRun.getHours() * 3600 + todayTimeToRun.getMinutes() * 60 + todayTimeToRun.getSeconds()) + (86400 - (currentTime.getHours() * 3600 + currentTime.getMinutes() * 60 + currentTime.getSeconds()));
                    LOG.debug("今天不需运行，延时为：" + delay + "s");
                }

                future = scheduler.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {

                        String frequency = model.getPlanFrequency();
                        if (frequency.equals(JOB_FREQUENCY_DAY) || (frequency.equals(JOB_FREQUENCY_WEEK) && TimeUtil.dayNoInOneWeek() == Integer.parseInt(model.getDay()))|| (frequency.equals(JOB_FREQUENCY_MONTH) && TimeUtil.dayNoInOneMonth() == Integer.parseInt(model.getDay()))){

                            //step1: 更新视图为 运行态
                            consolePrintln("\n 【当前时间" + df.format(new Date()) + "  job  --" + model.getJobName() + "--即将开始运行】");
                            String sql = model.getOrderContent().replace(";","");
                            consolePrintln(sql);
                            model.setStates(JOB_STATE_RUNNING);
                            JobController.super.put(model);

                            //step2: 执行命令
                            boolean succeed = execute(model);

                            //step3: 更新视图为 空闲态
                            model.setStates(JOB_STATE_FREE);
                            //step4: 执行成功的话，刷新；失败的话将其暂停，并刷新。
                            if (!succeed){
                                removeFuture(model);//测试证明，在线程中调用这个future.cancel()是可以停止调度这个任务的
                                model.setStatus(JOB_STATUS_DISABLE);
                                consolePrintln("【" + model.getJobName() + "】运行失败,已禁用");
                            }else {
                                consolePrintln("【" + model.getJobName() + "】一次运行完毕,等待下一个周期");
                            }
                            JobController.super.put(model);
                            LOG.debug("【{}】-----------finish------------", model.getJobName());
                        }else {
                            LOG.debug("【{}】不是正确的运行周期", model.getJobName());
                        }
                    }
                }, delay, 86400L, TimeUnit.SECONDS);

                futureMap.put(model.getId(), future);
            }else{
                LOG.debug("已包含这个任务，请不要重复启动");
            }
        }

        public void removeFuture(Job model){
            ScheduledFuture future = futureMap.get(model.getId());
            if (future != null){
                futureMap.remove(model.getId());
                future.cancel(true);
                LOG.debug("删除【"+ model.getJobName() +"】任务计划成功");
            }else{
                LOG.debug("没有包含这个任务，无法删除");
            }
        }
    }
}
