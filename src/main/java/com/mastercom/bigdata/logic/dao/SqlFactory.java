package com.mastercom.bigdata.logic.dao;

import com.mastercom.bigdata.exception.SqlException;
import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.tools.StringUtil;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kwong on 2019/1/8.
 */
public class SqlFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SqlFactory.class);

    private SqlFactory(){}
    
    private static String fieldNameToColumnName(String fieldName){
        return StringUtil.parseCamelCase(fieldName);
    }

    public static String[] columnNames(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fieldNameToColumnName(fields[i].getName());
        }
        return fieldNames;
    }

    public static String tableName(Class<?> clazz){
        return fieldNameToColumnName(clazz.getSimpleName());
    }

    private static String[] fieldNames(final Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    private static String wrappedFieldName(String fieldName){
        return String.format("#{%s}", fieldName);
    }

    private static Method getter(Class<?> clazz, String fieldName){
        char firstChar = fieldName.charAt(0);
        String getterMethodName = fieldName.replaceFirst(String.valueOf(firstChar), "get"+ Character.toUpperCase(firstChar));
        try {
            return clazz.getDeclaredMethod(getterMethodName);
        } catch (NoSuchMethodException e) {
            LOG.error("找不到getter方法", e);
            return null;
        }
    }

    private static boolean hasValue(Method getter, Object object){
        Object result = null;
        try {
            result = getter.invoke(object);
        } catch (IllegalAccessException e) {
            LOG.error("getter没有访问权限", e);
        } catch (InvocationTargetException e) {
            LOG.error("getter调用失败", e);
        }
        return result != null;
    }

    public static String getCreateSql(final Class<? extends IModel> clazz){
        StringBuilder sb = new StringBuilder("CREATE TABLE ")
                .append(tableName(clazz))
                .append("(");
        boolean isFirst = true;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String columnName = fieldNameToColumnName(fieldName);
            String columnType = columnType(field);
            if (!isFirst){
                sb.append(",");
            }
            sb.append(columnName).append(" ").append(columnType);
            if ("id".equalsIgnoreCase(fieldName)){
                sb.append(" not null generated always as identity (start with 1, increment by 1)");
            }
            isFirst = false;
        }
        sb.append(")");
        return sb.toString();
    }

    public static String getInsertSql(final Class<? extends IModel> clazz){
        SQL sql = new SQL().INSERT_INTO(tableName(clazz));
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if ("id".equalsIgnoreCase(fieldName)){
                continue;
            }
            String columnName = fieldNameToColumnName(fieldName);
            sql.VALUES(columnName, wrappedFieldName(fieldName));
        }
        return sql.toString();
    }

    public static String getUpdateByIdSql(final Class<? extends IModel> clazz){
        SQL sql = new SQL().UPDATE(tableName(clazz));
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if ("id".equalsIgnoreCase(fieldName)){
                continue;
            }
            String columnName = fieldNameToColumnName(fieldName);
            sql.SET(columnName + "=" + wrappedFieldName(fieldName));
        }
        sql.WHERE("ID = #{id}");
        return sql.toString();
    }

    @SuppressWarnings("all")
    public static String getDeleteSql(final Class<? extends IModel> clazz){
        return new SQL() {{
            DELETE_FROM(tableName(clazz));
            WHERE("ID = #{id}");
        }}.toString();
    }

    public static String getQueryByIdSql(final Class<? extends IModel> clazz){
        return new SQL().SELECT(columnNames(clazz)).FROM(tableName(clazz)).WHERE("ID = #{id}").toString();
    }

    @SuppressWarnings("all")
    public static <M extends IModel> String getQuerySql(final M model){
        final Class<?> clazz = model.getClass();
        return new SQL() {{
            SELECT(columnNames(clazz));
            FROM(tableName(clazz));

            for (String fieldName : fieldNames(clazz)){
                Method getter = getter(clazz, fieldName);
                if (hasValue(getter, model)){
                    WHERE(fieldNameToColumnName(fieldName) + " = " + wrappedFieldName(fieldName));
                }
            }
        }}.toString();
    }

    public static List<String> getParameterValues(String sql, final Object object){
        final List<String> result = new ArrayList<>();
        final Class<?> clazz = object.getClass();

        StringUtil.match(sql, "#\\{[A-Za-z0-9_$]+\\}", new StringUtil.StrMatchCallBack() {
            @Override
            public String handle(String oriStr, String matchWord) {
                String fieldName = matchWord.substring(2, matchWord.length()-1);
                Method getter = getter(clazz, fieldName);
                try {
                    if (getter != null){
                        Object value = getter.invoke(object);
                        if (value != null){
                            result.add(value.toString());
                        }else result.add(null);
                    }
                } catch (Exception e) {
                    throw new SqlException(e);
                }
                return oriStr;
            }
        });

        return result;
    }

    public static String replaceSqlParamsWithQuestionMark(String sql){
        return StringUtil.match(sql, "#\\{[A-Za-z0-9_$]+\\}", new StringUtil.StrMatchCallBack() {
            @Override
            public String handle(String oriStr, String matchWord) {

                return oriStr.replace(matchWord, "?");
            }
        });
    }

    private static String columnType(Field field){
        String typeName = field.getType().getName();
        switch (typeName){
            case "java.lang.String":
                return "varchar(1000)";
            case "java.lang.Integer":
            case "int":
                return "integer";
            case "java.lang.Long":
            case "long":
                return "integer";
            case "java.lang.Character":
            case "char":
            case "java.lang.Boolean":
            case "boolean":
            default:
                return "varchar(100)";
        }
    }
}
