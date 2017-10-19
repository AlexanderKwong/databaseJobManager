package com.mastercom.bigdata.UI.panel;

import com.mastercom.bigdata.logic.controller.ModelWrapper;
import com.mastercom.bigdata.UI.Constants;
import com.mastercom.bigdata.UI.ItemFrame;
import com.mastercom.bigdata.bean.impl.Job;
import com.mastercom.bigdata.logic.controller.impl.AbstractController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import static com.mastercom.bigdata.UI.Constants.*;

/**
 * Created by Kwong on 2017/9/22.
 */
public class NavigationPanel extends AbstractViewPanel<Job>{

    // 左面板的控件
    private JTable table = null;
    private DefaultTableModel tableModel = null;
    static JScrollPane scrollPane = null;
    private JButton jb1 = null;
    private JButton jb2 = null;
    private JButton jb3 = null;
    private JButton jb4 = null;
    private JButton jb5 = null;
    private JPanel jp4 = null;
    private JPanel jp5 = null;
    private JPanel jp6 = null;
    private JPanel jp7 = null;
    private JPanel jp8 = null;
    private JPanel jp9 = null;
    private JPanel jp10 = null;
    private JPanel jp11 = null;

    public NavigationPanel(){
        super();
        ((AbstractController)controller).register(this, true);
        data = controller.get(new Job()).getData();
        init();
        ((AbstractController)controller).init();//启动所有启用状态的任务
    }

    public void fillData() {

        Object[][] tableData = transListToArray(data);

        tableModel = new DefaultTableModel();
        tableModel.setDataVector(tableData, Constants.JOB_TABLE_COLUMNS);
        table = new JTable(){//单元格不允许直接编辑
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table.setModel(tableModel);
        scrollPane = new JScrollPane(table);
    }

    public void addComponents() {
//初始化
        jb1 = new JButton(MAINFRAME_NAVIGATION_BUTTON_NEW_TITLE);
        jb2 = new JButton(MAINFRAME_NAVIGATION_BUTTON_DELETE_TITLE);
        jb3 = new JButton(MAINFRAME_NAVIGATION_BUTTON_MODIFY_TITLE);
        jb4 = new JButton(MAINFRAME_NAVIGATION_BUTTON_SHUTDOWN_TITLE);
        jb5 = new JButton(MAINFRAME_NAVIGATION_BUTTON_STARTUP_TITLE);
        jp4 = new JPanel();
        jp5 = new JPanel();
        jp6 = new JPanel();
        jp7 = new JPanel();
        jp8 = new JPanel();
        jp9 = new JPanel();
        jp10 = new JPanel();
        jp11 = new JPanel();
        //添加
        this.add(jb5);
        this.add(jp4);
        this.add(jp5);
        this.add(jp6);
        this.add(jb4);
        jp11.add(scrollPane);
        this.add(jp11);
        this.add(jb1);
        this.add(jp7);
        this.add(jp8);
        this.add(jb3);
        this.add(jp9);
        this.add(jp10);
        this.add(jb2);
    }

    public void setLayouts() {
        setTableLayout();

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.insets = COMPONENT_LAYOUT_INSETS;
        s.gridwidth = 2;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jb5, s);
        s.gridwidth = 1;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(jp4, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jp5, s);
        s.gridwidth = 1;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(jp6, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jb4, s);
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 1;
        layout.setConstraints(jp11, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jb1, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jp7, s);
        s.gridwidth = 1;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(jp8, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jb3, s);
        s.gridwidth = 1;
        s.weightx = 1;
        s.weighty = 0;
        layout.setConstraints(jp9, s);
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jp10, s);
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(jb2, s);
    }

    public void setListeners() {
        // 新增按钮
        jb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ItemFrame.getInstance().isVisible()){
                    return;
                }
                ItemFrame.getInstance().cleanup();
                ItemFrame.getInstance().setVisible(true);
            }
        });
        // 删除按钮
        jb2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int row = NavigationPanel.this.table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(null, "请选择一条待删除记录！");
                } else {
                    Job job = data.get(row);
                    if (job.getStatus() == 1 && "运行态".equals(job.getStates())){
                        JOptionPane.showMessageDialog(null, NavigationPanel.this.table.getValueAt(row, 0) + "正在运行态，请等job运行完成之后再进行删除");
                    } else {
                        NavigationPanel.this.controller.delete(job);
                    }
                }
            }
        });
        // 修改按钮
        jb3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (ItemFrame.getInstance().isVisible()){
                    return;
                }
                int row = NavigationPanel.this.table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(null, "请选择一条记录！");
                } else {
                    Job job = data.get(row);
                    if (job.getStatus() == 1 && "运行态".equals(job.getStates())) {
                        JOptionPane.showMessageDialog(null, NavigationPanel.this.table.getValueAt(row, 0) + "正在运行态，请先等job运行结束再进行修改");
                    } else {

                        ItemFrame.getInstance().fillData(job);
                        ItemFrame.getInstance().setVisible(true);
                    }
                }

            }
        });
        // 禁用按钮
        jb4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = NavigationPanel.this.table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(null, "请选择一条记录！");
                } else {

                    Job job = data.get(row);
                    if (job.getStatus() == 1){
                        Job request = new Job(job);
                        request.setStatus(0);
                        ModelWrapper<Job> response = NavigationPanel.this.controller.put(request);
                        if (response.getReturnCode() == ModelWrapper.FAILED){
                            JOptionPane.showMessageDialog(null, response.getMsg());
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "该任务已经禁用！请勿重复操作");
                    }
                }
            }
        });
        // 启用按钮
        jb5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = NavigationPanel.this.table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(null, "请选择一条记录！");
                } else {
                    Job job = data.get(row);
                    if  (job.getStatus() == 0){
                        Job request = new Job(job);
                        request.setStatus(1);
                        ModelWrapper<Job> response = NavigationPanel.this.controller.put(request);
                        if (response.getReturnCode() == ModelWrapper.FAILED){
                            JOptionPane.showMessageDialog(null, response.getMsg());
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "该任务已经启动！请勿重复操作");
                    }
                }
            }
        });
    }

    private void setTableLayout(){
        GridBagLayout layoutOFJp11 = new GridBagLayout();
        jp11.setLayout(layoutOFJp11);

        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.insets = COMPONENT_LAYOUT_INSETS;

        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 1;
        layoutOFJp11.setConstraints(scrollPane, s);
    }

    private Object[][] transListToArray(List<Job> data){
        if (data != null && !data.isEmpty()){
            Object[][] result = new Object[data.size()][3];
            for (int i = 0; i < data.size(); i++){
                Job job = data.get(i);
                result[i] = new Object[]{job.getJobName(), job.getStatus() > 0 ? "启用":"禁用", job.getStates()};
            }
            return result;
        }
        return null;
    }

    public void update(List<Job> data) {
        this.data = data;
        jp11.remove(scrollPane);
        fillData();
        jp11.add(scrollPane);
        setTableLayout();
        this.updateUI();
    }

    public void update(){
        //默认更新所有
        ModelWrapper<Job> all = controller.get(new Job());

        if (all.getReturnCode() == ModelWrapper.SUCCESS){
            update(all.getData());
        }
    }
}
