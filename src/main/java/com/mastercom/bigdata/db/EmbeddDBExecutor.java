package com.mastercom.bigdata.db;

import com.mastercom.bigdata.exception.SqlException;
import com.mastercom.bigdata.logic.Constants;
import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.tools.adapter.ResultSetToModelAdapter;
import com.mastercom.bigdata.tools.sql.DBUtil;

import java.sql.*;
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
        Connection conn = getConnection();
        try (Statement stat = conn.createStatement()){
            return stat.executeUpdate(formatedSql);
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public static int execute(String formatedSql, List<String> params){
        Connection conn = getConnection();
        try (PreparedStatement stat = conn.prepareStatement(formatedSql)){
            for (int i = 0; i < params.size(); i++) {
                stat.setString(i + 1, params.get(i));
            }
            return stat.executeUpdate();
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public static <M extends IModel> Iterable<M> query(String formatedSql, ResultSetToModelAdapter<M> resultSetToModelAdapter){
        Connection conn = getConnection();
        try (Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(formatedSql)){
            return resultSetToModelAdapter.from(rs);
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public static <M extends IModel> Iterable<M> query(String formatedSql, List<String> params, ResultSetToModelAdapter<M> resultSetToModelAdapter){
        Connection conn = getConnection();
        try (PreparedStatement stat = conn.prepareStatement(formatedSql)){
            for (int i = 0; i < params.size(); i++) {
                stat.setString(i + 1, params.get(i));
            }
            ResultSet rs = stat.executeQuery();
            return resultSetToModelAdapter.from(rs);
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    private static Connection getConnection(){
        if (connection == null){
            synchronized (EmbeddDBExecutor.class){
                if (connection == null){
                    try {
                        connection = DBUtil.getConnectionByType(Constants.DEFAULT_DATA_SOURCE, Constants.DEFAULT_DB_URL, Constants.DEFAULT_DB_USERBANE, Constants.DEFAULT_DB_PASSWORD);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }
}
