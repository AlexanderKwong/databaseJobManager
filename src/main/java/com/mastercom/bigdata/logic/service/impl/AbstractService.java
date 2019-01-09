package com.mastercom.bigdata.logic.service.impl;

import com.mastercom.bigdata.logic.dao.IDAO;
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

    protected IDAO<T> dao;

    @Override
    public List<T> list(T model) throws Exception {
        Objects.requireNonNull(model);
        return dao.query(model);
    }

    @Override
    public T find(T model) throws Exception {
        Objects.requireNonNull(model);
        List<T> list = dao.query(model);
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
        return dao.updateById(model.getId(), model);
    }

    @Override
    public int remove(T model) throws Exception {
        Objects.requireNonNull(model);
        return dao.delete(model);
    }

    @Override
    public T findById(Integer id) throws Exception {
        return dao.queryById(id);
    }

}
