package com.github.ledoyen.automocker.examples.multipleds.order;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration for the {@link Order} slice of the system. A dedicated {@link DataSource}, {@link JpaTransactionManager} and
 * {@link EntityManagerFactory}. Note that there could of course be some deduplication with
 * {@link example.springdata.jpa.multipleds.customer.CustomerConfig}. I just decided to keep it to focus on the sepeartion of the two. Also, some
 * overlaps might not even occur in real world scenarios (whether to create DDl or the like).
 *
 * @author Oliver Gierke
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "orderEntityManagerFactory", transactionManagerRef = "orderTransactionManager")
class OrderConfig {

	@Bean
	PlatformTransactionManager orderTransactionManager() {
		return new JpaTransactionManager(orderEntityManagerFactory().getObject());
	}

	@Bean
	LocalContainerEntityManagerFactoryBean orderEntityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

		factoryBean.setDataSource(orderDataSource());
		factoryBean.setJpaVendorAdapter(vendorAdapter);
		factoryBean.setPackagesToScan(OrderConfig.class.getPackage()
				.getName());

		return factoryBean;
	}

	@Bean
	DataSource orderDataSource() {

		return new EmbeddedDatabaseBuilder().//
				setType(EmbeddedDatabaseType.HSQL)
				.//
				setName("orders")
				.//
				build();
	}
}
