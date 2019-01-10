package com.mastercom.bigdata.tools;

/**
 * Created by Kwong on 2019/1/10.
 */
public class Tuple3<T, D, R> {

    public final T first;

    public final D second;

    public final R third;

    public Tuple3(T first, D second, R third){
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
