package com.mastercom.bigdata.logic;

import com.mastercom.bigdata.common.sql.DBType;

/**
 * Created by Kwong on 2017/9/25.
 */
public final class Constants {

    public static final DBType DEFAULT_DATA_SOURCE = DBType.DERBY;

    public static final String DEFAULT_DB_URL = "jdbc:derby:test;create=true";

    public static final String DEFAULT_DB_USERBANE = "kwong";

    public static final String DEFAULT_DB_PASSWORD = "123456";


    public static final String JOB_FREQUENCY_DAY = "daily";

    public static final String JOB_FREQUENCY_WEEK = "week";

    public static final String JOB_FREQUENCY_MONTH = "month";

    public static final String JOB_STATE_RUNNING = "运行态";

    public static final String JOB_STATE_FREE = "空闲态";

    public static final int JOB_STATUS_ENABLE = 1;

    public static final int JOB_STATUS_DISABLE = 0;
}
