package com.happy3w.common.util.convert;

/**
 * Created by ysgao on 5/3/16.
 */
public class IntegerConverter implements ITypeConverter<Integer> {
    @Override
    public Class<Integer> getTyppe() {
        return Integer.class;
    }

    @Override
    public String toString(Integer value) {
        return String.valueOf(value);
    }

    @Override
    public Integer parse(String value) {
        if (value == null || value.isEmpty()) {
            return 0;
        }

        int posPoint = value.indexOf('.');
        if (posPoint == 0) {
            return 0;
        } if (posPoint > 0) {
            value = value.substring(0, posPoint);
        }
        return Integer.valueOf(value);
    }
}
