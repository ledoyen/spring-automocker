# SQL Examples

### Data-JPA application
Refer to [DataJpaApplicationTest](example-sql/src/test/java/com/github/ledoyen/automocker/examples/DataJpaApplicationTest.java)

### Data-JPA application with multiple datasources
Refer to [MultipleDatasourcesApplication](example-sql/src/test/java/com/github/ledoyen/automocker/examples/MultipleDatasourcesApplication.java)

To access `DataSource` directly, inject it by its Id
```java
@Resource(name="dataSource1")
private DataSource dataSource1;
```
or inject them all
```java
@Autowired
private Map<String, DataSource> dataSourcesByName;
```
