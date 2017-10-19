package com.mastercom.bigdata.logic.service;

import java.util.List;

/**
 * Created by Kwong on 2017/9/22.
 */
public interface IService<T> {

    List<T> list(T model) throws Exception;

    T find(T model) throws Exception;

    T findById(Integer id) throws Exception;

    int add(T model) throws Exception;

    int update(T model) throws Exception;

    int remove(T model) throws Exception;
}
