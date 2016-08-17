# spring-automocker
Automatic IO mocking tool for Spring's JavaConfig

Load your entire Production configuration into a JUnit test.

The purpose of this project is to find out what IO your project is using and mock them without any additional glue code.
Currently the project supports mocking of
* `@PropertySource`
* Spring-web by making available `MockMvc`
* `javax.sql.Datasource` [examples](examples/example-sql/README.md)
