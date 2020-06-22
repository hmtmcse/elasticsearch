package com.hmtmcse.elasticsearch.schema;

import java.util.LinkedHashMap;

public class Properties {


    public LinkedHashMap<String, Property> properties = new LinkedHashMap<>();
    private Property lastProperty;


    public Properties addField(String name, String type) {
        lastProperty = properties.put(name, new Property(type));
        return this;
    }

    public Properties addKeyword(String name) {
        return addField(name, "keyword");
    }

    public Properties addText(String name) {
        return addField(name, "text");
    }

    public Properties addDate(String name) {
        return addField(name, "date");
    }

    public Properties addLong(String name) {
        return addField(name, "long");
    }

    public Properties addInteger(String name) {
        return addField(name, "integer");
    }

    public Properties addDouble(String name) {
        return addField(name, "double");
    }

    public Properties addFloat(String name) {
        return addField(name, "float");
    }

    public Properties addShortInt(String name) {
        return addField(name, "short");
    }

}
