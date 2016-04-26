package com.happy3w.common.util.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;


@Configuration
@Profile("wave3_sit")
@PropertySource("classpath:/config/config_sit.properties")
public class DataConfigSit  extends DataConfig{

}
