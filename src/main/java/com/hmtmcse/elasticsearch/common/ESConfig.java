package com.hmtmcse.elasticsearch.common;

public class ESConfig {

    public String mysqlHost = "localhost";
    public String mysqlUsername = "root";
    public String mysqlPassword = "";
    public String mysqlDatabase = "_db";
    public String mysqlPort = "3306";


    public static ESConfig instance() {
        return new ESConfig();
    }

}
