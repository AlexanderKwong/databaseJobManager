package com.mastercom.bigdata.tools.adapter;

import com.mastercom.bigdata.exception.SqlException;
import com.mastercom.bigdata.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created by Kwong on 2019/1/8.
 */
public class ResultSetToModelAdapter<M extends IModel> implements Adapter<ResultSet, Iterable<M>> {

    private static Logger LOG = LoggerFactory.getLogger(ResultSetToModelAdapter.class);

    private Class<M> mClass;

    public ResultSetToModelAdapter(Class<M> mClass){
        this.mClass = mClass;
    }

    @Override
    public Iterable<M> from(final ResultSet rs) {

        return new Iterable<M>() {
            @Override
            public Iterator<M> iterator() {
                return new ResultToModelIterator(rs);
            }
        };
    }

    private String columnNameToFieldName(String columnName){
        return null;
    }

    private static Method setter(Class<?> clazz, String fieldName){
        return null;
    }

    private static Object columnValueToFieldValue(String columnTypeName, Object columnValue){
        return columnValue;
    }

    private class ResultToModelIterator implements Iterator<M>{

        ResultSetMetaData rm;
        ResultSet rs;

        public ResultToModelIterator(ResultSet rs){
            try {
                this.rm = rs.getMetaData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.rs = rs;
        }

        @Override
        public boolean hasNext() {
            try {
                return rs.next();
            } catch (SQLException e) {
                throw new SqlException(e);
            }
        }

        @Override
        public M next() {
            try {
                M model = mClass.newInstance();
                setFieldValue(model);
                return model;
            } catch (Exception e) {
                LOG.error("resultset转换成实体出错", e);
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void setFieldValue(M model) throws SQLException {
            for (int i = 1; i <= rm.getColumnCount(); i++)
            {
                String columnName = rm.getColumnName(i);
                String columnTypeName = rm.getColumnTypeName(i);
                String fieldName = columnNameToFieldName(columnName);
                Method setter = setter(mClass, fieldName);
                if (setter == null){
                    LOG.warn(String.format("在%s类中找不到%s的setter方法", mClass.getSimpleName(), fieldName));
                    continue;
                }
                Object columnValue = rs.getObject(i);
                Object fieldValue = columnValueToFieldValue(columnTypeName, columnValue);
                try {
                    setter.invoke(model, fieldValue);
                } catch (Exception e) {
                    LOG.warn(String.format("在%s类中执行%s方法失败", mClass.getSimpleName(), setter.getName()));
                    //ignore
                }
            }
        }
    }
}
