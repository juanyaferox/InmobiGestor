package com.feroxdev.inmobigestor.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@PropertySource ("classpath:application.properties")
@ComponentScan(basePackages = "com.feroxdev.inmobigestor")
@EnableJpaRepositories(basePackages = "com.feroxdev.inmobigestor.repository")
@EnableTransactionManagement
public class AppConfig {
    @Bean (destroyMethod = "close")
    DataSource dataSource(Environment env) {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        try {
            ds.setDriverClass(env.getRequiredProperty("jdbc.driverClassName"));
        } catch (IllegalStateException | PropertyVetoException ex) {
            throw new RuntimeException("error while setting the driver class name in the datasource", ex);
        }
        ds.setJdbcUrl(env.getRequiredProperty("jdbc.url"));
        ds.setUser(env.getRequiredProperty("jdbc.username"));
        ds.setPassword(env.getRequiredProperty("jdbc.password"));
        ds.setAcquireIncrement(env.getRequiredProperty("c3p0.acquire_increment", Integer.class));
        ds.setMinPoolSize(env.getRequiredProperty("c3p0.min_size", Integer.class));
        ds.setMaxPoolSize(env.getRequiredProperty("c3p0.max_size", Integer.class));
        ds.setMaxIdleTime(env.getRequiredProperty("c3p0.max_idle_time", Integer.class));
        ds.setUnreturnedConnectionTimeout(
                env.getRequiredProperty("c3p0.unreturned_connection_timeout", Integer.class));

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                       Environment env) {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setPackagesToScan("com.feroxdev.inmobigestor.model");
        entityManager.setDataSource(dataSource);
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.connection.autocommit", env.getRequiredProperty("hibernate.connection.autocommit"));
        jpaProperties.put("hibernate.generate_statistics", env.getRequiredProperty("hibernate.statistics"));
        jpaProperties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        jpaProperties.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
        jpaProperties.put("hibernate.enable_lazy_load_no_trans", env.getRequiredProperty("hibernate.enable_lazy_load_no_trans"));
        entityManager.setJpaProperties(jpaProperties);

        return entityManager;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}