package com.mastercom.bigdata.common.adapter;


/**
 * Created by Kwong on 2018/1/9.
 */
public interface Adapter<T, M> {

    M from(T object);
}
