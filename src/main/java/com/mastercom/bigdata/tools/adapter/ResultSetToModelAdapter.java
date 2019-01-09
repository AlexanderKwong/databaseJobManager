package com.mastercom.bigdata.tools.adapter;

import com.mastercom.bigdata.model.IModel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * Created by Kwong on 2019/1/8.
 */
public class ResultSetToModelAdapter<M extends IModel> implements ModelAdapter<ResultSet,M> {

    ResultSetMetaData resultSetMetaData;

    @Override
    public M from(ResultSet resultSet) {
        return null;
    }
}
