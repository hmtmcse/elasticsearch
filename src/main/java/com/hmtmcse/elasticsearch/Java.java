package com.hmtmcse.elasticsearch;

import com.hmtmcse.elasticsearch.mysql.MySQLToElastic;


public class Java {


    public static void main(String[] args) {
        MySQLToElastic mySQLToElastic = new MySQLToElastic();
//        System.out.println(mySQLToElastic.getSchema(""));
        mySQLToElastic.prepareSelectData("");

//        try {
//            JMQuery jmQuery = new JMQuery("localhost", "root", "", "");
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
