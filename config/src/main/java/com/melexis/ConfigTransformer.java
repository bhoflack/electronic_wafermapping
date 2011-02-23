package com.melexis;

import com.melexis.util.LotTransformer;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.Map;

import static org.apache.commons.lang.StringUtils.capitalize;

public class ConfigTransformer implements LotTransformer {

    private final ConfigDao configDao;

    public ConfigTransformer(final ConfigDao configDao) {
        this.configDao = configDao;
    }

    public Lot process(final Lot l) throws Exception {
        for (final String f : l.getFieldOrder()) {
            final Object value = invokeGetterOnField(l, f);
            final Map<String, String> configs = configDao.findForKey(f, value.toString());

            l.getConfig().putAll(configs);
        }
        return l;
    }

    private final static Object invokeGetterOnField(final Lot l, final String field) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Method m = l.getClass().getMethod("get" + capitalize(field), new Class[0]);
        return m.invoke(l, new Object[0]);
    }
}