package com.github.ledoyen.automocker.examples;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.ContextConfiguration;

import com.github.ledoyen.automocker.SpringAutomocker;
import com.github.ledoyen.automocker.SpringAutomockerJUnit4ClassRunner;

@ContextConfiguration(classes = DataJpaApplication.class)
@SpringAutomocker
public class SqlApplicationTest {

	@Test
	public void fails_as_h2_is_not_in_classpath() throws InitializationError {
		try {
			new SpringAutomockerJUnit4ClassRunner(this.getClass());
			Assertions.fail("Exception is expected");
		} catch (IllegalStateException e) {
			Assertions.assertThat(e)
					.hasMessage("Automocker @com.github.ledoyen.automocker.ModifyBeanDefinition"
							+ "(beanDefinitionModifier=class com.github.ledoyen.automocker.internal.sql.H2DatasourceBeanDefinitionModifier,"
							+ " targetClass=interface javax.sql.DataSource) is missing class [org.h2.jdbcx.JdbcConnectionPool], make sure h2.jar is in the test classpath");
		}
	}
}
