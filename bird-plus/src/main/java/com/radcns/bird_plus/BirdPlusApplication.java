package com.radcns.bird_plus;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.radcns.bird_plus.AutoDbMappingGenerater.AutoDbMappingGeneraterOption;
import com.radcns.bird_plus.AutoDbMappingGenerater.ColumnEntry;
import com.radcns.bird_plus.AutoDbMappingGenerater.UnderType;
import com.radcns.bird_plus.config.security.Role;
import com.radcns.bird_plus.entity.room.constant.RoomType;

import io.r2dbc.postgresql.codec.Json;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import spoon.Launcher;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

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

		SpringApplication.run(BirdPlusApplication.class, args);

	}
    
    @Value("${spring.datasource.url}")
    String url;
    
    @Value("${spring.r2dbc.username}")
    String username;
    
    @Value("${spring.r2dbc.password}")
    String password;
    
    @Value("${spring.r2dbc.properties.schema}")
    String schema;
    private static AutoDbMappingGenerater autoDbMappingGenerater = null;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		String profiles = System.getenv("MY_SERVER_PROFILES");
		if( (profiles != null &&! profiles.equals("local")) || autoDbMappingGenerater != null || true) {
			return;
		}
		autoDbMappingGenerater =
    	new AutoDbMappingGenerater(AutoDbMappingGeneraterOption.builder()
		   		.url(url)
		   		.username(username)
		   		.password(password)
		   		.schema(schema)
		   		.tableNameToEntityStartCharAt(2)
		   		.defaultRootPath( List.of("src", "main", "java") )
		   		.defaultPackageRootPath( List.of("com", "radcns", "bird_plus") )
		   		.entityClassLastName("Entity")
		   		.entityClassFieldColumnAnnotationType(Column.class)
		   		/*.entityClassFieldDefaultAnnotationType(Map.of(
		   			Getter.class, Collections.emptyMap()
		   		))*/
		   		.entityClassSpecificFieldAnnotation(Map.of(
					"create_at", Map.of(CreatedDate.class, Collections.emptyMap()),
					"create_by", Map.of(CreatedBy.class, Collections.emptyMap()),
					"update_at", Map.of(LastModifiedDate.class, Collections.emptyMap()),
					"update_by", Map.of(LastModifiedBy.class, Collections.emptyMap()),
					"password", Map.of(JsonProperty.class, Map.of("access", JsonProperty.Access.WRITE_ONLY))
				))
		   		.entityClassTableAnnotationType(Table.class)
		   		.entityClassDefaultAnnotation(Map.of(
					Getter.class, Collections.emptyMap(),
					Setter.class, Collections.emptyMap(),
					Builder.class, Map.of("toBuilder", true),
					NoArgsConstructor.class, Collections.emptyMap(),
					AllArgsConstructor.class, Collections.emptyMap(),
					With.class, Collections.emptyMap(),
					ToString.class, Collections.emptyMap(),
					JsonIgnoreProperties.class, Map.of("ignoreUnknown", true),
					JsonInclude.class, Map.of("value", JsonInclude.Include.NON_NULL)
				))
		   		.repositoryClassLastName("Repository")
		   		.repositoryPkClass(Long.class)
	   			.repositoryExtendsClass(ReactiveCrudRepository.class)
	   			.columnTypeMapper(Map.ofEntries(
	   				ColumnEntry.pair("int2", Long.class),
	   				ColumnEntry.pair("int4", Long.class),
	   				ColumnEntry.pair("int6", Long.class),
	   				ColumnEntry.pair("int8", Long.class),
	   				ColumnEntry.pair("_int2", new UnderType<List<Long>>() {}),
	   				ColumnEntry.pair("_int4", new UnderType<List<Long>>() {}),
	   				ColumnEntry.pair("_int6", new UnderType<List<Long>>() {}),
	   				ColumnEntry.pair("_int8", new UnderType<List<Long>>() {}),
	   				ColumnEntry.pair("serial", new UnderType<List<Long>>() {}),
	   				ColumnEntry.pair("bigserial", new UnderType<List<Long>>() {}),
	   				ColumnEntry.pair("timestamp", LocalDateTime.class),
	   				ColumnEntry.pair("varchar", String.class),
	   				ColumnEntry.pair("_varchar", new UnderType<List<String>>() {}),
	   				ColumnEntry.pair("bool", Boolean.class),
	   				ColumnEntry.pair("jsonb", Json.class),
	   				ColumnEntry.pair("json", Json.class)
	   			))
	   			.columnSpecificTypeMapper(Map.ofEntries(
	   				ColumnEntry.pair("roles", new UnderType<List<Role>>() {}),
	   				ColumnEntry.pair("room_type", RoomType.class)
	   			))
	   			.entityCeateAfterCallBack((ctClass, factory) -> {
	   				// create another default entity of field and method...
	   				
	   				Function<String, CtField<Long>> milsFun = ((name) -> {
	   					CtTypeReference<Long> createMilsRef = factory.Code().createCtTypeReference(Long.class);
		   				CtField<Long> milsField = factory.Core().<Long>createField();
		   				CtAnnotationType<Transient> transientType = (CtAnnotationType<Transient>) factory.Type().<Transient>get(Transient.class); 
		   				CtAnnotation<Annotation> transientAnnotation = factory.Core().createAnnotation();
		   				transientAnnotation.setAnnotationType(transientType.getReference());
		   				milsField.addAnnotation(transientAnnotation);
		   				milsField.setSimpleName(name);
		   				milsField.addModifier(ModifierKind.PRIVATE);
		   				milsField.setType(createMilsRef);
		   				
		   				return milsField;
	   				});
	   				if(ctClass.getField("createMils") == null) {
	   					ctClass.addField(milsFun.apply("createMils"));
	   				}
	   				if(ctClass.getField("updateMils") == null) {
	   					ctClass.addField(milsFun.apply("updateMils"));
	   				}	
	   				
	   				Set<CtMethod<?>> methods = Launcher.parseClass("""
	   						import java.time.ZoneId;
	   						import java.time.LocalDateTime;
	   			    		class %s{
	   			    		public void setCreateAt(LocalDateTime createAt) {
								this.createAt = createAt;
								this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
							}
							public void setUpdateAt(LocalDateTime updateAt) {
								this.updateAt = updateAt;
								this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
							}
							public Long getCreateMils() {
								if(this.createAt == null) {
									return null;
								}else if(this.createMils == null) {
									this.createMils = createAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
									
								}
								return this.createMils; 
							}
							public Long getUpdateMils() {
								if(this.updateAt == null) {
									return null;
								}else if(this.updateMils == null) {
									this.updateMils = updateAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
								}
								return this.updateMils; 
							}
	   			    		""".formatted(ctClass.getSimpleName())).getMethods();

	   				methods.forEach(e->{
	   					if(ctClass.getMethodsByName(e.getSimpleName()).size() != 0) return;
	   					CtMethod<?> m = e.clone();
	   					ctClass.addMethod(m);
	   				});
	   				
	   			})
	   			.isTest(false)
	   			//.intervalOfMinutes(5)
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
