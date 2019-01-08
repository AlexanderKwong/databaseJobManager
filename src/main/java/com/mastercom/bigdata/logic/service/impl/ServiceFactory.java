package com.mastercom.bigdata.logic.service.impl;

import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.logic.service.IService;
import com.mastercom.bigdata.tools.ClassUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kwong on 2017/9/23.
 */
public final class ServiceFactory {

    private static Map<String, IService> cacheMap = new ConcurrentHashMap<>();

    public static <T extends IModel, E extends IService<T>> E getInstance(Class modelClass){
        return (E)getInstance(modelClass.getName());
    }

    public static <T extends IModel, E extends IService<T>> E getInstance(String modelClassName){
        Objects.requireNonNull(modelClassName);

        if (cacheMap.containsKey(modelClassName)){
            return (E)cacheMap.get(modelClassName);
        }

        try {
            List<Class> clazzList = ClassUtil.getChildrenClasses(AbstractService.class, ServiceFactory.class.getPackage().getName());
            Class c = null;
            for(Class clazz : clazzList){
                try{
                    c =  ClassUtil.getGeneralClass(clazz, 0);
                }catch (ClassCastException e){
                    continue;
                }
                if (modelClassName.equals(c.getName())){
                    E INSTANCE = (E)clazz.newInstance();
                    cacheMap.put(modelClassName, INSTANCE);
                    return INSTANCE;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
