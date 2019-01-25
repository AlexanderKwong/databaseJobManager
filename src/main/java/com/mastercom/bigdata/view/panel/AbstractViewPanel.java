package com.mastercom.bigdata.view.panel;

import com.mastercom.bigdata.view.IView;
import com.mastercom.bigdata.model.IModel;
import com.mastercom.bigdata.logic.controller.IController;
import com.mastercom.bigdata.logic.controller.impl.ControllerFactory;
import com.mastercom.bigdata.common.util.ClassUtil;

import javax.swing.*;
import java.util.List;

/**
 * Created by Kwong on 2017/9/22.
 */
public abstract class AbstractViewPanel<T extends IModel> extends JPanel implements IView<T> {

    protected IController<T> controller;

    protected List<T> data;

    public AbstractViewPanel(){
        this.controller =  (IController<T>)ControllerFactory.getInstance(ClassUtil.getGeneralClass(getClass(), 0));
    }

    public abstract void fillData();

    public abstract void addComponents();

    public abstract void setLayouts();

    public abstract void setListeners();


    public void init(){
        fillData();
        addComponents();
        setLayouts();
        setListeners();
    }
}
