package com.happy3w.common.util.convert;

/**
 * Created by ysgao on 5/3/16.
 */
public class DummyTypeConverter implements ITypeConverter {
    @Override
    public Class getTyppe() {
        return Object.class;
    }

    @Override
    public String toString(Object value) {
        return "";
    }

    @Override
    public Object parse(String value) {
        return null;
    }
}
