package com.mastercom.bigdata.logic.service.impl;

import com.mastercom.bigdata.logic.dao.impl.JobDAO;
import com.mastercom.bigdata.model.impl.Job;
import com.mastercom.bigdata.db.EmbeddDBTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Kwong on 2017/9/23.
 */
class JobService extends AbstractService<Job> {

    private static Logger LOG = LoggerFactory.getLogger(JobService.class);

    public JobService(){

        dao = new JobDAO();
    }

}
