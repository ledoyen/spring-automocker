package com.github.ledoyen.automocker.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.util.PropertyPlaceholderHelper;

import com.github.ledoyen.automocker.api.AfterBeanRegistration;
import com.github.ledoyen.automocker.api.AfterBeanRegistrationExecutable;
import com.github.ledoyen.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.ledoyen.automocker.api.jdbc.DataSourceResetter;
import com.github.ledoyen.automocker.api.jdbc.DatasourceLocator;
import com.github.ledoyen.automocker.tools.Classes;
import com.github.ledoyen.automocker.tools.Props;

/**
 * Alter bean definitions of {@link javax.sql.DataSource DataSources} by setting the URL of an embedded memory one.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@AfterBeanRegistration(MockJdbc.MockJdbcExecutable.class)
public @interface MockJdbc {

    String url() default "jdbc:h2:mem:${beanName};DB_CLOSE_DELAY=-1";

    class MockJdbcExecutable implements AfterBeanRegistrationExecutable<MockJdbc> {
        private static final String H2_DATASOURCE_CLASS = "org.h2.jdbcx.JdbcDataSource";
        private static final PropertyPlaceholderHelper PROPERTY_PLACEHOLDER_HELPER
                = new PropertyPlaceholderHelper("${", "}");

        @Override
        public void execute(MockJdbc annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            Set<ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata> dataSourceBeans = extendedBeanDefinitionRegistry
                    .getBeanDefinitionsForClass(DataSource.class);

            if (!dataSourceBeans.isEmpty()) {
                if (Classes.isPresent(H2_DATASOURCE_CLASS)) {
                    dataSourceBeans.forEach(meta -> {
                        meta.beanDefinitionModifier()
                                .setBeanClass(Classes.forName(H2_DATASOURCE_CLASS))
                                .setFactoryBeanName(null)
                                .setFactoryMethodName(null)
                                .removeConstructorArgumentValues()
                                .addPropertyValue("url",
                                        PROPERTY_PLACEHOLDER_HELPER.replacePlaceholders(annotation.url(), Props.of("beanName", meta.name())));
                    });

                    extendedBeanDefinitionRegistry.registerBeanDefinition(DatasourceLocator.class);
                } else {
                    throw new IllegalStateException("\nAutomocker is missing class [" + H2_DATASOURCE_CLASS + "] to mock " + dataSourceBeans.size() + " bean(s) of type [" + DataSource.class.getName() + "]: " +
                            dataSourceBeans.stream().map(ExtendedBeanDefinitionRegistry.BeanDefinitionMetadata::name).collect(Collectors.joining(", ")) +
                            "\nMake sure h2.jar is in the test classpath");
                }
                extendedBeanDefinitionRegistry.registerBeanDefinition(DataSourceResetter.class);
            }
        }
    }
}
