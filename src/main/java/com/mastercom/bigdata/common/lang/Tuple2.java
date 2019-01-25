package com.mastercom.bigdata.common.lang;

/**
 * Created by Kwong on 2018/1/10.
 */
public class Tuple2<T, D> {

    public final T first;

    public final D second;

    public Tuple2(T first, D second){
        this.first = first;
        this.second = second;
    }
}
