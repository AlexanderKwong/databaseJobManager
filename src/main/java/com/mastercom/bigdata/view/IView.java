package com.mastercom.bigdata.view;

import java.util.List;

/**
 * Created by Kwong on 2017/9/22.
 */
public interface IView<T> {

    void update(List<T> data);

    void update();
}
