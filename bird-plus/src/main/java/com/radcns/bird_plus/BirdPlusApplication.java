package com.radcns.bird_plus;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

import com.radcns.bird_plus.AutoDbMappingGenerater.AutoDbMappingGeneraterOption;

//import com.hide_and_fps.project.entity.Customer;
//import com.hide_and_fps.project.repository.testRepository;

/*
 * 이 클래스의 패키지가 최상위 루트로 지정된다.
 */

//@ServletComponentScan

/*
[추후 리펙토링 필요한 내용들]
 2023 09 11 [FO] - 챗팅 해드처럼 데이터를 가져오는 방식을(SSE) 각 room list에 적용 -> 리프레쉬 비용을 절감할 수 있도록 함
			[FO] - room 생성 후 room에 초대 된 사용자에게 push 할 수 있어야함, 현재는 X(방 만든 사용자가 채팅쳐야 할 것임 (채팅 sse로 동작할 것임)) -> SSE 방식으로 변경 필요
			
*/
//매핑할 패키지 경로를 지정한다. 해당 클래스 외의 패키지 경로를 매핑하고 싶을 때(Autowired bean이 안잡힐 때)
@ComponentScan(basePackages = {
								"com.radcns.bird_plus.*"
							  })
@SpringBootApplication
public class BirdPlusApplication implements ApplicationRunner {
    public static void main(String[] args) {
		/*String profiles = System.getenv("MY_SERVER_PROFILES");
		System.out.println(profiles);
		System.out.println("test<<<<<");
		if(profiles == null) {
			//System.set
		}else {
			//System.setProperty("spring.profiles.active", "prd");
		}*/
		SpringApplication.run(BirdPlusApplication.class, args);		
	}
    
    @Value("spring.datasource.url")
    String url;
    
    @Value("spring.r2dbc.username")
    String username;
    
    @Value("spring.r2dbc.password")
    String password;
    
    @Value("spring.r2dbc.properties.schema")
    String schema;
    
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
	   	System.out.println("kjh test afterPropertiesSet");
	   	new AutoDbMappingGenerater(AutoDbMappingGeneraterOption.builder()
	   		.url(url)
	   		.username(username)
	   		.password(password)
	   		.schema(schema)
   			.build()
	   	);
	}

    /**
     * Spring Data R2dbc 는 연결될 때 데이터베이스에서 SQL 스크립트를 실행할 수 있도록 
     * ConnectionFactoryInitializer를 제공합니다 . 
     * Spring Boot 애플리케이션에서는 자동으로 구성됩니다. 응용 프로그램이 시작되면 스키마를 스캔합니다.
     **/
    /*
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        return initializer;
    }
    */
    /**
     * CommandLineRunner는 Bean이 SpringApplication에 포함될 때 실행되어야 함을 나타내는 데 사용되는 인터페이스입니다. 
     * 스프링 부트 응용 프로그램에는 CommandLineRunner를 구현하는 여러 개의 빈이 있을 수 있습니다. 
     * 이것들은 @Order와 함께 주문할 수 있습니다.
     **/
    /*
    @Bean
    public CommandLineRunner demo(testRepository repository) {
        return (args) -> {
            // save a few customers
            repository.saveAll(Arrays.asList(new Customer("Jack", "Bauer"),
                new Customer("Chloe", "O'Brian"),
                new Customer("Kim", "Bauer"),
                new Customer("David", "Palmer"),
                new Customer("Michelle", "Dessler")))
                .blockLast(Duration.ofSeconds(10));
            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            repository.findAll().doOnNext(customer -> {
                log.info("customer All >>>" + customer.toString());
            }).blockLast(Duration.ofSeconds(10));
            log.info("");
            // fetch an individual customer by ID
			repository.findById(1L).doOnNext(customer -> {
				log.info("Customer found with findById(1L):");
				log.info("--------------------------------");
				log.info("customer ById 1L?>>>" + customer.toString());
				log.info("");
			}).block(Duration.ofSeconds(10));
            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Bauer").doOnNext(bauer -> {
                log.info("bauer >>> " + bauer.toString());
            }).blockLast(Duration.ofSeconds(10));;
            log.info("");
        };
    }
    */
}
