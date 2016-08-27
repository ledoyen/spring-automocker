# Spring-Automocker
Automatic IO mocking tool for Spring's JavaConfig.

Load your entire Production configuration into a JUnit test.

The purpose of this project is to test functionalities of applications on real code without heavy glue code.

Currently the project supports mocking of
* `@PropertySource`
* __Spring-web__ [example](examples/example-mvc/src/test/java/com/github/ledoyen/automocker/examples/MvcApplicationTest.java)
* __JDBC__ [examples](examples/example-sql/README.md)
* __JMS__ [example](examples/example-jms/src/test/java/com/github/ledoyen/automocker/examples/JmsApplicationTest.java)

Use `SpringAutomockerJUnit4ClassRunner` in conjunction with `@SpringAutomocker`
```java
@RunWith(SpringAutomockerJUnit4ClassRunner.class)
@ContextConfiguration(classes = MyApplication.class)
@SpringAutomocker
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
* No **spring-boot** `EmbeddedWebApplicationContext` is used, so embedded web-server, if any, will not start
* If any bean annotated with **spring-context** `@Controller` is present, **spring-test** `MockMvc` is configured and added to the test context

### JDBC
All beans implementing `javax.sql.DataSource` are replaced with distinct `org.h2.jdbcx.JdbcDataSource`

### JMS
* All beans implementing `javax.jms.ConnectionFactory` are replaced by a **mockrunner** `MockConnectionFactory`. Each factory is built with its own `DestinationManager`
* If any bean implementing `javax.jms.ConnectionFactory` is present, a custom `JmsMock` is configured and added to the test context
* If any bean implementing **spring-jms** `JmsListenerContainerFactory` is present, a wrapper is set around its `ErrorHandler` and can be accessed through `JmsMock`
