package com.inorista.situationpuzzle.repository;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DataSourceConfigProperties.class)
public class DataSourceConfig {
    private final DataSourceConfigProperties properties;

    public DataSourceConfig(DataSourceConfigProperties properties) {
        this.properties = properties;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        dataSource.setInitialSize(properties.getInitialSize());
        dataSource.setMaxIdle(properties.getMaxIdle());
        dataSource.setMinIdle(properties.getMinIdle());

        return dataSource;
    }
}
