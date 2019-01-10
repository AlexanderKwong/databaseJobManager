package com.mastercom.bigdata.logic.service;

import java.util.List;

/**
 * Created by Kwong on 2017/9/22.
 */
public interface IService<T> {

    List<T> list(T model);

    T find(T model);

    T findById(Integer id);

    int add(T model);

    int update(T model);

    int remove(T model);
}
