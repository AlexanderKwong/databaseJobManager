package com.mastercom.bigdata.logic.dao.impl;

import com.mastercom.bigdata.model.impl.Job;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Kwong on 2019/1/8.
 */
public class TestJobDAO {

    private static final Logger LOG = LoggerFactory.getLogger(TestJobDAO.class);

    JobDAO dao;
    @Before
    public void before(){
        dao = new JobDAO();
    }

    @Test
    public void testQuery(){
        Job job = new Job();
        List<Job> result = dao.query(job);
        System.out.println(result);
    }

    @Test
    public void testInsert(){
        Job job = new Job();
        job.setJobName("测试01");
        job.setCreateTime("20190108");
        job.setDatabaseType("DERBY");
        job.setStates("hello");
        List<Job> result = dao.query(job);
        Assert.assertTrue(result.isEmpty());
        dao.insert(job);
        result = dao.query(job);
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testDelete(){
        LOG.info("=======================================");
        Job job = new Job();
        job.setJobName("测试01");
        job.setCreateTime("20190108");
        job.setDatabaseType("DERBY");
        job.setStates("hello");
        List<Job> result = dao.query(job);
        if (result.isEmpty()) {
            LOG.info("No query reuslt and it goes to insert one.");
            dao.insert(job);
            LOG.info("Insert completed.");
            result = dao.query(job);
        }
        Assert.assertFalse(result.isEmpty());
        LOG.info("Delete query result.");
        for (Job j : result){
            dao.delete(j);
        }
        LOG.info("Delete completed.");
        result = dao.query(job);
        Assert.assertTrue(result.isEmpty());
        LOG.info("=======================================");
    }

    @Test
    public void testUpdate(){
        LOG.info("=======================================");
        Job job = new Job();
        job.setJobName("测试01");
        job.setCreateTime("20190108");
        job.setDatabaseType("DERBY");
        job.setStates("hello");
        List<Job> result = dao.query(job);
        if (result.isEmpty()) {
            LOG.info("No query reuslt and it goes to insert one.");
            dao.insert(job);
            LOG.info("Insert completed.");
            result = dao.query(job);
        }
        LOG.info("update query result.");
        for (Job j : result){
            j.setStates("kill myself");
            dao.updateById(j.getId(), j);
        }
        LOG.info("Update completed.");
        result = dao.query(new Job());
        for (Job j : result){
            Assert.assertTrue("kill myself".equals(j.getStates()));
            dao.delete(j);
        }
        LOG.info("=======================================");
    }

}
