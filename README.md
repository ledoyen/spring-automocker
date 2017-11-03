# Spring-Automocker
[![Build status](https://ci.appveyor.com/api/projects/status/gm3oos59plasbipi/branch/master?svg=true)](https://ci.appveyor.com/project/ledoyen/spring-automocker/branch/master)

Compatible with Spring >= 4.3 (so spring-boot >= 1.4)

PoC of automatic IO mocking tool for Spring's JavaConfig.

Load your entire Production configuration into a JUnit test.

The purpose of this project is to test functionalities of applications on real code without heavy glue code.

Currently the project supports mocking of
* `@PropertySource`
* __Spring-web__ [example](examples/example-mvc/src/test/java/com/github/ledoyen/automocker/examples/MvcApplicationTest.java)
* __JDBC__ [examples](examples/example-sql/README.md)
* __JMS__ [example](examples/example-jms/src/test/java/com/github/ledoyen/automocker/examples/JmsApplicationTest.java)

Use `SpringJUnit4ClassRunner` in conjunction with `@Automocker`
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MyApplication.class)
@Automocker
public class MyApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void user_creation_api() {
		mvc.perform(MockMvcRequestBuilders.get("/create_user?firstName=Alyson&lastName=Hannigan"))
			.andExpect(MockMvcResultMatchers.status().isOk());

		mvc.perform(MockMvcRequestBuilders.get("/list_users"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("[\"Alyson Hannigan\"]"));
	}
}
```

## Mocking strategies
### Spring-web
There is nothing to mock here. However some things are nonetheless being taken care of
* If any bean annotated with **spring-context** `@Controller` is present, **spring-test** `MockMvc` is configured and added to the test context

### JDBC
All beans implementing `javax.sql.DataSource` are replaced with distinct `org.h2.jdbcx.JdbcDataSource`.
A `DataSourceLocator` bean is supplied to easily access multiple dataSources.

### JMS
* All beans implementing `javax.jms.ConnectionFactory` are replaced by a **mockrunner** `MockConnectionFactory`. Each factory is built with its own `DestinationManager`
* If any bean implementing `javax.jms.ConnectionFactory` is present, a custom `JmsMock` is configured and added to the test context
* If any bean implementing **spring-jms** `JmsListenerContainerFactory` is present, a wrapper is set around its `ErrorHandler` and can be accessed through `JmsMock`
* A `JmsMockLocator` bean is supplied to easily access multiple `JmsMock`.

## Extending Spring-Automocker

### @AfterBeanRegistration extension
A test class meta-annotated with `@AfterBeanRegistration` will trigger specific behavior through the indicated `AfterBeanRegistrationExecutable` implementation.
This annotation can only be used on an annotation as the `AfterBeanRegistrationExecutable#execute` will be supplied with the annotation annotated with `AfterBeanRegistration`.
Doing so the developper can defined specific arguments in the annotation, such as `@MockJdbc#url()` and used them in the `AfterBeanRegistrationExecutable` implementation.

For example, replacing user-defined beans by Mockito mocks can be done like this:
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AfterBeanRegistration(MockBeans.MockBeansExecutable.class)
public @interface MockBeans {

    Class<?>[] classes();

    class MockBeansExecutable implements AfterBeanRegistrationExecutable<MockBeans> {

        @Override
        public void execute(MockBeans annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            String mockCreatorBeanName = extendedBeanDefinitionRegistry.registerBeanDefinition(MockCreator.class);
            Arrays.stream(annotation.classes())
                    .map(extendedBeanDefinitionRegistry::getBeanDefinitionsForClass)
                    .flatMap(Set::stream)
                    .forEach(bean -> replaceDefinitionByMock(mockCreatorBeanName, bean));
        }

        private static void replaceDefinitionByMock(String mockCreatorBeanName, BeanDefinitionMetadata beanDefinitionMetadata) {
            beanDefinitionMetadata.beanDefinitionModifier()
                    .setFactoryBeanName(mockCreatorBeanName)
                    .setFactoryMethodName("createMock")
                    .addIndexedArgumentValue(0, beanDefinitionMetadata.beanClass());
        }
    }

    class MockCreator {
        @Bean
        public <T> T createMock(Class<T> clazz) {
            return Mockito.mock(clazz);
        }
    }
}
```

Usage is then pretty simple:

```
@ContextConfiguration(classes = SimpleApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@MockBeans(classes = MyBean.class)
public class SimpleApplicationTest {

    @Autowired
    private MyBean myMock;
...
```