package com.epam.esm.controller.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "prodFactory", transactionManagerRef = "prodTransaction")
public class ConfigProd {

    @Bean
    @ConfigurationProperties("spring.second-datasource")
    public DataSourceProperties prodProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource prodDatabase(@Qualifier("prodProperties") DataSourceProperties prodProperties) {
        return prodProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean prodFactory(@Qualifier("prodDatabase") DataSource prodDatabase, EntityManagerFactoryBuilder builder) {
        return builder.dataSource(prodDatabase).packages("com.epam.esm.dao.entity").build();
    }

    @Bean
    public PlatformTransactionManager prodTransaction(EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}
