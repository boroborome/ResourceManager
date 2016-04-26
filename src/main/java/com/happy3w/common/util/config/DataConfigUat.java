package com.happy3w.common.util.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("wave3_uat")
@PropertySource("classpath:/config/config_uat.properties")
public class DataConfigUat  extends DataConfig {
}
