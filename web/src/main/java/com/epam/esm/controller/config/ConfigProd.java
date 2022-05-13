package com.epam.esm.controller.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
@Profile("prod")
public class ConfigProd {

    @Bean
    @ConfigurationProperties("spring.second-datasource")
    public DataSourceProperties prodProperties() {
        return new DataSourceProperties();
    }


    @Profile("prod")
    @Bean
    @Qualifier("prodProperties")
    public DataSource prodDatabase(DataSourceProperties prodProperties) {
        return prodProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Qualifier("prodDatabase")
    public LocalContainerEntityManagerFactoryBean prodFactory( DataSource prodDatabase, EntityManagerFactoryBuilder builder) {
        return builder.dataSource(prodDatabase).packages("com.epam.esm.dao.entity").build();
    }

    @Bean
    public PlatformTransactionManager prodTransaction(EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}
