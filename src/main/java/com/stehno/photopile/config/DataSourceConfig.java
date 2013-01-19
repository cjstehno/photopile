package com.stehno.photopile.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
    // FIXME: this is a temporary setup
    // I need to consider the pros/cons of how this DB is setup - jdbc will suffice for now as a quick start

    @Bean
    public DataSource dataSource(){
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/photopile_dev");
        dataSource.setUsername("photopile");
        dataSource.setPassword("photopile");
        return dataSource;
    }

    @Bean
    public SessionFactory sessionFactory() throws IOException {
        final LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource());
        factory.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));
        factory.setHibernateProperties(new Properties(){
            {
                setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                setProperty("hibernate.show_sql", "true");
                setProperty("hibernate.hbm2ddl.auto", "create-drop");
            }
        });
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public HibernateTransactionManager transactionManager() throws IOException {
        HibernateTransactionManager manager = new HibernateTransactionManager();
        manager.setSessionFactory(sessionFactory());
        return manager;
    }
}
