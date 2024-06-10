package com.feroxdev.inmobigestor.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.feroxdev.inmobigestor")
@EnableJpaRepositories(basePackages = "com.feroxdev.inmobigestor.repository")
@EnableTransactionManagement
public class AppConfig {
}