package com.mastercom.bigdata.logic.dao.impl;

import com.mastercom.bigdata.db.EmbeddDBExecutor;
import com.mastercom.bigdata.logic.dao.IDao;
import com.mastercom.bigdata.logic.dao.SqlFactory;
import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.common.util.ClassUtil;
import com.mastercom.bigdata.common.util.CollectionUtil;
import com.mastercom.bigdata.common.adapter.ResultSetToIntegerAdapter;
import com.mastercom.bigdata.common.adapter.ResultSetToModelAdapter;

import java.util.*;

public class BaseDao<M extends IModel> implements IDao<M> {

    private final Class<M> clazz = ClassUtil.getGeneralClass(getClass(), 0);

    private final String CREATE_SQL = SqlFactory.getCreateSql(clazz);

    private final String INSERT_SQL = SqlFactory.getInsertSql(clazz);

    private final String QUERY_BY_ID_SQL = SqlFactory.getQueryByIdSql(clazz);

    private final String DELETE_SQL = SqlFactory.getDeleteSql(clazz);

    private final String UPDATE_SQL = SqlFactory.getUpdateByIdSql(clazz);

    ResultSetToModelAdapter<M> resultSetToModelAdapter = new ResultSetToModelAdapter<>(clazz);

    public BaseDao(){
        if (!tableExists()){
            create();
        }
    }

    @Override
    public void create() {
        EmbeddDBExecutor.execute(CREATE_SQL);
    }

    @Override
    public int insert(M model) {
        String formatedSql = SqlFactory.replaceSqlParamsWithQuestionMark(INSERT_SQL);
        List<String> params = SqlFactory.getParameterValues(INSERT_SQL, model);
        return EmbeddDBExecutor.execute(formatedSql, params);
    }

    @Override
    public int delete(M model) {
        String formatedSql = SqlFactory.replaceSqlParamsWithQuestionMark(DELETE_SQL);
        List<String> params = SqlFactory.getParameterValues(DELETE_SQL, model);
        return EmbeddDBExecutor.execute(formatedSql, params);
    }

    @Override
    public List<M> query(M model) {
        String querySql = SqlFactory.getQuerySql(model);
        String formatedSql = SqlFactory.replaceSqlParamsWithQuestionMark(querySql);
        List<String> params = SqlFactory.getParameterValues(querySql, model);
        Iterable<M> result = EmbeddDBExecutor.query(formatedSql, params, resultSetToModelAdapter);
        List<M> modelList = new ArrayList<>();
        CollectionUtil.collect(result, modelList);
        return modelList;
    }

    @Override
    public M queryById(Integer id) {
        String formatedSql = SqlFactory.replaceSqlParamsWithQuestionMark(QUERY_BY_ID_SQL);
        List<String> params = Collections.singletonList(String.valueOf(id));
        Iterable<M> result = EmbeddDBExecutor.query(formatedSql, params, resultSetToModelAdapter);
        Iterator<M> jobIterator = result.iterator();
        if (jobIterator.hasNext()){
            return jobIterator.next();
        }else return null;
    }

    @Override
    public int updateById(Integer id, M model) {
        String formatedSql = SqlFactory.replaceSqlParamsWithQuestionMark(UPDATE_SQL);
        List<String> params = SqlFactory.getParameterValues(UPDATE_SQL, model);
        return EmbeddDBExecutor.execute(formatedSql, params);
    }

    private boolean tableExists(){
        String sql = "SELECT 1 FROM SYS.SYSTABLES WHERE TABLENAME='" + SqlFactory.tableName(clazz)+"'";
        Iterable<Integer> result = EmbeddDBExecutor.query(sql, new ResultSetToIntegerAdapter());
        return result.iterator().hasNext();
    }
}
