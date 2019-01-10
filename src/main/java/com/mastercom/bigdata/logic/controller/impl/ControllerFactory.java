package com.mastercom.bigdata.logic.controller.impl;

import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.logic.controller.IController;
import com.mastercom.bigdata.tools.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kwong on 2017/9/23.
 */
public final class ControllerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerFactory.class);

    private ControllerFactory(){}

    private static Map<String, IController> cacheMap = new ConcurrentHashMap<>();

    public static <T extends IModel, C extends IController<T>> C getInstance(Class modelClass){
        return getInstance(modelClass.getName());
    }

    public static <T extends IModel, C extends IController<T>> C getInstance(String modelClassName){
        Objects.requireNonNull(modelClassName);

        if (cacheMap.containsKey(modelClassName)){
            return (C)cacheMap.get(modelClassName);
        }

        try {
            List<Class> clazzList = ClassUtil.getChildrenClasses(AbstractController.class, ControllerFactory.class.getPackage().getName());
            Class c = null;
            for(Class clazz : clazzList){
                c = getGenericClass(clazz);
                if (c == null){
                    continue;
                }

                if (modelClassName.equals(c.getName())){
                    C instance = (C)clazz.newInstance();
                    cacheMap.put(modelClassName, instance);
                    return instance;
                }
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    private static Class<?> getGenericClass(Class<?> clazz){
        try{
            return ClassUtil.getGeneralClass(clazz, 0);
        }catch (ClassCastException e){
            return null;
        }
    }
}
