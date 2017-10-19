package com.mastercom.bigdata.tools.sql;

import com.mastercom.bigdata.tools.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DBUtil
 * 获取 greenPlum/SQLServer/MySQL/Oracle/Hive的连接
 *
 * Created by Kwong on 2017/9/22.
 */

public final class DBUtil {

    private static Logger LOG = LoggerFactory.getLogger(DBUtil.class);

    /**
     * @param dbDriver     驱动类
     * @param dbProperties 连接配置
     * @return
     *//*
    public static Connection getConnection(String dbDriver, String dbProperties) throws SQLException {

        Properties prop = getProp(dbProperties);

        Connection conn = null;

        try {
            Class.forName(dbDriver);

            conn = DriverManager.getConnection(prop.getProperty(Constants.DB_DEFAULT_URL), prop.getProperty(Constants.DB_DEFAULT_USERNAME),
                    prop.getProperty(Constants.DB_DEFAULT_PASSWORD));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }*/

    /**
     * @param dbDriver 驱动类
     * @param url      连接地址
     * @param user     用户名
     * @param password 密码
     * @return
     */
    public static Connection getConnection(String dbDriver, String url, String user, String password) throws SQLException, ClassNotFoundException {

        Connection conn;

        try {
            Class.forName(dbDriver);

            conn = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
        return conn;
    }

    /**
     * 获取 MySQL分库分表/Oracle/Hive/MySQL四种连接
     *
     * @param dbType 类型
     * @return
     */
    public static Connection getConnectionByType(DBType dbType, String url, String user, String password) throws SQLException, ClassNotFoundException {

        Connection conn = null;

        switch (dbType) {
            case GreenPlum:
                conn = DBUtil.getConnection(Constants.DB_GREENPLUM_DRIVER, url, user, password);
                break;
            case MySQL:
                conn = DBUtil.getConnection(Constants.DB_MYSQL_DRIVER, url, user, password);
                break;
            case SqlServer:
                conn = DBUtil.getConnection(Constants.DB_SQLSERVER_DRIVER, url, user, password);
                break;
            case Oracle:
                conn = DBUtil.getConnection(Constants.DB_ORACLE_DRIVER, url, user, password);
                break;
            case HiveServer:
                conn = DBUtil.getConnection(Constants.DB_HIVESERVER_DRIVER, url, user, password);
                break;
            case Spark:
                conn = DBUtil.getConnection(Constants.DB_SPARKSERVER_DRIVER, url, user, password);
                break;
            case Derby:
//                conn = getDerbySingleConn(url, user, password);
                try{
                    conn = DBUtil.getConnection(Constants.DB_DERBY_DRIVER, url, user, password);
                } catch (SQLException se) {
                    if ( se.getSQLState().equals("XJ015") ) {
                        System.gc();
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
    public static void close(Connection conn, Statement stat, ResultSet rs) throws SQLException {

        try {
            if (rs != null) {
                rs.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                //test
                if (conn != connection)
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
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
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(sql);
            for (int i = 1; i <= values.size(); i++) {
                stat.setString(i, values.get(i - 1));
            }
            return stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            LOG.error("执行操作：【" + sql + "】异常", e.fillInStackTrace());
            throw e;
        } /*finally {
            DBUtil.close(conn, stat, null);
        }*/
    }

    public static List<String[]> executeQuery(String sql, List<String> values,  Connection conn)
            throws SQLException {

        Objects.requireNonNull(conn);
        List<String[]> result = new ArrayList<>();
        PreparedStatement stat = null;
        ResultSet rs = null;
        String[] o = null;
        try {
            stat = conn.prepareStatement(sql);
            for (int i = 1; i <= values.size(); i++) {
                stat.setString(i, values.get(i - 1));
            }
            rs = stat.executeQuery();
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
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            LOG.error("执行操作：【" + sql + "】异常", e.fillInStackTrace());
            throw e;
        }/* finally {
            DBUtil.close(conn, stat, null);
        }*/
        return result;
    }

    public static int execute(String sql, Connection conn)
            throws SQLException {

        Objects.requireNonNull(conn);
        Statement stat = null;
        try {
            LOG.debug(sql);
            stat = conn.createStatement();
            return stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            LOG.error("执行操作：【" + sql + "】异常", e.fillInStackTrace());
            throw e;
        } /*finally {
            DBUtil.close(conn, stat, null);
        }*/
    }

    public static List<String[]> executeQuery(String sql, Connection conn)
            throws SQLException {

        Objects.requireNonNull(conn);
        List<String[]> result = new ArrayList<>();
        Statement stat = null;
        ResultSet rs = null;
        String[] o = null;
        try {
            LOG.debug(sql);
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
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
                    System.out.print(o[i]+"\t");
                }
                System.out.println();
                result.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            LOG.error("执行操作：【" + sql + "】异常", e.fillInStackTrace());
            throw e;
        }/* finally {
            DBUtil.close(conn, stat, null);
        }*/
        return result;
    }

    private static Connection connection;
    private static Connection getDerbySingleConn(String url, String user, String password){
        if (connection == null){
            synchronized (DBUtil.class){
                if (connection == null){
                    try {
                        connection = getConnection(Constants.DB_DERBY_DRIVER, url, user, password);
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
