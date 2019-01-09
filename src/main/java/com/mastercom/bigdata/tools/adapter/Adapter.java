package com.mastercom.bigdata.tools.adapter;


/**
 * Created by Kwong on 2019/1/9.
 */
public interface Adapter<T, M> {

    M from(T object);
}
