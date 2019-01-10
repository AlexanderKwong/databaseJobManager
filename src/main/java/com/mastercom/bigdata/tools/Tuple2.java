package com.mastercom.bigdata.tools;

/**
 * Created by Kwong on 2019/1/10.
 */
public class Tuple2<T, D> {

    public final T first;

    public final D second;

    public Tuple2(T first, D second){
        this.first = first;
        this.second = second;
    }
}
