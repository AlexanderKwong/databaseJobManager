package com.mastercom.bigdata.UI.panel;
import com.mastercom.bigdata.UI.Constants;
import com.mastercom.bigdata.logic.controller.ModelWrapper;
import com.mastercom.bigdata.tools.StringUtil;
import com.mastercom.bigdata.tools.sql.DBType;
import com.mastercom.bigdata.model.impl.Job;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.mastercom.bigdata.logic.Constants.*;

/**
 * Created by Kwong on 2017/9/22.
 */
public class ItemPanel extends AbstractViewPanel<Job>{
    private JButton jb1;
    private JButton jb2;
    private JComboBox jcb1;
    private JPanel jp1;
    private JPanel jp2;
    private JPanel jp3;
    private JPanel jp4;
    private JPanel jp5;
    private JPanel jp6;
    private JLabel jl1;
    private JLabel jl2;
    private JLabel jl3;
    private JLabel jl4;
    private JLabel jl5;
    private JLabel jl6;
    private JLabel jl7;
    private JLabel jl8;
    private JTextField jtf1;
    private JTextField jtf2;
    private JTextField jtf3;
    private JTextField jtf4;
    private JTextArea jta1;
    private JTextArea jta2;
    private JSeparator js1;
    private JRadioButton jrb1;
    private JRadioButton jrb2;
    private JRadioButton jrb3;
    private ButtonGroup bg;

    JSpinner spinner = new JSpinner();
    JSpinner spinner_1 = new JSpinner(new SpinnerNumberModel(1, 1, 7, 1));
    JSpinner spinner_2 = new JSpinner();
    JSpinner spinner_3 = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
    JSpinner spinner_4 = new JSpinner();
    SpinnerModel dateModel = null;

    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    String format = "HH:mm:ss";
    DateFormat timeFormat = new SimpleDateFormat(this.format);

    private String frequencyType = "";

    public ItemPanel(){
        init();
    }

