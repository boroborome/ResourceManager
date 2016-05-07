package com.happy3w.common.util.convert;

/**
 * Created by ysgao on 5/3/16.
 */
public interface ITypeConverter <T> {
    Class<T> getTyppe();
    String toString(T value);
    T parse(String value);
}
