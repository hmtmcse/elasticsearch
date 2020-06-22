package com.hmtmcse.elasticsearch.schema;

import com.hmtmcse.parser4java.JsonProcessor;
import com.hmtmcse.parser4java.common.Parser4JavaException;

import java.util.LinkedHashMap;

public class ESSchema {

    private Properties properties = new Properties();
    private JsonProcessor jsonProcessor;

    public ESSchema() {
        jsonProcessor = new JsonProcessor();
    }

    public Properties property() {
        return properties;
    }

    public String getMappings(){
        LinkedHashMap<String, Object> mappings = new LinkedHashMap<>();
        LinkedHashMap<String, Properties> propertyMap = new LinkedHashMap<>();
        propertyMap.put("properties", properties);
        mappings.put("mappings", properties);
        try{
            return jsonProcessor.klassToString(mappings);
        } catch (Parser4JavaException e) {
            e.printStackTrace();
        }
        return "{}";
    }

}
