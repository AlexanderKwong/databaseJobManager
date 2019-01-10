package com.mastercom.bigdata.tools.sql;

import com.mastercom.bigdata.tools.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DBUtil
 * 获取 greenPlum/SQLServer/MYSQL/ORACLE/Hive的连接
 *
 * Created by Kwong on 2017/9/22.
 */

public final class DBUtil {

    private DBUtil(){}

    private static final Logger LOG = LoggerFactory.getLogger(DBUtil.class);

    /**
     * @param dbDriver 驱动类
     * @param url      连接地址
     * @param user     用户名
     * @param password 密码
     * @return
     */
    public static Connection getConnection(String dbDriver, String url, String user, String password) throws SQLException, ClassNotFoundException {
        Connection conn;
        Class.forName(dbDriver);
        conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    /**
     * 获取 MySQL分库分表/ORACLE/Hive/MySQL四种连接
     *
     * @param dbType 类型
     * @return
     */
    public static Connection getConnectionByType(DBType dbType, String url, String user, String password) throws SQLException, ClassNotFoundException {

        Connection conn = null;

        switch (dbType) {
            case GREENPLUM:
                conn = DBUtil.getConnection(Constants.DB_GREENPLUM_DRIVER, url, user, password);
                break;
            case MYSQL:
                conn = DBUtil.getConnection(Constants.DB_MYSQL_DRIVER, url, user, password);
                break;
            case SQLSERVER:
                conn = DBUtil.getConnection(Constants.DB_SQLSERVER_DRIVER, url, user, password);
                break;
            case ORACLE:
                conn = DBUtil.getConnection(Constants.DB_ORACLE_DRIVER, url, user, password);
                break;
            case HIVESERVER:
                conn = DBUtil.getConnection(Constants.DB_HIVESERVER_DRIVER, url, user, password);
                break;
            case SPARK:
                conn = DBUtil.getConnection(Constants.DB_SPARKSERVER_DRIVER, url, user, password);
                break;
            case DERBY:
                try{
                    conn = DBUtil.getConnection(Constants.DB_DERBY_DRIVER, url, user, password);
                } catch (SQLException se) {
                    if ( se.getSQLState().equals("XJ015") ) {
                        return getConnectionByType(dbType, url, user, password);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return conn;
    }


    /**
     * 关闭连接
     *
     * @param conn
     * @param stat
     * @param rs
     */
    public static void close(Connection conn, Statement stat, ResultSet rs) {

        try {
            if (rs != null) {
                rs.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            LOG.error("", e);
        }

    }

    /**
     * 执行CRUD操作中CUD,查询单独出去
     * @param sql    绑定变量形式的sql语句
     * @param values 绑定变量传入的值
     * @param conn 连接
     * @return
     */
    public static int execute(String sql, List<String> values, Connection conn)
            throws SQLException {

        Objects.requireNonNull(conn);
        try (PreparedStatement stat = conn.prepareStatement(sql)){
            for (int i = 1; i <= values.size(); i++) {
                stat.setString(i, values.get(i - 1));
            }
            return stat.executeUpdate();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOG.error("回滚失败", e1);
            }
            logSqlException(sql, e);
            throw e;
        }
    }

    private static void logSqlException(String sql, SQLException e) {
        LOG.error("执行操作：【{}】异常", sql, e);
    }

    public static List<String[]> executeQuery(String sql, List<String> values,  Connection conn)
            throws SQLException {

        Objects.requireNonNull(conn);
        List<String[]> result = new ArrayList<>();
        String[] o = null;
        try (PreparedStatement stat = conn.prepareStatement(sql)){
            for (int i = 1; i <= values.size(); i++) {
                stat.setString(i, values.get(i - 1));
            }
            try (ResultSet rs = stat.executeQuery()){
                ResultSetMetaData rm = rs.getMetaData();
                while (rs.next())
                {
                    o = new String[rm.getColumnCount()];
                    for (int i = 0; i < rm.getColumnCount(); i++)
                    {
                        if (rs.getString(i + 1) == null)
                        {
                            o[i] = "null";
                        }
                        else
                        {
                            o[i] = rs.getString(i + 1);
                        }
                    }
                    result.add(o);
                }
            }

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOG.error("回滚失败", e1);
            }
            logSqlException(sql, e);
            throw e;
        }
        return result;
    }

    public static int execute(String sql, Connection conn)
            throws SQLException {

        Objects.requireNonNull(conn);
        try (Statement stat = conn.createStatement()){
            LOG.debug(sql);
            return stat.executeUpdate(sql);
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOG.error("回滚失败", e1);
            }
            logSqlException(sql, e);
            throw e;
        }
    }

    public static List<String[]> executeQuery(String sql, Connection conn)
            throws SQLException {

        Objects.requireNonNull(conn);
        List<String[]> result = new ArrayList<>();
        String[] o = null;
        LOG.debug(sql);
        try (Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(sql)){

            ResultSetMetaData rm = rs.getMetaData();
            while (rs.next())
            {
                o = new String[rm.getColumnCount()];
                for (int i = 0; i < rm.getColumnCount(); i++)
                {
                    if (rs.getString(i + 1) == null)
                    {
                        o[i] = "null";
                    }
                    else
                    {
                        o[i] = rs.getString(i + 1);
                    }
                }
                result.add(o);
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOG.error("回滚失败", e1);
            }
            logSqlException(sql, e);
            throw e;
        }
        return result;
    }

}
