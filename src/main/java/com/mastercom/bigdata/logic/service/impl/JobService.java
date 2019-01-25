package com.mastercom.bigdata.logic.service.impl;

import com.mastercom.bigdata.logic.dao.impl.JobDao;
import com.mastercom.bigdata.model.impl.Job;

/**
 * Created by Kwong on 2017/9/23.
 */
class JobService extends AbstractService<Job> {

    public JobService(){

        dao = new JobDao();
    }

}
