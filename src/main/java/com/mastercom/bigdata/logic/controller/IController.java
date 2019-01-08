package com.mastercom.bigdata.logic.controller;

import com.mastercom.bigdata.model.IModel;

/**
 * Created by Kwong on 2017/9/22.
 */
public interface IController<T extends IModel> {

    ModelWrapper<T> get(T model);

    ModelWrapper<T> put(T model);

    ModelWrapper<T> delete(T model);
}
