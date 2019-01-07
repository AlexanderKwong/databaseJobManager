package com.mastercom.bigdata.logic.controller.impl;

import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.logic.controller.IController;
import com.mastercom.bigdata.tools.ClassUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kwong on 2017/9/23.
 */
public final class ControllerFactory {

    private static Map<String, IController> cacheMap = new ConcurrentHashMap<>();

    public static <T extends IModel, C extends IController<T>> C getInstance(Class modelClass){
        return (C)getInstance(modelClass.getName());
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
                try{
                    c =  ClassUtil.getGeneralClass(clazz, 0);
                }catch (ClassCastException e){
                    continue;
                }
                if (modelClassName.equals(c.getName())){
                    C INSTANCE = (C)clazz.newInstance();
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
