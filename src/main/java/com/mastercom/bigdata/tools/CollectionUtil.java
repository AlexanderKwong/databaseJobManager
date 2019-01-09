package com.mastercom.bigdata.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kwong on 2017/9/22.
 */
public final class CollectionUtil {

    public static Object[] toArray(List list){
        return null;
    }

    public static <M> List<? super M> toList(Iterable<? extends M> iterable){
        List<? super M> result = new ArrayList<>();
        for (M m : iterable) {
            result.add(m);
        }
        return result;
    }
}