    @Override
    public void update(List<Job> data) {
        if (data != null && data.size()==1){
            this.data = data;
            fillData();
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void fillData() {
        if (data != null && data.size()==1){
            Job job = data.get(0);
            if (DBType.GreenPlum.getName().equals(job.getDatabaseType()))
            {
                this.jcb1.setSelectedIndex(0);
            } else if (DBType.GreenPlum.getName().equals(job.getDatabaseType()))
            {
                this.jcb1.setSelectedIndex(1);
            }
            this.jtf1.setText(job.getUrl());
            this.jtf2.setText(job.getUsername());
            this.jtf3.setText(job.getPassword());
            this.jtf4.setText(job.getJobName());
            if (null != job.getJobName()){
                this.jtf4.setEditable(false);
            }else {
                this.jtf4.setEditable(true);
            }
            this.jta1.setText(job.getOrderContent());

            if (JOB_FREQUENCY_DAY.equals(job.getPlanFrequency()))
            {
                this.jrb1.setSelected(true);
                this.jp6.removeAll();
                addDailyPanel(this.jp6, job.getDailyTime());
                this.jb2.updateUI();
                this.frequencyType = JOB_FREQUENCY_DAY;
            } else if (JOB_FREQUENCY_WEEK.equals(job.getPlanFrequency()))
            {
                this.jrb2.setSelected(true);
                this.jp6.removeAll();
                addWeekPanel(this.jp6, job.getDailyTime(), job.getDay());
                this.jp2.updateUI();
                this.frequencyType = JOB_FREQUENCY_WEEK;
            } else if (JOB_FREQUENCY_MONTH.equals(job.getPlanFrequency()))
            {
                this.jrb3.setSelected(true);
                this.jp6.removeAll();
                addMonthPanel(this.jp6, job.getDailyTime(), job.getDay());
                this.jp2.updateUI();
                this.frequencyType = JOB_FREQUENCY_MONTH;
            } else {
                this.bg.clearSelection();
                this.jp6.removeAll();
                this.jp2.updateUI();
                this.frequencyType = "";
            }

            this.jta2.setText(job.getDeclaration());
        }
    }

    @Override
    public void addComponents() {
// 初始化组件
        jl1 = new JLabel("数据库类型：");
        jcb1 = new JComboBox(Constants.ITEMFRAME_ITEM_COMBOBOX);
        jp1 = new JPanel();
        jl2 = new JLabel("数据库URL：");
        jtf1 = new JTextField();
        jl3 = new JLabel("用户名：");
        jtf2 = new JTextField();
        jl4 = new JLabel("密码：");
        jtf3 = new JTextField();

        js1 = new JSeparator();

        jl5 = new JLabel("作业名称：");
        jtf4 = new JTextField();
        jl6 = new JLabel("作业命令：");
        jta1 = new JTextArea();
        jta1.setLineWrap(true);
        jta1.setWrapStyleWord(true);
        jl7 = new JLabel("作业频率：");

        jp2 = new JPanel();
        bg = new ButtonGroup();
        jrb1 = new JRadioButton("每天");
        jrb2 = new JRadioButton("每周");
        jrb3 = new JRadioButton("每月");
        jp6 = new JPanel();

        jl8 = new JLabel("作业说明");
        jta2 = new JTextArea();
        jta2.setLineWrap(true);
        jta2.setWrapStyleWord(true);

        jp3 = new JPanel();
        jb1 = new JButton("启用");
        jp4 = new JPanel();
        jb2 = new JButton("取消");
        jp5 = new JPanel();
        jp6 = new JPanel();

        this.add(jl1);
        this.add(jcb1);
        this.add(jp1);
        this.add(jl2);
        this.add(jtf1);
        this.add(jl3);
        this.add(jtf2);
        this.add(jl4);
        this.add(jtf3);
        this.add(js1);
        this.add(jl5);
        this.add(jtf4);
        this.add(jl6);
        this.add(jta1);
        this.add(jl7);
        this.add(jp2);
        this.bg.add(jrb1);
        this.bg.add(jrb2);
        this.bg.add(jrb3);
        this.jp2.add(jrb1);
        this.jp2.add(jrb2);
        this.jp2.add(jrb3);
        this.jp2.add(this.jp6);

        this.add(jl8);
        this.add(jta2);
        this.add(jp3);
        this.add(jb1);
        this.add(jp4);
        this.add(jb2);
        this.add(jp5);
    }

    @Override
    public void setLayouts() {
// 设置布局
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagLayout layoutOfJP2 = new GridBagLayout();
        this.jp2.setLayout(layoutOfJP2);

        GridBagConstraints s = new GridBagConstraints();// 定义一个GridBagConstraints，
        // 是用来控制添加进的组件的显示位置
        s.fill = GridBagConstraints.BOTH;
        // s.ipadx = 50;
        // s.ipady = 10;
        s.insets = Constants.COMPONENT_LAYOUT_INSETS;

        s.gridwidth = 1;// 该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;// 该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        s.weighty = 0;// 该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        layout.setConstraints(jl1, s);// 设置组件
        s.gridwidth = 2;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jcb1, s);
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(jp1, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jl2, s);
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(jtf1, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jl3, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jtf2, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jl4, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jtf3, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(js1, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jl5, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jtf4, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jl6, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0.5;
        layout.setConstraints(jta1, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jl7, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jp2, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layoutOfJP2.setConstraints(jrb1, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layoutOfJP2.setConstraints(jrb2, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layoutOfJP2.setConstraints(jrb3, s);
        s.gridwidth = 4;
        s.weightx = 0;
        s.weighty = 0;
        layoutOfJP2.setConstraints(jp6, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jl8, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0.5;
        layout.setConstraints(jta2, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jp3, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jb1, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jp4, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jb2, s);
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(jp5, s);
    }

    @Override
    public void setListeners() {
        // 下拉数据库类型选择框
        this.jcb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ItemPanel.this.jcb1.getSelectedItem().toString().equals(DBType.SqlServer.getName())) {
                    ItemPanel.this.jtf1.setText("jdbc:sqlserver://数据库IP:端口号;DatabaseName=数据库名称");
                } else if (ItemPanel.this.jcb1.getSelectedItem().toString().equals(DBType.GreenPlum.getName())) {
                    ItemPanel.this.jtf1.setText("jdbc:postgresql://数据库IP:端口号/数据库名称");
                }
            }
        });
        // 每天单选框
        this.jrb1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ItemPanel.this.jp6.removeAll();
                addDailyPanel(ItemPanel.this.jp6);
                ItemPanel.this.jp2.updateUI();
                ItemPanel.this.frequencyType = JOB_FREQUENCY_DAY;
            }
        });
        //每周单选框
        this.jrb2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ItemPanel.this.jp6.removeAll();
                addWeekPanel(ItemPanel.this.jp6);
                ItemPanel.this.jp2.updateUI();
                ItemPanel.this.frequencyType = JOB_FREQUENCY_WEEK;
            }
        });
        //每月单选框
        this.jrb3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ItemPanel.this.jp6.removeAll();
                addMonthPanel(ItemPanel.this.jp6);
                ItemPanel.this.jp2.updateUI();
                ItemPanel.this.frequencyType = JOB_FREQUENCY_MONTH;
            }
        });
        //启用
        jb1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                StringBuffer message = new StringBuffer();
                String choiceDatabase = ItemPanel.this.jcb1.getSelectedItem().toString();
                String urlContent = ItemPanel.this.jtf1.getText();
                if (StringUtil.isBlank(urlContent))
                {
                    message.append("【数据库url为必填项】");
                }
                String username = ItemPanel.this.jtf2.getText();
                if (StringUtil.isBlank(username))
                {
                    message.append("【数据库用户名为必填项】");
                }
                String password = ItemPanel.this.jtf3.getText();
                if (StringUtil.isBlank(password))
                {
                    message.append("【数据库密码为必填项】");
                }
                String jobname = ItemPanel.this.jtf4.getText();
                if (StringUtil.isBlank(jobname))
                {
                    message.append("【作业名称为必填项】");
                }
                String ordercontent = ItemPanel.this.jta1.getText();
                if (StringUtil.isBlank(ordercontent))
                {
                    message.append("【作业命令内容为必填项】");
                }
                String time = null;
                String day = "";
                if (ItemPanel.this.frequencyType.equals(JOB_FREQUENCY_DAY))
                {
                    Date date = (Date) ItemPanel.this.spinner.getValue();
                    time = ItemPanel.this.formatter.format(date);
                } else if (ItemPanel.this.frequencyType.equals(JOB_FREQUENCY_WEEK))
                {
                    day = ItemPanel.this.spinner_1.getValue().toString();
                    Date date = (Date) ItemPanel.this.spinner_2.getValue();
                    time = ItemPanel.this.formatter.format(date);
                } else if (ItemPanel.this.frequencyType.equals(JOB_FREQUENCY_MONTH))
                {
                    day = ItemPanel.this.spinner_3.getValue().toString();
                    Date date = (Date) ItemPanel.this.spinner_4.getValue();
                    time = ItemPanel.this.formatter.format(date);
                }
                if ((ItemPanel.this.frequencyType == null) || ("".equals(ItemPanel.this.frequencyType)))
                {
                    message.append("【作业执行频率为必填项】");
                }
                String discribe = ItemPanel.this.jta2.getText();
                if (message.toString().equals("")) {
                    Job job = new Job.Builder()
                            .withJobName(jobname)
                            .withDatabaseType(choiceDatabase)
                            .withUrl(urlContent)
                            .withUsername(username)
                            .withPassword(password)
                            .withOrderContent(ordercontent)
                            .withCreateTime(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()))
                            .withDay(day)
                            .withDailyTime(time)
                            .withDeclaration(discribe)
                            .withPlanFrequency(frequencyType)
                            .withStates(JOB_STATE_FREE)
                            .withStatus(JOB_STATUS_ENABLE)
                            .build();
                    ModelWrapper<Job> response = null;
                    if (data != null && !data.isEmpty()) {//modify
                        Job old = data.get(0);
                        if (null != old.getJobName() && !old.getJobName().equals(jobname)) {
                            JOptionPane.showMessageDialog(null, "JobName不能被修改");
                        }
                        job.setId(old.getId());
                        response = ItemPanel.this.controller.put(job);
                    } else {//new
                        response = ItemPanel.this.controller.put(job);
                    }

                    if (response.getReturnCode() == ModelWrapper.SUCCESS){
                        //hide() ? || dispose() ?
                        //frame/rootPane/layeredPane/contentPane
                        ItemPanel.this.getParent().getParent().getParent().setVisible(false);
                    }else {
                        JOptionPane.showMessageDialog(null, response.getMsg());
                    }

                } else
                {
                    JOptionPane.showMessageDialog(null, message.toString());
                }
            }
        });
        jb2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
