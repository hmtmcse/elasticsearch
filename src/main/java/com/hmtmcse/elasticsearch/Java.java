package com.hmtmcse.elasticsearch;

import com.hmtmcse.elasticsearch.mysql.MySQLToElastic;
import com.hmtmcse.tmutil.mysql.JMQuery;
import com.hmtmcse.tmutil.mysql.JavaMySQLException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Java {


    public static void main(String[] args) {

        MySQLToElastic mySQLToElastic = new MySQLToElastic();
        mySQLToElastic.makeSchema("");

//        try {
//            JMQuery jmQuery = new JMQuery("localhost", "root", "", "_db");
//            ResultSet resultSet = jmQuery.selectSQL("SELECT COLUMN_NAME, DATA_TYPE, COLUMN_TYPE, CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION, NUMERIC_SCALE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = 'matview_corona_screening_stats'");
//            System.out.println("");
//            System.out.println("");
//            while (resultSet.next()){
//                System.out.println(
//                        resultSet.getString("COLUMN_NAME") +  " " +
//                        resultSet.getString("DATA_TYPE") +  " " +
//                        resultSet.getString("COLUMN_TYPE") +  " " +
//                        resultSet.getString("CHARACTER_MAXIMUM_LENGTH") +  " " +
//                        resultSet.getString("NUMERIC_PRECISION") +  " " +
//                        resultSet.getString("NUMERIC_SCALE") + " "
//                );
//            }
//
//        } catch (JavaMySQLException | SQLException e) {
//            e.printStackTrace();
//        }
    }

}
