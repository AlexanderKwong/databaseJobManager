package com.mastercom.bigdata.logic.controller;

import com.mastercom.bigdata.bean.IModel;

import java.util.List;

/**
 * 返回数据包装。 格式：{operation:0/1/2/3, returnCode:0/1/2/... , data:[row1, row2, row3] , msg: "success"}
 * Created by Kwong on 2017/9/22.
 */
public class ModelWrapper<T extends IModel> {

    public static final int SUCCESS = 0;

    public static final int FAILED = 1;

    public static final int OPERA_NEW = 0;

    public static final int OPERA_MODIFY = 1;

    public static final int OPERA_DELETE = 2;

    public static final int OPERA_QUERY = 3;

    public static final int OPERA_UNDEFINED = 9;

    private final int operation;

    private final int returnCode;

    private final List<T> data;

    private final String msg;

    public ModelWrapper(final int operation, final int returnCode, final List<T> data, final String msg) {
        this.operation = operation;
        this.returnCode = returnCode;
        this.data = data;
        this.msg = msg;
    }

    public int getOperation(){
        return operation;
    }

    public List<T> getData(){
        return data;
    }

    public int getReturnCode(){
        return returnCode;
    }

    public String getMsg(){
        return msg;
    }
}
