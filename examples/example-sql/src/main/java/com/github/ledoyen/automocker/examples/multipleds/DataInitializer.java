package com.github.ledoyen.automocker.examples.multipleds;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.ledoyen.automocker.examples.multipleds.customer.Customer;
import com.github.ledoyen.automocker.examples.multipleds.customer.Customer.CustomerId;
import com.github.ledoyen.automocker.examples.multipleds.customer.CustomerRepository;
import com.github.ledoyen.automocker.examples.multipleds.order.Order;
import com.github.ledoyen.automocker.examples.multipleds.order.Order.LineItem;
import com.github.ledoyen.automocker.examples.multipleds.order.OrderRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Sample component to demonstrate how to work with repositories backed by different {@link DataSource}s. Note how we explicitly select a transaction
 * manager by name. In this particular case (only one operation on the repository) this is not strictly necessary. However, if multiple repositories
 * or multiple interactions on the very same repsoitory are to be executed in a method we need to expand the transaction boundary around these
 * interactions. It's recommended to create a dedicated annotation meta-annotated with {@code @Transactional("…")} to be able to refer to a particular
 * datasource without using String qualifiers.
 * <p>
 * Also, not that one cannot interact with both databases in a single, transactional method as transactions are thread bound in Spring an thus only a
 * single transaction can be active in a single thread. See {@link MultipleDatasourcesApplication#init()} for how to orchestrate the calls.
 *
 * @author Oliver Gierke
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataInitializer {

    private final @NonNull
    OrderRepository orders;
    private final @NonNull
    CustomerRepository customers;

    /**
     * Initializes a {@link Customer}.
     *
     * @return
     */
    @Transactional("customerTransactionManager")
    public CustomerId initializeCustomer() {
        return customers.save(new Customer("Dave", "Matthews"))
                .getId();
    }

    /**
     * Initializes an {@link Order}.
     *
     * @param customer must not be {@literal null}.
     * @return
     */
    @Transactional("orderTransactionManager")
    public void initializeOrder(CustomerId customer) {

        Assert.notNull(customer, "Customer identifier must not be null!");

        Order order1 = new Order(customer);
        order1.add(new LineItem(null, "Fender Jag-Stang Guitar"));
        orders.save(order1);

        Order order2 = new Order(customer);
        order2.add(new LineItem(null, "Gene Simmons Axe Bass"));
        orders.save(order2);

    }
}
