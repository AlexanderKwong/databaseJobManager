package com.mastercom.bigdata.tools.sql;

/**
 * DBType
 *
 * Created by Kwong on 2017/9/22.
 */

public enum DBType {

//    连接类型[0 MysqlBase库/1Mysql业务库/2Mysql图形报表库/3oracle库/4Hive库]

    GreenPlum(0, "GreenPlum"), MySQL(1, "MySQL"), SqlServer(2,"SqlServer"), Oracle(3, "Oracle"), HiveServer(4, "Hive"), Spark(5, "Spark"), Derby(6, "Derby");

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
            case "GreenPlum":
                return GreenPlum;
            case "MySQL":
                return MySQL;
            case "SqlServer":
                return SqlServer;
            case "Oracle":
                return Oracle;
            case "Hive":
                return HiveServer;
            case "Spark":
                return Spark;
            case "Derby":
                return Derby;
            default:
                return Derby;
        }
    }
}
