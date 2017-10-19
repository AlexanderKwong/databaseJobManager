package com.mastercom.bigdata.UI;

import com.mastercom.bigdata.UI.panel.ItemPanel;
import com.mastercom.bigdata.bean.impl.Job;

import javax.swing.*;
import java.util.Collections;

/**
 * Created by Kwong on 2017/9/23.
 */
public class ItemFrame extends JFrame {

    private static ItemFrame instance;

    private ItemPanel itemPanel;

    private ItemFrame(){
        super("作业");
        if (instance != null)
            throw new IllegalAccessError();

       init();
    }

    public static ItemFrame getInstance(){
        if (instance == null){
            instance = new ItemFrame();
        }
        return instance;
    }

    private void init(){
        this.setAlwaysOnTop(true);
        this.setBounds(100, 40, 686, 690);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        itemPanel = new ItemPanel();
        this.setContentPane(itemPanel);
    }

    public void cleanup(){
        itemPanel.cleanup();
    }

    public void fillData(Job model){
        itemPanel.update(Collections.singletonList(model));
    }
}
