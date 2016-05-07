package com.happy3w.common.util.convert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ysgao on 5/3/16.
 */
public class TypeConverterManager {

    private static TypeConverterManager instance;
    public static TypeConverterManager getInstance() {
        if (instance == null) {
            instance = new TypeConverterManager();
        }
        return instance;
    }

    private Map<Class, ITypeConverter> mapConverter = new HashMap<Class, ITypeConverter>();
    private ITypeConverter defaultConverter = new DummyTypeConverter();

    public TypeConverterManager() {
        register(new StringConverter());
        register(new IntegerConverter());
    }

    public void register(ITypeConverter converter) {
        mapConverter.put(converter.getTyppe(), converter);
    }

    public ITypeConverter getConverter(Class type) {
        ITypeConverter tc = mapConverter.get(type);
        if (tc == null) {
            tc = defaultConverter;
        }
        return tc;
    }
}
