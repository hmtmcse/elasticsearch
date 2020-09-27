package com.hmtmcse.elasticsearch.common;

public class ESConfig {

    public String mysqlHost = "localhost";
    public String mysqlUsername = "----";
    public String mysqlPassword = "----";
    public String mysqlDatabase = "------";
    public String mysqlPort = "3307";


    public static ESConfig instance() {
        return new ESConfig();
    }

}
