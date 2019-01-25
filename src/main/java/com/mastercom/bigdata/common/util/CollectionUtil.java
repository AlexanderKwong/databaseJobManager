package com.mastercom.bigdata.common.util;

import java.util.Collection;

/**
 * Created by Kwong on 2017/9/22.
 */
public final class CollectionUtil {

    private CollectionUtil(){}


    public static <M> void collect(Iterable<? extends M> iterable, Collection<? super M> collecter){
        for (M m : iterable) {
            collecter.add(m);
        }
    }
}
