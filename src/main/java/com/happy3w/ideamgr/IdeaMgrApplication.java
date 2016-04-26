package com.happy3w.ideamgr;

import com.happy3w.common.util.config.DataConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.happy3w.common", "com.happy3w.ideamgr"})
public class IdeaMgrApplication {
	private DataSource ds;
	@Autowired
	DataConfig dataConfig;
	@Bean
	public DataSource dataSource() {
		if (ds == null) {
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName(dataConfig.getDbDriver());
			dataSource.setUrl(dataConfig.getDbUrl());
			dataSource.setUsername(dataConfig.getDbUser());
			dataSource.setPassword(dataConfig.getDbPwd());
			ds = dataSource;
		}

		return ds;
	}
	@Bean
	public WebMvcConfigurer crossSiteConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
//		factory.setMaxFileSize("128KB");
//		factory.setMaxRequestSize("128KB");
		return factory.createMultipartConfig();
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasename("messages.messages");
		ms.setDefaultEncoding("utf-8");
		return ms;
	}
	public static void main(String[] args) {
		SpringApplication.run(IdeaMgrApplication.class, args);
	}
}
