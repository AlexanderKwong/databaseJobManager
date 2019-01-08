package com.mastercom.bigdata.logic.service.impl;

import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.logic.Constants;
import com.mastercom.bigdata.tools.ClassUtil;
import com.mastercom.bigdata.tools.sql.DBUtil;
import com.mastercom.bigdata.db.EmbeddDBTable;
import com.mastercom.bigdata.logic.service.IService;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Kwong on 2017/9/23.
 */
public abstract class AbstractService<T extends IModel> implements IService<T> {

    protected DAO<T> dao;

    @Override
    public List<T> list(T model) throws Exception {
        Objects.requireNonNull(model);
        return dao.list(model);
    }

    @Override
    public T find(T model) throws Exception {
        Objects.requireNonNull(model);
        List<T> list = dao.list(model);
        return (list != null && !list.isEmpty()) ? list.get(0):null;
    }

    @Override
    public int add(T model) throws Exception {
        Objects.requireNonNull(model);
        return dao.insert(model);
    }

    @Override
    public int update(T model) throws Exception {
        Objects.requireNonNull(model);
        return dao.update(model);
    }

    @Override
    public int remove(T model) throws Exception {
        Objects.requireNonNull(model);
        return dao.delete(model);
    }

    @Override
    public T findById(Integer id) throws Exception {
        if(id == null) return null;
        Class clazz = ClassUtil.getGeneralClass(this.getClass(), 0);
        Method setIdMethod = clazz.getMethod("setId", Integer.class);
        T model = (T)clazz.newInstance();
        setIdMethod.invoke(model, id);
        return find(model);
    }

    /**
     * 简单的 数据访问对象， 与对应的表相关
     * UPDATE和delete必须根据id来操作
     * @param <T>
     */
    protected class DAO<T>{

        EmbeddDBTable table;

        Method[] getters;

        Method[] setters;

        String columnsWithComma;

        /**
         *
         * @param table
         * @param getters getters要与table.getColumns数组顺序对应
         */
        public DAO(EmbeddDBTable table, Method[] getters, Method[] setters){

            this.table = table;
            //获取所有列
            StringBuilder sb = new StringBuilder();
//            int columnCnt = this.table.getColumns().length;
            for (String[] column : table.getColumns()){
                sb.append(column[0]).append(",");
            }
            this.getters = getters;
            this.setters = setters;
            this.columnsWithComma = sb.delete(sb.length()-1, sb.length()).toString();
        }

        private List<String[]> executeQuery(String sql) throws Exception{
            Connection conn = DBUtil.getConnectionByType(Constants.DEFAULT_DATA_SOURCE, Constants.DEFAULT_DB_URL, Constants.DEFAULT_DB_USERBANE, Constants.DEFAULT_DB_PASSWORD);
            try{
                return DBUtil.executeQuery(sql, conn);
            }finally {
                DBUtil.close(conn, null, null);
            }
        }

        private int execute(String sql) throws Exception {
            Connection conn = DBUtil.getConnectionByType(Constants.DEFAULT_DATA_SOURCE, Constants.DEFAULT_DB_URL, Constants.DEFAULT_DB_USERBANE, Constants.DEFAULT_DB_PASSWORD);
            try{
                return DBUtil.execute(sql, conn);
            }finally {
                DBUtil.close(conn, null, null);
            }
        }

        public List<T> list(T condition) throws Exception{
            List<String[]> results = executeQuery(getSelectSQL(condition));
            List<T> beans = new ArrayList<>();
            String[][] columns = table.getColumns();
            for (String [] row : results){
                T bean = (T)(ClassUtil.getGeneralClass(AbstractService.this.getClass(), 0).newInstance());
                for (int i = 0; i < columns.length; i++){
                    String[] column = columns[i];
                    if (column[1].equalsIgnoreCase("integer")){
                        setters[i].invoke(bean, Integer.parseInt(row[i]));
                    }else{
                        setters[i].invoke(bean, row[i]);
                    }
                }
                beans.add(bean);
            }
            return beans;
        }

        public int insert(T condition) throws Exception{

            return execute(getInsertSQL(condition));
        }

        public int update(T condition) throws Exception{
            return execute(getUpdateSQL(condition));
        }

        public int delete(T condition) throws Exception{
            return execute(getDeleteSQL(condition));
        }

        private String getInsertSQL(T condition) throws Exception {
            StringBuilder sbInset = new StringBuilder("INSERT INTO ");
            int columnCnt = columnsWithComma.split(",").length;
            sbInset.append(table.getTableName())
                    .append("(")
                    .append(columnsWithComma.replace("id,", ""))
                    .append(") VALUES(");
            //添加值
            for (int i = 1; i < getters.length; i++){

                Object o = getters[i].invoke(condition);
                if (Objects.nonNull(o)){
                    if (table.getColumns()[i][1].equalsIgnoreCase("integer")){
                        sbInset.append(o.toString()).append(",");
                    }else{
                        sbInset.append("'").append(o.toString()).append("'").append(",");
                    }
                }else {
                    sbInset.append("NULL").append(",");
                }
            }
            return sbInset.delete(sbInset.length()-1, sbInset.length()).append(")").toString();
        }

        private String getSelectSQL(T condition) throws Exception {
            StringBuilder sbSelect = new StringBuilder("SELECT ");
            sbSelect.append(columnsWithComma)
                    .append(" FROM ")
                    .append(table.getTableName());
//          添加条件
            sbSelect.append(" WHERE 1=1");
            for (int i = 0; i < getters.length; i++){
                Object o = getters[i].invoke(condition);
                if (Objects.nonNull(o)){
                    String columnName = table.getColumns()[i][0];
                    if (table.getColumns()[i][1].equalsIgnoreCase("integer")){
                        sbSelect.append(" AND ").append(columnName).append("=").append(o.toString());
                    }else {
                        sbSelect.append(" AND ").append(columnName).append("=").append("'").append(o.toString()).append("'");
                    }
                }
            }
            return sbSelect.toString();
        }

        private String getUpdateSQL(T condition) throws Exception {
            StringBuilder sbUpdate = new StringBuilder("UPDATE ");
            sbUpdate.append(table.getTableName())
                    .append(" SET ");
            for (int i = 1; i < getters.length; i++){
      /*          if ("getId".equals(getters[i].getName())){
                    continue;
                }*/
                Object o = getters[i].invoke(condition);
                if (Objects.nonNull(o)){
                    String columnName = table.getColumns()[i][0];
                    if (table.getColumns()[i][1].equalsIgnoreCase("integer")){
                        sbUpdate.append(columnName).append("=").append(o.toString()).append(",");
                    }else{
                        sbUpdate.append(columnName).append("=").append("'").append(o.toString()).append("'").append(",");
                    }
                }
            }
            sbUpdate.delete(sbUpdate.length()-1, sbUpdate.length());
            sbUpdate.append("WHERE ID=").append(getters[0].invoke(condition));
            return sbUpdate.toString();
        }

        private String getDeleteSQL(T condition) throws Exception {
            return new StringBuilder("DELETE FROM ")
                    .append(table.getTableName())
                    .append(" WHERE ID=")
                    .append(getters[0].invoke(condition)).toString();
        }
    }
}
