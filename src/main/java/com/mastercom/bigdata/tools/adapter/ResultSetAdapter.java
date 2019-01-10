package com.mastercom.bigdata.tools.adapter;

import com.mastercom.bigdata.exception.SqlException;
import com.mastercom.bigdata.tools.sql.DBUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kwong on 2019/1/8.
 */
public abstract class ResultSetAdapter<M> implements Adapter<ResultSet, Iterable<M>> {

    @Override
    public Iterable<M> from(final ResultSet rs) {

        return new Iterable<M>() {
            @Override
            public Iterator<M> iterator() {
                return new ResultIterator(rs);
            }
        };
    }

    protected abstract M trans(List<ResultRowStruct> rowStructs);

    private class ResultIterator implements Iterator<M>{

        ResultSetMetaData rm;
        ResultSet rs;

        public ResultIterator(ResultSet rs){
            try {
                this.rm = rs.getMetaData();
            } catch (SQLException e) {
                throw new SqlException(e);
            }
            this.rs = rs;
        }

        @Override
        public boolean hasNext() {
            try {
                boolean hasNext = rs.next();
                if (!hasNext){
                    release();
                }
                return hasNext;
            } catch (SQLException e) {
                throw new SqlException(e);
            }
        }

        @Override
        public M next() {
            try {
                List<ResultRowStruct> rowStructs = getRowStruct();
                return trans(rowStructs);
            } catch (SQLException e) {
                throw new SqlException(e);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private List<ResultRowStruct> getRowStruct() throws SQLException {
            List<ResultRowStruct> rowStruct = new ArrayList<>();
            for (int i = 1; i <= rm.getColumnCount(); i++)
            {
                String columnName = rm.getColumnName(i);
                String columnTypeName = rm.getColumnTypeName(i);
                Object columnValue = rs.getObject(i);
                rowStruct.add(new ResultRowStruct(columnName, columnTypeName, columnValue));
            }
            return rowStruct;
        }

        private void release(){
            try {
                Statement statement = rs.getStatement();
                DBUtil.close(null, statement, rs);
            } catch (SQLException e) {
                throw new SqlException(e);
            }
        }

        @Override
        protected void finalize() throws Throwable {
            release();
        }
    }

    protected static class ResultRowStruct{

        public final String columnName;
        public final String columnTypeName;
        public final Object columnValue;

        public ResultRowStruct(String columnName, String columnTypeName, Object columnValue) {
            this.columnName = columnName;
            this.columnTypeName = columnTypeName;
            this.columnValue = columnValue;
        }
    }
}
