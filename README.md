# Spring-Automocker
Automatic IO mocking tool for Spring's JavaConfig.

Load your entire Production configuration into a JUnit test.

The purpose of this project is to test functionalities of applications on real code without heavy glue code.

Currently the project supports mocking of
* `@PropertySource`
* Spring-web by making available `MockMvc`
* `javax.sql.Datasource` [examples](examples/example-sql/README.md)

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
