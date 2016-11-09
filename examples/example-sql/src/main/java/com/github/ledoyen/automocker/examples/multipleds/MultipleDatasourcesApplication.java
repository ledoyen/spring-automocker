package com.github.ledoyen.automocker.examples.multipleds;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.ledoyen.automocker.examples.multipleds.customer.Customer.CustomerId;

/**
 * Core Spring Boot application configuration. Note, that we explicitly deactivate some auto-configurations explicitly. They mostly will even disable
 * automatically if special bean names are used (e.g. {@code entityManagerFactory}) but I wanted to keep the two configurations symmetric. The
 * configuration classes being located in separate packages serves the purpose of scoping the Spring Data repository scanning to those packages so
 * that the infrastructure setup is attached to the corresponding repository instances.
 * <p>
 * {@link DevToolsDataSourceAutoConfiguration} is explicitly excluded until {@link https://github.com/spring-projects/spring-boot/issues/5540} is
 * fixed. {@link https://github.com/spring-projects/spring-boot/issues/5541} has been filed to improve the need for manual exclusions in general.
 * 
 * @author Oliver Gierke
 * @see example.springdata.jpa.multipleds.customer.CustomerConfig
 * @see example.springdata.jpa.multipleds.order.OrderConfig
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class })
@EnableTransactionManagement
public class MultipleDatasourcesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultipleDatasourcesApplication.class, args);
	}

	@Autowired
	DataInitializer initializer;

	@PostConstruct
	public void init() {
		CustomerId customerId = initializer.initializeCustomer();
		initializer.initializeOrder(customerId);
	}
}
