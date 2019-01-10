package com.mastercom.bigdata.UI;

import com.mastercom.bigdata.tools.sql.DBType;

import java.awt.*;

/**
 * Created by Kwong on 2017/9/22.
 */
public final class Constants {

    public static final String[] JOB_TABLE_COLUMNS =  { "名称", "启用否", "状态" };

    public static final String MAINFRAME_TITLE = "导航页";

    public static final int MAINFRAME_LOCATION_X = 100;

    public static final int MAINFRAME_LOCATION_Y = 50;

    public static final Dimension MAINFRAME_SPLITPANE_DIMENSION = new Dimension(800, 600);

    public static final int MAINFRAME_SPLITPANE_DIVIDER_LOCATION = 400;

    public static final int MAINFRAME_SPLITPANE_DIVIDER_SIZE = 10;

    public static final Insets COMPONENT_LAYOUT_INSETS = new Insets(5, 10, 5, 10);

    public static final String MAINFRAME_NAVIGATION_BUTTON_NEW_TITLE = "新增";

    public static final String MAINFRAME_NAVIGATION_BUTTON_MODIFY_TITLE = "修改";

    public static final String MAINFRAME_NAVIGATION_BUTTON_DELETE_TITLE = "删除";

    public static final String MAINFRAME_NAVIGATION_BUTTON_STARTUP_TITLE = "启用";

    public static final String MAINFRAME_NAVIGATION_BUTTON_SHUTDOWN_TITLE = "禁用";

    public static final String MAINFRAME_LOG_LABLE_TITLE = "运行日志";


    public static final String[] ITEMFRAME_ITEM_COMBOBOX = {DBType.GREENPLUM.getName(), DBType.SQLSERVER.getName() };

    public static final String ITEMFRAME_TITLE = "作业";
}
