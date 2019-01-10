package com.mastercom.bigdata.tools.adapter;

import com.mastercom.bigdata.exception.SqlException;
import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.tools.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Kwong on 2019/1/8.
 */
public class ResultSetToModelAdapter<M extends IModel> extends ResultSetAdapter<M> {

    private static final Logger LOG = LoggerFactory.getLogger(ResultSetToModelAdapter.class);

    private Class<M> mClass;

    public ResultSetToModelAdapter(Class<M> mClass){
        this.mClass = mClass;
    }

    private String columnNameToFieldName(String columnName){
        return StringUtil.parseUnderlineCase(columnName);
    }

    private static Method setter(Class<?> clazz, String fieldName){
        try {
            Field field = clazz.getDeclaredField(fieldName);
            Class<?> fieldType = field.getType();
            char firstChar = fieldName.charAt(0);
            String getterMethodName = fieldName.replaceFirst(String.valueOf(firstChar), "set"+ Character.toUpperCase(firstChar));

            return clazz.getDeclaredMethod(getterMethodName, fieldType);
        } catch (Exception e) {
            LOG.error("setter获取失败", e);
            return null;
        }
    }

    private static Object columnValueToFieldValue(String columnTypeName, Object columnValue){
        switch (columnTypeName){
            case "VARCHAR":
                return String.valueOf(columnValue);
            case "INTEGER":
                return columnValue;
            default:
                return columnValue;
        }
    }

    @Override
    protected M trans(List<ResultRowStruct> rowStructs) {
        M model = createInstance();
        for (ResultRowStruct rowStruct : rowStructs) {
            String fieldName = columnNameToFieldName(rowStruct.columnName);
            Method setter = setter(mClass, fieldName);
            if (setter == null){
                LOG.warn(String.format("在%s类中找不到%s的setter方法", mClass.getSimpleName(), fieldName));
                continue;
            }
            Object fieldValue = columnValueToFieldValue(rowStruct.columnTypeName, rowStruct.columnValue);
            try {
                setter.invoke(model, fieldValue);
            } catch (Exception e) {
                LOG.warn(String.format("在%s类中执行%s方法失败", mClass.getSimpleName(), setter.getName()));
                //ignore
            }
        }
        return model;
    }

    private M createInstance(){
        try {
            return mClass.newInstance();
        } catch (Exception e) {
            throw new SqlException(e);
        }
    }

}
