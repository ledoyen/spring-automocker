package com.github.ledoyen.automocker.examples.simplieds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.ledoyen.automocker.examples.simplieds.repository.Customer;
import com.github.ledoyen.automocker.examples.simplieds.repository.CustomerRepository;

@SpringBootApplication
public class DataJpaApplication {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(DataJpaApplication.class, args)) {
            CustomerRepository repository = context.getBean(CustomerRepository.class);

            Customer cust1 = new Customer("Scarlett", "Johansson");
            Customer cust2 = new Customer("Jessica", "Alba");
            repository.save(cust1);
            repository.save(cust2);

            System.out.println(repository.findAll());
        }
    }
}
