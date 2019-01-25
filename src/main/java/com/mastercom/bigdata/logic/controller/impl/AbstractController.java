package com.mastercom.bigdata.logic.controller.impl;

import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.logic.controller.IController;
import com.mastercom.bigdata.logic.controller.ModelWrapper;
import com.mastercom.bigdata.logic.service.impl.ServiceFactory;
import com.mastercom.bigdata.view.IView;
import com.mastercom.bigdata.model.impl.Job;
import com.mastercom.bigdata.logic.service.IService;
import com.mastercom.bigdata.common.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kwong on 2017/9/22.
 */
public abstract class AbstractController<T extends IModel> implements IController<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

    protected Set<ViewRef<T>> views;

    protected IService<T> service;

    protected AbstractController(){
        views = new HashSet<>();
        service = ServiceFactory.getInstance(ClassUtil.getGeneralClass(getClass(), 0));
    }

    public void register(IView<T> view){
        register(view, false);
    }

    public void register(IView<T> view, boolean callbackOnDataChanged){
        views.add(new ViewRef<T>(view, callbackOnDataChanged));
    }

    public ModelWrapper<T> get(T model) {
       try {
           List<T> data = service.list(model);
           return new ModelWrapper<>(ModelWrapper.OPERA_QUERY, ModelWrapper.SUCCESS, data, "操作成功。");
       }catch(Exception e){
           LOG.error("Fail to get model", e);
           return new ModelWrapper<>(ModelWrapper.OPERA_QUERY, ModelWrapper.FAILED, null, "操作失败。\n\t"+e.getMessage());
       }
    }

    public ModelWrapper<T> put(T model) {
        int num = 0;
        int operation = ModelWrapper.OPERA_UNDEFINED;

        try{
            if(model.getId() != null /*&& service.findById(model.getId()) != null*/){
                operation = ModelWrapper.OPERA_MODIFY;
                num = service.update(model);

            }else{
                operation = ModelWrapper.OPERA_NEW;
                num = service.add(model);
            }

        }catch (Exception e){
            LOG.error("Fail to put model", e);
            return new ModelWrapper<>(operation, ModelWrapper.FAILED, null, "操作失败。\n\t"+e.getMessage());
        }
        if (num > 0){
            updateAllView();
        }
        return new ModelWrapper<>(operation, ModelWrapper.SUCCESS, Collections.singletonList(model), String.format("操作成功，受影响行数为：%d", num));
    }

    public ModelWrapper<T> delete(T model) {
        if (model.getId() == 0){
            return new ModelWrapper<>(ModelWrapper.OPERA_DELETE, ModelWrapper.FAILED, null, "操作失败，删除必须指定id");
        }
        int num = 0;
        try{
            num = service.remove(model);

        }catch (Exception e){
            LOG.error("Fail to delete model", e);
            return new ModelWrapper<>(ModelWrapper.OPERA_DELETE, ModelWrapper.FAILED, null, "操作失败。\n\t"+e.getMessage());
        }
        updateAllView();
        return new ModelWrapper<>(ModelWrapper.OPERA_DELETE, ModelWrapper.SUCCESS, Collections.singletonList(model), String.format("操作成功，受影响行数为：%d", num));
    }

    protected void updateAllView(){
        for (ViewRef viewRef : views){
            if (viewRef.callbackOnDataChanged){
                viewRef.view.update();
            }
        }
    }

    protected void updateAllView(List<Job> data){
        for (ViewRef viewRef : views){
            if (viewRef.callbackOnDataChanged){
                viewRef.view.update(data);
            }
        }
    }

    protected class ViewRef<T>{

        public final IView<T> view;

        public final boolean callbackOnDataChanged;

        public final Class viewClass;

        public ViewRef(IView<T> view, boolean callbackOnDataChanged){
            this.view = view;
            this.callbackOnDataChanged = callbackOnDataChanged;
            this.viewClass = view.getClass();
        }
    }

    /**
     * 初始化
     */
    public void init(){

    }

    /**
     * 清理资源
     */
    public void clear(){

    }
}
