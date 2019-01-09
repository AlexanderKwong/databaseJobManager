package com.mastercom.bigdata.tools.adapter;

import com.mastercom.bigdata.model.IModel;

/**
 * Created by Kwong on 2019/1/9.
 */
public interface ModelAdapter<T, M extends IModel> {

    M from(T object);
}
