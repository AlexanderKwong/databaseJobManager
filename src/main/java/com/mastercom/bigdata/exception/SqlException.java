package com.mastercom.bigdata.exception;

/**
 * Created by Kwong on 2018/1/8.
 */
public class SqlException extends RuntimeException{

    public SqlException(){}


    public SqlException(Throwable e){
        super(e);
    }
}
