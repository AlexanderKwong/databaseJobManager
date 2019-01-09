package com.mastercom.bigdata.logic.dao.impl;

import com.mastercom.bigdata.model.impl.Job;

/**
 * Created by Kwong on 2019/1/8.
 */
public class TestJobDAO {

    public void testInsert(){
        Job job = new Job();
        job.setJobName("测试01");
        job.setCreateTime("20190108");
        job.setDatabaseType("Derby");
        job.setStates("hello");


    }
}
