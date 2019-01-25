package com.mastercom.bigdata.view;

import com.mastercom.bigdata.view.panel.LogPanel;
import com.mastercom.bigdata.view.panel.NavigationPanel;
import static com.mastercom.bigdata.view.Constants.*;
import javax.swing.*;

/**
 * Created by Kwong on 2017/9/22.
 */
public final class MainFrame extends JFrame {

    private static MainFrame instance;

    JSplitPane splitPane;

    NavigationPanel leftPanel;

    LogPanel rightPanel;

    private MainFrame(){
        super(MAINFRAME_TITLE);

        if (instance != null) {
            throw new IllegalAccessError();
        }
        init();
    }

    public static MainFrame getInstance(){
        if (instance == null){
            synchronized (MainFrame.class){
                if (instance == null){
                    instance = new MainFrame();
                    instance.rightPanel.output("程序开始运行，请进行相关操作");
                }
            }
        }
        return instance;
    }

    private void init(){

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        splitPane = new JSplitPane();

        rightPanel = new LogPanel();//日志面板应比导航面板更早初始化

        leftPanel = new NavigationPanel();

        splitPane.setOneTouchExpandable(true); // 让分割线显示出箭头

        splitPane.setContinuousLayout(true); // 当用户操作分割线箭头时，系统会重绘图形

        splitPane.setPreferredSize(MAINFRAME_SPLITPANE_DIMENSION);

        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); // 设置分割线为水平分割线

        splitPane.setLeftComponent(leftPanel);

        splitPane.setRightComponent(rightPanel);

        splitPane.setDividerSize(MAINFRAME_SPLITPANE_DIVIDER_SIZE);

        splitPane.setDividerLocation(MAINFRAME_SPLITPANE_DIVIDER_LOCATION);

        this.setLocation(MAINFRAME_LOCATION_X, MAINFRAME_LOCATION_Y);

        this.setContentPane(splitPane);

        this.pack();
    }

}
