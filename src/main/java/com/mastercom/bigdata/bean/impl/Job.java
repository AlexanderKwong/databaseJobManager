package com.mastercom.bigdata.bean.impl;

import com.mastercom.bigdata.bean.IModel;
import com.mastercom.bigdata.tools.TimeUtil;

/**
 * Created by Kwong on 2017/9/22.
 */
public class Job implements IModel {

    private Integer id;
    private String databaseType;
    private String url;
    private String username;
    private String password;
    private String jobName;
    private String orderContent;
    private String planFrequency;
    private String dailyTime;
    private String day;
    private String declaration;
    /**
     * 1:启用；0：禁用
     */
    private Integer status;
    private String states;
    private String createTime;

    public Job(){}

    public Job(Integer id){
        this.id = id;
    }

    public Job(Job sample){
        this.id = sample.getId();
        this.databaseType = sample.databaseType;
        this.url = sample.url;
        this.username = sample.username;
        this.password = sample.password;
        this.jobName = sample.jobName;
        this.orderContent = sample.orderContent;
        this.planFrequency = sample.planFrequency;
        this.dailyTime = sample.dailyTime;
        this.day = sample.day;
        this.declaration = sample.declaration;
        this.status = sample.status;
        this.states = sample.states;
        this.createTime = sample.createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public String getPlanFrequency() {
        return planFrequency;
    }

    public void setPlanFrequency(String planFrequency) {
        this.planFrequency = planFrequency;
    }

    public String getDailyTime() {
        return dailyTime;
    }

    public void setDailyTime(String dailyTime) {
        this.dailyTime = dailyTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static class Builder{

        private Job job;

        public Builder(){
            job = new Job();
        }

        public Builder withId(Integer id) {
            job.id = id;
            return this;
        }

        public Builder withDatabaseType(String databaseType) {
            job.databaseType = databaseType;
            return this;
        }

        public Builder withUrl(String url) {
            job.url = url;
            return this;
        }
        public Builder withUsername(String username) {
            job.username = username;
            return this;
        }
        public Builder withPassword(String password) {
            job.password = password;
            return this;
        }
        public Builder withJobName(String jobName) {
            job.jobName = jobName;
            return this;
        }
        public Builder withOrderContent(String orderContent) {
            job.orderContent = orderContent;
            return this;
        }
        public Builder withPlanFrequency(String planFrequency) {
            job.planFrequency = planFrequency;
            return this;
        }
        public Builder withDailyTime(String dailyTime) {
            job.dailyTime = dailyTime;
            return this;
        }
        public Builder withDay(String day) {
            job.day = day;
            return this;
        }
        public Builder withDeclaration(String declaration) {
            job.declaration = declaration;
            return this;
        }
        public Builder withStatus(Integer status) {
            job.status = status;
            return this;
        }
        public Builder withStates(String states) {
            job.states = states;
            return this;
        }
        public Builder withCreateTime(String createTime) {
            job.createTime = createTime;
            return this;
        }

        public Job build(){
            return job;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n 【Job信息如下】\n").append("Job名称：").append(getJobName()).append("\n");
        if (this.getPlanFrequency().equals("daily")){
            sb.append("Job执行计划：每天").append(getDailyTime());
        }else if (this.getPlanFrequency().equals("week")){
            sb.append("Job执行计划：每周").append(TimeUtil.returnWeekWord(Integer.parseInt(getDay()))).append(getDailyTime());
        }else if (this.getPlanFrequency().equals("month")){
            sb.append("Job执行计划：每月").append(getDay()).append("号").append(getDailyTime());
        }else{

        }
        return sb.toString();
    }
}
