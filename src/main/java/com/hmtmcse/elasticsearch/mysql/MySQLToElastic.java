package com.hmtmcse.elasticsearch.mysql;

import com.hmtmcse.elasticsearch.common.ESConfig;
import com.hmtmcse.elasticsearch.schema.ESSchema;
import com.hmtmcse.httputil.HttpExceptionHandler;
import com.hmtmcse.httputil.HttpResponse;
import com.hmtmcse.httputil.HttpUtil;
import com.hmtmcse.parser4java.JsonProcessor;
import com.hmtmcse.parser4java.common.Parser4JavaException;
import com.hmtmcse.tmutil.mysql.JMQuery;
import com.hmtmcse.tmutil.mysql.JavaMySQLException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MySQLToElastic {

    private JMQuery jmQuery;
    private String SELECT_TABLE_INFO = "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_TYPE, CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION, NUMERIC_SCALE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = ";
    private String SELECT_ALL = "SELECT * FROM ";
    private String COUNT = "SELECT COUNT(*) as rowcount FROM ";
    private ESSchema esSchema;
    private JsonProcessor jsonProcessor;
    public Integer itemPerChunk = 5;


    public void init() {
        jmQuery = new JMQuery(ESConfig.instance().mysqlHost, ESConfig.instance().mysqlUsername, ESConfig.instance().mysqlPassword, ESConfig.instance().mysqlDatabase);
        esSchema = new ESSchema();
        jsonProcessor = new JsonProcessor();
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
        } catch (JavaMySQLException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String getSchema(String tableName) {
        makeSchema(tableName);
        return esSchema.getMappings();
    }


    private List<String> getAvailableColumn(ResultSet resultSet) {
        List<String> list = new ArrayList<>();
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int numberOfColumns = resultSetMetaData.getColumnCount();
            for (int i = 1; i < numberOfColumns + 1; i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                if (columnName != null && esSchema != null && esSchema.property().isFieldAvailable(columnName)) {
                    list.add(columnName);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    private void availableColumn(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsMetaData = resultSet.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();

        for (int i = 1; i < numberOfColumns + 1; i++) {
            String columnName = rsMetaData.getColumnName(i);

            System.out.println(columnName);

//            if ("theColumn".equals(columnName)) {
//                System.out.println("Bingo!");
//            }
        }
    }

    public List<String> getAllowedColumn(String tableName) {
        try {
            String sql = SELECT_ALL + tableName + " LIMIT 0";
            ResultSet resultSet = jmQuery.selectSQL(sql);
            return getAvailableColumn(resultSet);
        } catch (JavaMySQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String bulkInsertDocument(LinkedHashMap<String, Object> data){
        String response = "";
        try{
            response += "{ \"index\":{} }\n";
            response += jsonProcessor.klassToString(data) + "\n";
        } catch (Parser4JavaException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Integer count(String tableName) {
        try {
            ResultSet resultSet = jmQuery.selectSQL(COUNT + tableName);
            resultSet.next();
            return resultSet.getInt("rowcount");
        } catch (JavaMySQLException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void prepareSelectData(String tableName) {
        makeSchema(tableName);
        HttpUtil httpUtil = new HttpUtil();
        try {
            List<String> availableColumn = getAllowedColumn(tableName);
            String response = "";
            if (availableColumn.size() != 0){
                LinkedHashMap<String, Object> map;
                Integer total = 20; //count(tableName);
                Integer offset = 0;
                Double loop = Math.ceil(Double.valueOf(total) / itemPerChunk);
                for (Integer index = 0; index < loop.intValue(); index++){
                    offset = index * itemPerChunk;
                    String sql = SELECT_ALL + tableName + " LIMIT " + offset + ", " + itemPerChunk;
                    ResultSet resultSet = jmQuery.selectSQL(sql);
                    StringBuilder stringBuilder = new StringBuilder();
                    while (resultSet.next()){
                        map = new LinkedHashMap<>();
                        for ( String columnName : availableColumn){
                            map.put(columnName, resultSet.getObject(columnName));
                        }
                        response = bulkInsertDocument(map);
                        if (!response.equals("")){
                            stringBuilder.append(response);
                        }
                    }
                    HttpResponse res = httpUtil.jsonPost("http://localhost:9200/data_entry/_bulk", stringBuilder.toString()).send();
                    System.out.println(res.getHttpCode() + " " + res.getContent());
                }
            }
        } catch (JavaMySQLException | SQLException | HttpExceptionHandler e) {
            e.printStackTrace();
        }

    }

}
