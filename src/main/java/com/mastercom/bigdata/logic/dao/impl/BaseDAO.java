package com.mastercom.bigdata.logic.dao.impl;

import com.mastercom.bigdata.db.EmbeddDBExecutor;
import com.mastercom.bigdata.logic.dao.IDAO;
import com.mastercom.bigdata.logic.dao.SqlFactory;
import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.tools.CollectionUtil;
import com.mastercom.bigdata.tools.adapter.ResultSetToModelAdapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BaseDAO<M extends IModel> implements IDAO<M> {

    private final Class<M> clazz = getTypeClass();

    private final String CREATE_SQL = SqlFactory.getCreateSql(clazz);

    private final String INSERT_SQL = SqlFactory.getInsertSql(clazz);

    private final String QUERY_BY_ID_SQL = SqlFactory.getQueryByIdSql(clazz);

    private final String DELETE_SQL = SqlFactory.getInsertSql(clazz);

    private final String UPDATE_SQL = SqlFactory.getInsertSql(clazz);

    ResultSetToModelAdapter<M> resultSetToModelAdapter = new ResultSetToModelAdapter<>();

    /* 获取泛型指向的类 */
    private Class<M> getTypeClass(){
        Type type = getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        if (types[0] instanceof Class)
            return ((Class<M>) types[0]);
        else throw new IllegalArgumentException();
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
        return CollectionUtil.toList(result);
    }

    @Override
    public M queryById(Integer id) {
        String formatedSql = SqlFactory.replaceSqlParamsWithQuestionMark(QUERY_BY_ID_SQL);
        List<String> params = Arrays.asList(new String[]{String.valueOf(id)});
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

}
