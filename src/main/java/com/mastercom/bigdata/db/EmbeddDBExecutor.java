package com.mastercom.bigdata.db;

import com.mastercom.bigdata.exception.SqlException;
import com.mastercom.bigdata.logic.Constants;
import com.mastercom.bigdata.common.adapter.ResultSetAdapter;
import com.mastercom.bigdata.common.sql.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by Kwong on 2018/1/8.
 */
public class EmbeddDBExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddDBExecutor.class);

    private EmbeddDBExecutor(){}

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
        logSql(formatedSql, null);
        Connection conn = getConnection();
        try (Statement stat = conn.createStatement()){
            return stat.executeUpdate(formatedSql);
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public static int execute(String formatedSql, List<String> params){
        logSql(formatedSql, params);
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

    public static <M> Iterable<M> query(String formatedSql, ResultSetAdapter<M> resultSetAdapter){
        logSql(formatedSql, null);
        Connection conn = getConnection();
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(formatedSql);
            return resultSetAdapter.from(rs);
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public static <M> Iterable<M> query(String formatedSql, List<String> params, ResultSetAdapter<M> resultSetAdapter){
        logSql(formatedSql, params);
        Connection conn = getConnection();
        try {
            PreparedStatement stat = conn.prepareStatement(formatedSql);
            for (int i = 0; i < params.size(); i++) {
                stat.setString(i + 1, params.get(i));
            }
            ResultSet rs = stat.executeQuery();
            return resultSetAdapter.from(rs);
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
                    } catch (Exception e) {
                        throw new SqlException(e);
                    }
                }
            }
        }
        return connection;
    }

    private static void logSql(String sql, List<String> params){
        if (LOG.isDebugEnabled()){
            LOG.debug("SQL:{}", sql);
            if (params != null){
                LOG.debug("PARAMETERS{}", params);
            }
        }
    }
}
