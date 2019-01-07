package com.mastercom.bigdata.logic.dao;

import com.mastercom.bigdata.model.IModel;

import java.util.List;

public interface IDAO<M extends IModel> {

    void createTable();

    int insert(M model);

    int delete(M model);

    List<M> query(M model);

    M queryById(Integer id);

    int updateById(Integer id, M model);


}
