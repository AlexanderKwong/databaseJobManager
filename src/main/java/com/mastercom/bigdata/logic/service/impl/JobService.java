package com.mastercom.bigdata.logic.service.impl;

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

        dao = new DAO<>(EmbeddDBTable.TB_CFG_JOB_INFORMATION, getOrderedGetters(), getOrderedSetters());
    }

    /**
     * 写死的顺序
     * 按照数据库表的字段顺序，获取Job成员属性的getter
     * @return
     */
    private Method[] getOrderedGetters(){
        Map<String, Method> getterNameMapping = new HashMap<>();
        Method[] methods = Job.class.getDeclaredMethods();
        for (Method m : methods){
            //todo 没有考虑返回值了
            if (m.getName().startsWith("get") && m.getParameterTypes().length == 0)
                getterNameMapping.put(m.getName(), m);
        }
        Method[] result = new Method[EmbeddDBTable.TB_CFG_JOB_INFORMATION.getColumns().length];
        result[0] = getterNameMapping.get("getId");
        result[1] = getterNameMapping.get("getDatabaseType");
        result[2] = getterNameMapping.get("getUrl");
        result[3] = getterNameMapping.get("getUsername");
        result[4] = getterNameMapping.get("getPassword");
        result[5] = getterNameMapping.get("getJobName");
        result[6] = getterNameMapping.get("getOrderContent");
        result[7] = getterNameMapping.get("getPlanFrequency");
        result[8] = getterNameMapping.get("getDailyTime");
        result[9] = getterNameMapping.get("getDay");
        result[10] = getterNameMapping.get("getDeclaration");
        result[11] = getterNameMapping.get("getStatus");
        result[12] = getterNameMapping.get("getStates");
        result[13] = getterNameMapping.get("getCreateTime");
        return result;
    }

    private Method[] getOrderedSetters(){
        Map<String, Method> getterNameMapping = new HashMap<>();
        Method[] methods = Job.class.getDeclaredMethods();
        for (Method m : methods){
            //todo 没有考虑返回值了
            if (m.getName().startsWith("set") && m.getParameterTypes().length == 1)
                getterNameMapping.put(m.getName(), m);
        }
        Method[] result = new Method[EmbeddDBTable.TB_CFG_JOB_INFORMATION.getColumns().length];
        result[0] = getterNameMapping.get("setId");
        result[1] = getterNameMapping.get("setDatabaseType");
        result[2] = getterNameMapping.get("setUrl");
        result[3] = getterNameMapping.get("setUsername");
        result[4] = getterNameMapping.get("setPassword");
        result[5] = getterNameMapping.get("setJobName");
        result[6] = getterNameMapping.get("setOrderContent");
        result[7] = getterNameMapping.get("setPlanFrequency");
        result[8] = getterNameMapping.get("setDailyTime");
        result[9] = getterNameMapping.get("setDay");
        result[10] = getterNameMapping.get("setDeclaration");
        result[11] = getterNameMapping.get("setStatus");
        result[12] = getterNameMapping.get("setStates");
        result[13] = getterNameMapping.get("setCreateTime");
        return result;
    }
}
