package com.mastercom.bigdata.logic.dao.impl;

import com.mastercom.bigdata.logic.dao.IDAO;
import com.mastercom.bigdata.model.impl.Job;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class JobDAO implements IDAO<Job> {
    @Override
    public void createTable() {
        String sql = "";
    }

    @Override
    public int insert(Job model) {
        String sql = new SQL()
                .INSERT_INTO("PERSON")
                .VALUES("ID, FIRST_NAME", "#{id}, #{firstName}")
                .VALUES("LAST_NAME", "#{lastName}")
                .toString();
        return 0;
    }

    @Override
    public int delete(Job model) {
        /*return new SQL() {{
            DELETE_FROM("PERSON");
            WHERE("ID = #{id}");
        }}.toString();*/
        return 0;
    }

    @Override
    public List<Job> query(Job model) {
       /* String sql = new SQL() {{
            SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FIRST_NAME, P.LAST_NAME");
            FROM("PERSON P");
            if (id != null) {
                WHERE("P.ID like #{id}");
            }
            if (firstName != null) {
                WHERE("P.FIRST_NAME like #{firstName}");
            }
            if (lastName != null) {
                WHERE("P.LAST_NAME like #{lastName}");
            }
            ORDER_BY("P.LAST_NAME");
        }}.toString();*/
        return null;
    }

    @Override
    public Job queryById(Integer id) {
        return null;
    }

    @Override
    public int updateById(Integer id, Job model) {
        /*return new SQL() {{
            UPDATE("PERSON");
            SET("FIRST_NAME = #{firstName}");
            WHERE("ID = #{id}");
        }}.toString();*/
        return 0;
    }
}
