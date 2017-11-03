package com.github.ledoyen.automocker.examples;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.assertj.core.groups.Tuple;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.ledoyen.automocker.api.jdbc.DatasourceLocator;
import com.github.ledoyen.automocker.base.Automocker;
import com.github.ledoyen.automocker.base.MockJdbc;
import com.github.ledoyen.automocker.examples.multipleds.MultipleDatasourcesApplication;
import com.github.ledoyen.automocker.examples.multipleds.customer.CustomerRepository;
import com.github.ledoyen.automocker.examples.multipleds.order.OrderRepository;

@RunWith(Enclosed.class)
public class MultipleDatasourcesTest {

    @SuppressWarnings("unchecked")
    private static List<String> listTables(DataSource dataSource)
            throws MetaDataAccessException, SQLException {
        return (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, dbmd -> {
            ResultSet rs = dbmd.getTables(dbmd.getUserName(), null, null, new String[]{"TABLE"});
            List<String> names = new ArrayList<>();
            while (rs.next()) {
                names.add(rs.getString(3));
            }
            return names;
        });
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(classes = MultipleDatasourcesApplication.class)
    @Automocker
    @Transactional(transactionManager = "customerTransactionManager")
    public static class CustormerTest {

        @Autowired
        private CustomerRepository customerRepo;

        @Test
        public void data_initializer_have_already_persisted_customer() {
            assertThat(customerRepo.findAll()).hasSize(1)
                    .extracting("firstname", "lastname")
                    .contains(new Tuple("Dave", "Matthews"));
        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(classes = MultipleDatasourcesApplication.class)
    @MockJdbc
    @Transactional(transactionManager = "orderTransactionManager")
    public static class OrderTest {

        @Autowired
        private OrderRepository orderRepo;

        @Test
        public void data_initializer_have_already_persisted_orders() {
            assertThat(orderRepo.findAll()).hasSize(2);
            assertThat(Sets.newHashSet(orderRepo.findAll())
                    .stream()
                    .flatMap(o -> o.getLineItems()
                            .stream())
                    .collect(Collectors.toList())).extracting("description")
                    .contains("Fender Jag-Stang Guitar", "Gene Simmons Axe Bass");
        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(classes = MultipleDatasourcesApplication.class)
    @MockJdbc
    @Transactional(transactionManager = "orderTransactionManager")
    public static class DatasourcesTest {

        @Autowired
        private Map<String, DataSource> datasources;

        @Test
        public void datasources_are_different_and_data_is_not_shared()
                throws MetaDataAccessException, SQLException {
            assertThat(datasources).containsOnlyKeys("customerDataSource", "orderDataSource");
            assertThat(listTables(datasources.get("customerDataSource"))).containsExactly("CUSTOMER")
                    .doesNotContain("SAMPLEORDER", "LINEITEM");
            assertThat(listTables(datasources.get("orderDataSource"))).contains("SAMPLEORDER", "LINEITEM")
                    .doesNotContain("CUSTOMER");
        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(classes = MultipleDatasourcesApplication.class)
    @MockJdbc
    public static class DatasourceLocatorTest {

        @Autowired
        private DatasourceLocator datasourceLocator;

        @Test
        public void accessing_datasource_without_name_throws() {
            try {
                datasourceLocator.getDataSource();
                fail("Accessing datasource as an unique one should have thrown an exception");
            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage()).isEqualTo("Multiple datasources available: customerDataSource, orderDataSource");
            }
        }
    }
}
