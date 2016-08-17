package com.github.ledoyen.automocker.examples.multipleds.customer;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository to manage {@link Customer}s.
 * 
 * @author Oliver Gierke
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	Optional<Customer> findByLastname(String lastname);
}
