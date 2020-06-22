package com.hmtmcse.elasticsearch.mysql;

import com.hmtmcse.elasticsearch.common.ESConfig;
import com.hmtmcse.elasticsearch.schema.ESSchema;
import com.hmtmcse.tmutil.mysql.JMQuery;
import com.hmtmcse.tmutil.mysql.JavaMySQLException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLToElastic {

    private JMQuery jmQuery;
    private String SELECT_TABLE_INFO = "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_TYPE, CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION, NUMERIC_SCALE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = ";
    private ESSchema esSchema;


    public void init() {
        jmQuery = new JMQuery(ESConfig.instance().mysqlHost, ESConfig.instance().mysqlUsername, ESConfig.instance().mysqlPassword, ESConfig.instance().mysqlDatabase);
        esSchema = new ESSchema();
    }

    private void addField(String name, String mysqlType) {

        switch (mysqlType) {
            case "datetime":
                esSchema.property().addDate(name);
                break;
            case "bigint":
                esSchema.property().addLong(name);
                break;
            case "tinyint ":
                esSchema.property().addShortInt(name);
                break;
            case "varchar":
                esSchema.property().addText(name);
                break;
            case "double":
                esSchema.property().addDouble(name);
                break;
            case "int":
                esSchema.property().addInteger(name);
                break;
        }

    }

    private void createSchema(ResultSet row) throws SQLException {
        addField(row.getString("COLUMN_NAME"), row.getString("DATA_TYPE"));
//        System.out.println(
//                row.getString("COLUMN_NAME") +  " " +
//                        row.getString("DATA_TYPE") +  " " +
//                        row.getString("COLUMN_TYPE") +  " " +
//                        row.getString("CHARACTER_MAXIMUM_LENGTH") +  " " +
//                        row.getString("NUMERIC_PRECISION") +  " " +
//                        row.getString("NUMERIC_SCALE") + " "
//        );
    }

    public void makeSchema(String tableName){
        init();
        try {
            String sql = SELECT_TABLE_INFO + "'" + tableName + "'";
            ResultSet resultSet = jmQuery.selectSQL(sql);
            while (resultSet.next()){
                createSchema(resultSet);
            }
            System.out.println(esSchema.getMappings());
        } catch (JavaMySQLException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String getSchema(String tableName) {
        String schema = "{}";
        try {
            String sql = SELECT_TABLE_INFO + "'" + tableName + "'";
            ResultSet resultSet = jmQuery.selectSQL(sql);
            while (resultSet.next()){
                createSchema(resultSet);
            }
        } catch (JavaMySQLException | SQLException e) {
            e.printStackTrace();
        }
        return schema;
    }

}
