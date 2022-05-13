package com.epam.esm.controller.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "devFactory",
        transactionManagerRef = "devTransaction")
@EnableJpaAuditing
@Profile("dev")
public class ConfigDev {
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties devProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    @Profile("dev")
    @Qualifier("devProperties")
    public DataSource devDatabase(DataSourceProperties devProperties) {
        return devProperties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean
    @Qualifier("devDatabase")
    public LocalContainerEntityManagerFactoryBean devFactory(DataSource devDatabase, EntityManagerFactoryBuilder builder) {
        return builder.dataSource(devDatabase).packages("com.epam.esm.dao.entity").build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager devTransaction(EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }

}
