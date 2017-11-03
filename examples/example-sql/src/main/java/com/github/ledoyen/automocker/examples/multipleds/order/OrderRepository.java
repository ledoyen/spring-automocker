package com.github.ledoyen.automocker.examples.multipleds.order;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.github.ledoyen.automocker.examples.multipleds.customer.Customer.CustomerId;

/**
 * Spring Data repository managing {@link Order}s.
 *
 * @author Oliver Gierke
 */
public interface OrderRepository extends CrudRepository<Order, Long> {

    /**
     * Returns all {@link Order}s for the {@link Customer} with the given identifier.
     *
     * @param id must not be {@literal null}.
     * @return
     */
    List<Order> findByCustomer(CustomerId id);
}
