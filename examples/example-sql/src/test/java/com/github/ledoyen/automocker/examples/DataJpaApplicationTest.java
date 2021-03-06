package com.github.ledoyen.automocker.examples;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ledoyen.automocker.base.Automocker;
import com.github.ledoyen.automocker.examples.simplieds.DataJpaApplication;
import com.github.ledoyen.automocker.examples.simplieds.repository.Customer;
import com.github.ledoyen.automocker.examples.simplieds.repository.CustomerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataJpaApplication.class)
@Automocker
public class DataJpaApplicationTest {

    @Autowired
    private CustomerRepository repo;

    @Test
    public void repository_is_available_and_consistent() throws InitializationError {
        Assertions.assertThat(repo.count())
                .isEqualTo(0L);
        repo.save(new Customer("Scarlett", "Johansson"));
        Assertions.assertThat(repo.count())
                .isEqualTo(1L);
        repo.save(new Customer("Jessica", "Alba"));
        Assertions.assertThat(repo.count())
                .isEqualTo(2L);

        repo.delete(repo.findByLastName("Johansson"));
        Assertions.assertThat(repo.count())
                .isEqualTo(1L);
    }
}
