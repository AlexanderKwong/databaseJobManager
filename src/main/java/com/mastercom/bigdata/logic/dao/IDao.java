package com.mastercom.bigdata.logic.dao;

import com.mastercom.bigdata.model.IModel;

import java.util.List;

public interface IDao<M extends IModel> {

    void create();

    int insert(M model);

    int delete(M model);

    List<M> query(M model);

    M queryById(Integer id);

    int updateById(Integer id, M model);


}
