package com.mastercom.bigdata.common.sql;

/**
 * DBType
 *
 * Created by Kwong on 2017/9/22.
 */

public enum DBType {

//    连接类型[0 MysqlBase库/1Mysql业务库/2Mysql图形报表库/3oracle库/4Hive库]

    GREENPLUM(0, "GREENPLUM"), MYSQL(1, "MYSQL"), SQLSERVER(2,"SQLSERVER"), ORACLE(3, "ORACLE"), HIVESERVER(4, "Hive"), SPARK(5, "SPARK"), DERBY(6, "DERBY");

    private Integer value;

    private String name;

    DBType(Integer value, String name) {

        this.value = value;

        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName(){
        return name;
    }

    public static DBType fromName(String name){
        switch (name){
            case "GREENPLUM":
                return GREENPLUM;
            case "MYSQL":
                return MYSQL;
            case "SQLSERVER":
                return SQLSERVER;
            case "ORACLE":
                return ORACLE;
            case "Hive":
                return HIVESERVER;
            case "SPARK":
                return SPARK;
            case "DERBY":
                return DERBY;
            default:
                return DERBY;
        }
    }
}
