package com.mastercom.bigdata.logic.service.impl;

import com.mastercom.bigdata.logic.dao.impl.JobDAO;
import com.mastercom.bigdata.model.impl.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Kwong on 2017/9/23.
 */
class JobService extends AbstractService<Job> {

    public JobService(){

        dao = new JobDAO();
    }

}