//                ItemPanel.this.frame.dispose();
                ItemPanel.this.getParent().getParent().getParent().setVisible(false);
            }
        });
    }

    private void addDailyPanel(JPanel panel){
        this.addDailyPanel(panel, null);
    }

    /**
     * 为面板添加 “日”组件
     * @param panel
     * @param dailyTime
     */
    private void addDailyPanel(JPanel panel, String dailyTime){
        panel.setLayout(new GridLayout());

        JLabel lblNewLabel_3 = new JLabel("每天执行时间：");
        panel.add(lblNewLabel_3);
        try {
            if(dailyTime == null){
                dailyTime = "00:00:00";
            }
            ItemPanel.this.dateModel = new SpinnerDateModel(
                    ItemPanel.this.timeFormat.parse(dailyTime), null,
                    null, 12);
        } catch (ParseException e1) {
            e1.printStackTrace();
//            SystemoutLog.printLog(e1.getMessage());
        }
        ItemPanel.this.spinner.setModel(ItemPanel.this.dateModel);
        ItemPanel.this.spinner.setEditor(new JSpinner.DateEditor(
                ItemPanel.this.spinner, ItemPanel.this.format));

        panel.add(ItemPanel.this.spinner);
    }

    private void addWeekPanel(JPanel panel){
        this.addWeekPanel(panel, null, null);
    }

    /**
     * 为面板添加 “周”组件
     * @param panel
     * @param dailyTime
     * @param day
     */
    private void addWeekPanel(JPanel panel, String dailyTime, String day){
        panel.setLayout(new GridLayout());
        JLabel lblNewLabel_4 = new JLabel("每周：");
        panel.add(lblNewLabel_4);
        if(day != null){
            this.spinner_1.setValue(Integer.valueOf(Integer.parseInt(day)));
        }
        panel.add(ItemPanel.this.spinner_1);

        JLabel lblNewLabel_5 = new JLabel("日");
        panel.add(lblNewLabel_5);
        try {
            if(dailyTime == null){
                dailyTime = "00:00:00";
            }
            ItemPanel.this.dateModel = new SpinnerDateModel(
                    ItemPanel.this.timeFormat.parse(dailyTime), null,
                    null, 12);
        } catch (ParseException e1) {
            e1.printStackTrace();
//            SystemoutLog.printLog(e1.getMessage());
        }
        ItemPanel.this.spinner_2.setModel(ItemPanel.this.dateModel);
        ItemPanel.this.spinner_2.setEditor(new JSpinner.DateEditor(
                ItemPanel.this.spinner_2, ItemPanel.this.format));
        panel.add(ItemPanel.this.spinner_2);

        JLabel lblNewLabel_6 = new JLabel("具体时间");
        panel.add(lblNewLabel_6);
    }

    private void addMonthPanel(JPanel panel){
        this.addMonthPanel(panel, null, null);
    }

    /**
     * 为面板添加 “月”组件
     * @param panel
     * @param dailyTime
     * @param day
     */
    private void addMonthPanel(JPanel panel, String dailyTime, String day){

        panel.setLayout(new GridLayout());
        JLabel lblNewLabel_7 = new JLabel("每月：");
        panel.add(lblNewLabel_7);
        if(day != null){
            this.spinner_3.setValue(Integer.valueOf(Integer.parseInt(day)));
        }
        panel.add(ItemPanel.this.spinner_3);

        JLabel lblNewLabel_8 = new JLabel("日");
        panel.add(lblNewLabel_8);
        try {
            if(dailyTime == null){
                dailyTime = "00:00:00";
            }
            ItemPanel.this.dateModel = new SpinnerDateModel(
                    ItemPanel.this.timeFormat.parse(dailyTime), null,
                    null, 12);
        } catch (ParseException e1) {
            e1.printStackTrace();
//            SystemoutLog.printLog(e1.getMessage());

        }
        ItemPanel.this.spinner_4.setModel(ItemPanel.this.dateModel);
        ItemPanel.this.spinner_4.setEditor(new JSpinner.DateEditor(
                ItemPanel.this.spinner_4, ItemPanel.this.format));
        panel.add(ItemPanel.this.spinner_4);

        JLabel lblNewLabel_9 = new JLabel("具体时间");
        panel.add(lblNewLabel_9);
    }

    public void cleanup(){
       update(Collections.singletonList(new Job()));
       this.data = null;
    }
}
