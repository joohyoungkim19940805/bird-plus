package com.radcns.bird_plus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
@Configuration
@EnableR2dbcAuditing
public class DBConfig {
}
/*
@Configuration
public class DBConfig {

	String host;
	
	String porotocol;
	
	String port;
	
	String driver;
	
	String database;
    
    @Value("${spring.r2dbc.username}")
    String username;
    
    @Value("${spring.r2dbc.password}")
    String password;
    
    @Value("${spring.r2dbc.properties.schema}")
    String schema;
	@Bean
    public ConnectionFactory connectionFactory() {
		PostgresqlConnectionConfiguration configuration = PostgresqlConnectionConfiguration
	        .builder()
	        .username(username)
	        .password(password)
	        .host(host)
	        .port(Integer.valueOf(port))
	        .database(database)
	        .schema(schema)
        .build();
		ConnectionFactory factory = new PostgresqlConnectionFactory(configuration);
        
        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder(factory)
                .maxSize(101)
                .initialSize(50)
                
                .build();
        //new PostgresqlConnectionConfiguration();
        return factory;
    }

    @Bean
    TransactionAwareConnectionFactoryProxy transactionAwareConnectionFactoryProxy(ConnectionFactory connectionFactory) {
        return new TransactionAwareConnectionFactoryProxy(connectionFactory);
    }

    @Bean
    DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                //.bindMarkers(() -> BindMarkersFactory.named(":", "", 20).create())
                .namedParameters(true)
                .build();
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }
}
*/

