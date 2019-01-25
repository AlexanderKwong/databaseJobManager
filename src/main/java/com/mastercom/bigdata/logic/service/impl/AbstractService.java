package com.mastercom.bigdata.logic.service.impl;

import com.mastercom.bigdata.logic.dao.IDao;
import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.logic.service.IService;

import java.util.List;
import java.util.Objects;

/**
 * Created by Kwong on 2017/9/23.
 */
public abstract class AbstractService<T extends IModel> implements IService<T> {

    protected IDao<T> dao;

    @Override
    public List<T> list(T model) {
        Objects.requireNonNull(model);
        return dao.query(model);
    }

    @Override
    public T find(T model) {
        Objects.requireNonNull(model);
        List<T> list = dao.query(model);
        return (list != null && !list.isEmpty()) ? list.get(0):null;
    }

    @Override
    public int add(T model) {
        Objects.requireNonNull(model);
        return dao.insert(model);
    }

    @Override
    public int update(T model) {
        Objects.requireNonNull(model);
        return dao.updateById(model.getId(), model);
    }

    @Override
    public int remove(T model) {
        Objects.requireNonNull(model);
        return dao.delete(model);
    }

    @Override
    public T findById(Integer id) {
        return dao.queryById(id);
    }

}
