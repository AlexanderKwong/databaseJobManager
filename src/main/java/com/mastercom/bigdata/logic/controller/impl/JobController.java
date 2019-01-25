package com.mastercom.bigdata.logic.controller.impl;

import com.mastercom.bigdata.logic.service.impl.SqlJobExecutor;
import com.mastercom.bigdata.view.LogPrinter;
import com.mastercom.bigdata.model.impl.Job;
import com.mastercom.bigdata.logic.controller.ModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import static com.mastercom.bigdata.logic.Constants.*;

/**
 * Created by Kwong on 2017/9/23.
 */
class JobController extends AbstractController<Job>{

    private static final Logger LOG = LoggerFactory.getLogger(JobController.class);

    private SqlJobExecutor jobExecutor;

    public JobController(){
        super();

        jobExecutor = new SqlJobExecutor();

    }

    private boolean isModifiable(Job model){
        if (model.getId() != null){

            try {
                Job tmp = service.findById(model.getId());
                if (tmp == null){
                    LOG.debug("Job--【{}】没有找到id!", tmp.getJobName());
                    return false;
                }

                if (tmp != null && JOB_STATE_RUNNING.equals(tmp.getStates())){
                    LOG.debug("Job--【{}】正在运行态，请稍候再操作!", tmp.getJobName());
                    return false;
                }
                if (tmp != null && !tmp.getJobName().equals(model.getJobName())){
                    LOG.debug("Job 修改失败，请勿修改jobname");
                    return false;
                }
            } catch (Exception e) {
                LOG.error("Job 查询失败", e);
                return false;
            }
        }
        return true;
    }

    @Override
    public ModelWrapper<Job> put(final Job model) {
        if (!isModifiable(model)){
            return new ModelWrapper<>(ModelWrapper.OPERA_MODIFY, ModelWrapper.FAILED, null, "Job操作失败，请检查日志。");
        }
        ModelWrapper<Job> result = super.put(model);
        if (result.getReturnCode() == ModelWrapper.SUCCESS){

            if (model.getStatus() == JOB_STATUS_ENABLE){
                if (result.getOperation() == ModelWrapper.OPERA_MODIFY)
                    jobExecutor.remove(model);
                if (result.getOperation() == ModelWrapper.OPERA_NEW)
                    try {
                        model.setId(service.find(model).getId());
                    } catch (Exception e) {
                        LOG.error("", e);
                    }
                jobExecutor.add(model);
            }else{
                jobExecutor.remove(model);
            }
        }
        LogPrinter.println("Job " + result.resultMsg());
        return result;
    }

    @Override
    public ModelWrapper<Job> delete(Job model) {
        if (!isModifiable(model)){
            return new ModelWrapper<>(ModelWrapper.OPERA_MODIFY, ModelWrapper.FAILED, null, "Job操作失败，请检查日志。");
        }
        ModelWrapper<Job> result = super.delete(model);
        if (result.getReturnCode() == ModelWrapper.SUCCESS){
            jobExecutor.remove(model);
        }
        LogPrinter.println("Job " + result.resultMsg());
        return result;
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
            jobExecutor.add(job);
        }
    }

}
