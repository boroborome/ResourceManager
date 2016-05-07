package com.happy3w.common.util.convert;

/**
 * Created by ysgao on 5/3/16.
 */
public class StringConverter implements ITypeConverter<String> {
    @Override
    public Class<String> getTyppe() {
        return String.class;
    }

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public String parse(String value) {
        return value;
    }
}
