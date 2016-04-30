package com.happy3w.common.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ysgao on 4/27/16.
 */
public class SqlUtil {
    public static void fillParams(PreparedStatement statement, List<Object> lstParam) throws SQLException {
        int index = 1;
        for (Object obj : lstParam) {
            fillParam(statement, index, obj);
            ++index;
        }
    }
    public static void fillParams(PreparedStatement statement, Object ... aryParam) throws SQLException {
        int index = 1;
        for (Object obj : aryParam) {
            fillParam(statement, index, obj);
            ++index;
        }
    }

    public static void fillParam(PreparedStatement statement, int index, Object value) throws SQLException {
        if (value instanceof Number) {
            statement.setInt(index, ((Number) value).intValue());
        } else {
            statement.setString(index, String.valueOf(value));
        }
    }
}
