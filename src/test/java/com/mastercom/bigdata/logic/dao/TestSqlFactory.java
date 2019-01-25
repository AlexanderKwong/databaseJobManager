package com.mastercom.bigdata.logic.dao;

import com.mastercom.bigdata.logic.Constants;
import com.mastercom.bigdata.model.impl.Job;
import com.mastercom.bigdata.common.sql.DBUtil;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Kwong on 2018/1/8.
 */
public class TestSqlFactory {

    @Test
    public void testInsertSql(){
        String expectedSql = "INSERT INTO JOB" +
                "(DATABASE_TYPE,URL,USERNAME,PASSWORD,JOB_NAME,ORDER_CONTENT,PLAN_FREQUENCY,DAILY_TIME,DAY,DECLARATION,STATUS,STATES,CREATE_TIME)" +
                " VALUES(#{databaseType},#{url},#{username},#{password},#{jobName},#{orderContent},#{planFrequency},#{dailyTime},#{day},#{declaration},#{status},#{states},#{createTime})";
        String insertSql = SqlFactory.getInsertSql(Job.class);
        System.out.println(insertSql);
        Assert.assertEquals("error", replaceSpace(expectedSql), replaceSpace(insertSql));
    }

    @Test
    public void testQueryByIdSql(){
        String expectedSql = "SELECT ID,DATABASE_TYPE,URL,USERNAME,PASSWORD,JOB_NAME,ORDER_CONTENT,PLAN_FREQUENCY,DAILY_TIME,DAY,DECLARATION,STATUS,STATES,CREATE_TIME FROM JOB WHERE ID = #{id}";

        String querySql = SqlFactory.getQueryByIdSql(Job.class);
        System.out.println(querySql);
        Assert.assertEquals("error", replaceBrackets(replaceSpace(expectedSql)), replaceBrackets(replaceSpace(querySql)));
    }

    @Test
    public void testQuerySql(){
        Job job = new Job();
        String querySql = SqlFactory.getQuerySql(job);
        System.out.println(querySql);
        Assert.assertFalse(querySql.contains("WHERE"));

        job.setCreateTime("20180108");
        job.setDay("2");
        job.setJobName("hello world");
        querySql = SqlFactory.getQuerySql(job);
        System.out.println(querySql);
        String condition = querySql.split("WHERE")[1];
        Assert.assertTrue(condition.split("AND").length == 3);
        Assert.assertTrue(condition.contains("JOB_NAME = #{jobName}"));
        Assert.assertTrue(condition.contains("DAY = #{day}"));
        Assert.assertTrue(condition.contains("CREATE_TIME = #{createTime}"));
    }

    @Test
    public void testQueryParameters(){
        Job job = new Job();
        job.setCreateTime("20180108");
        job.setDay("2");
        job.setJobName("hello world");
        String querySql = SqlFactory.getQuerySql(job);
        System.out.println(querySql);
        List<String> values = SqlFactory.getParameterValues(querySql, job);

        System.out.println(values);
        Assert.assertTrue(values.size() == 3);
        Assert.assertTrue(values.contains("hello world"));
        Assert.assertTrue(values.contains("2"));
        Assert.assertTrue(values.contains("20180108"));

    }

    @Test
    public void testReplaceSqlParamsWithQuestionMark(){
        Job job = new Job();
        job.setCreateTime("20180108");
        job.setDay("2");
        job.setJobName("hello world");
        String querySql = SqlFactory.getQuerySql(job);
        System.out.println(querySql);
        System.out.println(SqlFactory.replaceSqlParamsWithQuestionMark(querySql));
    }

    @Test
    public void testCreateTableSql(){
        String createSql = SqlFactory.getCreateSql(Job.class);
        System.out.println(createSql);

        Connection conn = null;
        List<String[]> results = null;
        try {
            conn = DBUtil.getConnectionByType(Constants.DEFAULT_DATA_SOURCE, Constants.DEFAULT_DB_URL, Constants.DEFAULT_DB_USERBANE, Constants.DEFAULT_DB_PASSWORD);

            results = DBUtil.executeQuery(String.format("SELECT DISTINCT TABLENAME FROM SYS.SYSTABLES WHERE TABLENAME IN (%s)", "'JOB'"), conn);
            if (!results.isEmpty()){
                DBUtil.execute("DROP TABLE JOB", conn);
                results = DBUtil.executeQuery(String.format("SELECT DISTINCT TABLENAME FROM SYS.SYSTABLES WHERE TABLENAME IN (%s)", "'JOB'"), conn);
            }
            Assert.assertTrue(results.isEmpty());

            DBUtil.execute(createSql, conn);

            results = DBUtil.executeQuery(String.format("SELECT DISTINCT TABLENAME FROM SYS.SYSTABLES WHERE TABLENAME IN (%s)", "'JOB'"), conn);
            Assert.assertFalse(results.isEmpty());
            DBUtil.execute("DROP TABLE JOB", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(conn, null, null);
        }
    }

    private String replaceSpace(String str){
       return str.replaceAll("\\s+", "").replaceAll("\\n+", "");
    }

    private String replaceBrackets(String str){
        return str.replaceAll("\\(+", "").replaceAll("\\)+", "");
    }
}
