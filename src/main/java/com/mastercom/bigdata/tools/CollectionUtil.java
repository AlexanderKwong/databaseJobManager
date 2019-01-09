package com.mastercom.bigdata.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Kwong on 2017/9/22.
 */
public final class CollectionUtil {

    public static Object[] toArray(List list){
        return null;
    }

    public static <M> void collect(Iterable<? extends M> iterable, Collection<? super M> collecter){
        for (M m : iterable) {
            collecter.add(m);
        }
    }
}
