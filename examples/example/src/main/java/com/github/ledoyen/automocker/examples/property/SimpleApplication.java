package com.github.ledoyen.automocker.examples.property;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("classpath:appli.properties")
public class SimpleApplication {

	public static void main(String[] args) {
		try (ConfigurableApplicationContext context = SpringApplication.run(SimpleApplication.class, args)) {
			TextService service = context.getBean(TextService.class);

			System.out.println(service.getText());
		}
	}
}
