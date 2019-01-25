package com.mastercom.bigdata.logic.service.impl;

import com.mastercom.bigdata.logic.service.IService;
import com.mastercom.bigdata.logic.service.ScheduledExecutorServiceHolder;
import com.mastercom.bigdata.model.impl.Job;
import com.mastercom.bigdata.common.util.TimeUtil;
import com.mastercom.bigdata.common.sql.DBType;
import com.mastercom.bigdata.common.sql.DBUtil;
import com.mastercom.bigdata.view.LogPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.mastercom.bigdata.logic.Constants.*;
import static com.mastercom.bigdata.logic.Constants.JOB_STATE_FREE;
import static com.mastercom.bigdata.logic.Constants.JOB_STATUS_DISABLE;

/**
 * Created by Kwong on 2018/1/14.
 */
public class SqlJobExecutor implements IService<Job>{

    private static final Logger LOG = LoggerFactory.getLogger(SqlJobExecutor.class);

    JobService jobService = ServiceFactory.getInstance(Job.class);

    private Map<Integer, ScheduledFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public List<Job> list(Job model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Job find(Job model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Job findById(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int add(Job model) {
        addFuture(model);
        return 0;
    }

    @Override
    public int update(Job model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int remove(Job model) {
        removeFuture(model);
        return 0;
    }

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
                LOG.error("", e);
                LogPrinter.println(e.getMessage());
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            LogPrinter.println(e.getMessage());
            LOG.error("", e);
            return false;
        }finally {
            DBUtil.close(conn, null, null);
        }
    }

    private long getDelay(Job model){
        String[] hhmmss = model.getDailyTime().split(":");
        Date currentTime = new Date();
        Date todayTimeToRun = new Date(currentTime.getYear(), currentTime.getMonth(), currentTime.getDate(), Integer.parseInt(hhmmss[0]), Integer.parseInt(hhmmss[1]), Integer.parseInt(hhmmss[2]));
        LOG.debug("当前时间{}:{}:{}",currentTime.getHours(), currentTime.getMinutes(), currentTime.getSeconds());
        LOG.debug("目标时间{}:{}:{}",hhmmss[0],hhmmss[1],hhmmss[2]);
        long delay = 0L;
        if (currentTime.before(todayTimeToRun)){
            delay = (todayTimeToRun.getHours() * 3600L + todayTimeToRun.getMinutes() * 60 + todayTimeToRun.getSeconds()) - (currentTime.getHours() * 3600 + currentTime.getMinutes() * 60 + currentTime.getSeconds());
            LOG.debug("今天还需运行，延时为：" + delay + "s");
        }else {
            delay = (todayTimeToRun.getHours() * 3600L + todayTimeToRun.getMinutes() * 60 + todayTimeToRun.getSeconds()) + (86400 - (currentTime.getHours() * 3600 + currentTime.getMinutes() * 60 + currentTime.getSeconds()));
            LOG.debug("今天不需运行，延时为：" + delay + "s");
        }
        return delay;
    }

    private void prepare(Job model){
        LogPrinter.println("\n 【当前时间" + TimeUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss") + "  job  --" + model.getJobName() + "--即将开始运行】");
        String sql = model.getOrderContent().replace(";","");
        LogPrinter.println(sql);
        model.setStates(JOB_STATE_RUNNING);
        jobService.update(model);
    }
    /**
     * TODO
     * 由于存在月定时，而月的天数又是不固定的，无法通过固定的period去实现。
     * 又由于不想引入更多的包来实现，如quartz。
     * 折衷的解决方案有：1、定时任务按最小粒度（天）来定，每天进入任务时比较一下当前的日期与任务设定的日期（每月/周的第n天）,如果对应得上就做任务，否则啥都不干
     *                   2、每执行完一次delay的任务就将其移除，并添加下一次delay的任务进缓存，相当于在future执行完后将自己cancel，然后schedule下一个delay的任务
     * 暂时采用1，弊端：如果系统时间被修改，就变成不按周期了 FIXME
     * @param model
     */
    private void addFuture(final Job model){
        ScheduledFuture future = futureMap.get(model.getId());
        if (future == null){
            LogPrinter.println(model.toString());
            //获取距离第一次的时间间隔
            long delay = getDelay(model);

            future = ScheduledExecutorServiceHolder.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isTimeToDoJob(model)){
                            //step1: 更新视图为 运行态
                            prepare(model);
                            //step2: 执行命令
                            boolean succeed = execute(model);
                            //step3：根据执行结果更新视图
                            release(succeed, model);
                            LOG.debug("【{}】-----------finish------------", model.getJobName());
                        }else {
                            LOG.debug("【{}】不是正确的运行周期", model.getJobName());
                        }
                    }catch (Throwable e){
                        LOG.error("任务执行出错", e);
                    }
                }
            }, delay, 86400L, TimeUnit.SECONDS);

            futureMap.put(model.getId(), future);
        }else{
            LOG.debug("已包含这个任务，请不要重复启动");
        }
    }

    private boolean isTimeToDoJob(Job model){
        String frequency = model.getPlanFrequency();
        return frequency.equals(JOB_FREQUENCY_DAY)
                || (frequency.equals(JOB_FREQUENCY_WEEK) && TimeUtil.dayNoInOneWeek() == Integer.parseInt(model.getDay()))
                || (frequency.equals(JOB_FREQUENCY_MONTH) && TimeUtil.dayNoInOneMonth() == Integer.parseInt(model.getDay()));
    }

    private void release(boolean succeed, Job model) {
        //更新视图为 空闲态
        model.setStates(JOB_STATE_FREE);
        //执行成功的话，刷新；失败的话将其暂停，并刷新。
        if (!succeed){
            removeFuture(model);//测试证明，在线程中调用这个future.cancel()是可以停止调度这个任务的
            model.setStatus(JOB_STATUS_DISABLE);
            LogPrinter.println("【" + model.getJobName() + "】运行失败,已禁用");
        }else {
            LogPrinter.println("【" + model.getJobName() + "】一次运行完毕,等待下一个周期");
        }
        jobService.update(model);
    }

    private void removeFuture(Job model){
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
