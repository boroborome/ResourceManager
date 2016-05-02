package com.happy3w.ideamgr;

import com.happy3w.common.util.config.DataConfig;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import java.io.File;

@SpringBootApplication
@ComponentScan(basePackages = {"com.happy3w.common", "com.happy3w.ideamgr"})
@EnableConfigurationProperties(IdeaMgrApplication.TomcatSslConnectorProperties.class)
@PropertySource("classpath:/config/tomcat.https.properties")
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


	@ConfigurationProperties(prefix = "custom.tomcat.https")
	public static class TomcatSslConnectorProperties {
		private Integer port;
		private Boolean ssl = true;
		private Boolean secure = true;
		private String scheme = "https";
		private File keystore;
		private String keystorePassword;

		public void configureConnector(Connector connector) {
			if (port != null) {
				connector.setPort(port);
			}
			if (secure != null) {
				connector.setSecure(secure);
			}
			if (scheme != null) {
				connector.setScheme(scheme);
			}
			if (ssl != null) {
				connector.setProperty("SSLEnabled", ssl.toString());
			}
			if (keystore != null) {
				String keystoreFile = keystore.exists() ? keystore.getAbsolutePath() : keystore.toString();
				connector.setProperty("keystoreFile", keystoreFile);
				connector.setProperty("keystorePass", keystorePassword);
			}
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public Boolean getSsl() {
			return ssl;
		}

		public void setSsl(Boolean ssl) {
			this.ssl = ssl;
		}

		public Boolean getSecure() {
			return secure;
		}

		public void setSecure(Boolean secure) {
			this.secure = secure;
		}

		public String getScheme() {
			return scheme;
		}

		public void setScheme(String scheme) {
			this.scheme = scheme;
		}

		public File getKeystore() {
			return keystore;
		}

		public void setKeystore(File keystore) {
			this.keystore = keystore;
		}

		public String getKeystorePassword() {
			return keystorePassword;
		}

		public void setKeystorePassword(String keystorePassword) {
			this.keystorePassword = keystorePassword;
		}
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer(TomcatSslConnectorProperties properties) {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		tomcat.addAdditionalTomcatConnectors(createSslConnector(properties));
		return tomcat;
	}

	private Connector createSslConnector(TomcatSslConnectorProperties properties) {
		Connector connector = new Connector();
		properties.configureConnector(connector);
		return connector;
	}

	public static void main(String[] args) {
		SpringApplication.run(IdeaMgrApplication.class, args);
	}
}
