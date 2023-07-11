package me.youzheng.core.configure.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    public static final String DATASOURCE_HIKARI_PREFIX = "spring.datasource.hikari";

    @ConditionalOnMissingBean(name = "defaultDataSource", value = DataSource.class)
    @Bean("defaultDataSource")
    @ConfigurationProperties(DATASOURCE_HIKARI_PREFIX)
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

}