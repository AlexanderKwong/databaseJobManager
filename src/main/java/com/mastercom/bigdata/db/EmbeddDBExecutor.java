package com.mastercom.bigdata.db;

import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.tools.adapter.ResultSetToModelAdapter;
import com.mastercom.bigdata.tools.sql.DBUtil;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Kwong on 2019/1/8.
 */
public class EmbeddDBExecutor {

    static Connection connection;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                DBUtil.close(connection, null, null);
            }
        }));

    }

    public static int execute(String formatedSql){
        return 0;
    }

    public static int execute(String formatedSql, List<String> params){
        return 0;
    }

    public static <M extends IModel> Iterable<M> query(String formatedSql, ResultSetToModelAdapter<M> resultSetToModelAdapter){
        return null;
    }

    public static <M extends IModel> Iterable<M> query(String formatedSql, List<String> params, ResultSetToModelAdapter<M> resultSetToModelAdapter){
        return null;
    }


}
