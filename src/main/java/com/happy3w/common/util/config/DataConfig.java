package com.happy3w.common.util.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class DataConfig {
    @Autowired
    Environment env;

    public String getDbUrl() {
        return env.getProperty("db.url");
    }

    public String getDbUser() {
        return env.getProperty("db.username");
    }

    public String getDbPwd() {
        return env.getProperty("db.password");
    }

    public String getDbDriver() {
        return    env.getProperty("db.driverClassName");
    }

    public String getAuthUrl() {
        return    env.getProperty("auth.url");
    }

    public int getPageSize() {
        String pageSize = env.getProperty("PreselectSettings.pageSize");
        if (pageSize == null || pageSize.isEmpty()) {
            return 50;
        }
        return Integer.parseInt(pageSize);
    }
}
