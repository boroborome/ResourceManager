package com.happy3w.common.db;

import com.happy3w.common.util.convert.ITypeConverter;

/**
 * Created by ysgao on 5/3/16.
 */
public class DBColumn {
    private String name;
    private Class type;
    private ITypeConverter converter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public ITypeConverter getConverter() {
        return converter;
    }

    public void setConverter(ITypeConverter converter) {
        this.converter = converter;
    }
}
