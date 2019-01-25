package com.mastercom.bigdata.common.adapter;

import java.util.List;

/**
 * Created by Kwong on 2018/1/10.
 */
public class ResultSetToIntegerAdapter extends ResultSetAdapter<Integer>{
    @Override
    protected Integer trans(List<ResultRowStruct> rowStructs) {
        return (Integer)rowStructs.get(0).columnValue;
    }
}
