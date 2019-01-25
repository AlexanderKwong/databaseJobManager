package com.mastercom.bigdata.view.panel;

import com.mastercom.bigdata.view.Constants;
import com.mastercom.bigdata.logic.controller.impl.AbstractController;
import com.mastercom.bigdata.model.impl.Job;
import com.mastercom.bigdata.common.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Kwong on 2017/9/23.
 */
public class LogPanel extends AbstractViewPanel<Job> {

    private static Logger LOG = LoggerFactory.getLogger(LogPanel.class);

    public JTextArea textArea = null;
    private JLabel jl1 = null;
    private JScrollPane jsp = null;
    private JPanel jp3 = null;

    public LogPanel(){
        super();
        ((AbstractController)controller).register(this);
        init();
    }

    @Override
    public void update(List<Job> data) {

    }

    @Override
    public void update() {

    }

    @Override
    public void fillData() {

    }

    @Override
    public void addComponents() {
//初始化
        textArea = new JTextArea();
        jl1 = new JLabel(Constants.MAINFRAME_LOG_LABLE_TITLE);
        jp3 = new JPanel();

        this.add(jl1);
        this.add(jp3);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        jsp = new JScrollPane(textArea);
        this.add(jsp);
//        LOG.info("LOG 面板初始化完成");
    }

    @Override
    public void setLayouts() {
// 设置布局
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.insets = new Insets(5, 10, 5, 10);

        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jl1, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jp3, s);
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 1;
        layout.setConstraints(jsp, s);
    }

    @Override
    public void setListeners() {

    }

    public void output(String msg){
        if (this.textArea != null){

            this.textArea.append("[" + TimeUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "] " + msg);
            this.textArea.paintImmediately(this.textArea.getBounds());
        }else {
            LOG.info("LOG_TEXT_AREA未初始化，待输出内容为："+ msg);
        }
    }
}
