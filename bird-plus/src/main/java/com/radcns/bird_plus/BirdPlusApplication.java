package com.radcns.bird_plus;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

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

@ComponentScan(basePackages = {
								"com.radcns.bird_plus.*"
							  })
@SpringBootApplication
public class BirdPlusApplication implements ApplicationRunner 
{
    public static void main(String[] args) {
    	System.setProperty("jasypt.encryptor.password", System.getenv("MY_SERVER_PASSWORD"));
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
		if( (profiles != null &&! profiles.equals("local")) || autoDbMappingGenerater != null) {
			return;
		}
		autoDbMappingGenerater =
    	new AutoDbMappingGenerater(AutoDbMappingGeneraterOption.builder()
		   		.url(url)
		   		.username(username)
		   		.password(password)
		   		.schema(schema)
		   		.isTest(false) // 테스트 여부, true인 경우 가장 먼저 조회되는 테이블 한 건만을 대상으로 엔티티와 레포지토리 클래스를 생성
		   		.tableNameToEntityStartCharAt(2) // ex) 0으로 설정시 == SY_FILE_ICON => SyFileIconEntity로 엔티티 생성/ 2 또는 3으로 설정시 == SY_FILE_ICON => FileIconEntity로 생성됨
		   		.defaultRootPath( List.of("src", "main", "java") ) // java 경로
		   		.defaultPackageRootPath( List.of("com", "radcns", "bird_plus") ) // 프로젝트 루트 경로, 이 경우 entity 패키지는 com.radcns.bird_plus.entity로 생성 됨
		   		.entityClassLastName("Entity") // 끝에 붙을 식별 이름 ex) SY_FILE_ICON => FileIcon{끝에 붙을 식별 이름} => FileIconEntity
		   		.entityClassFieldColumnAnnotationType(Column.class) // 필드에 붙을 컬럼 어노테이션 ex) private UUID id => @Column private UUID id;
		   		.entityClassFieldPkAnnotationType(Id.class) // PK인 필드에 붙을 어노테이션, 단 pk가 2개 이상이거나 없는 경우 스킵하도록 되어 있음 
		   		/*.entityClassFieldDefaultAnnotationType(Map.of( //그 외 모든 필드에 필수로 들어갈 어노테이션
		   			Getter.class, Collections.emptyMap()
		   		))*/
		   		.entityClassSpecificFieldAnnotation(Map.of( // 그 외 특정 필드에만 붙어야 하는 어노테이션 설정 (key = 컬럼명, value = 어노테이션 클래스 및 어노테이션에 들어갈 값)
					"create_at", Map.of(CreatedDate.class, Collections.emptyMap()),
					"create_by", Map.of(CreatedBy.class, Collections.emptyMap()),
					"update_at", Map.of(LastModifiedDate.class, Collections.emptyMap()),
					"update_by", Map.of(LastModifiedBy.class, Collections.emptyMap()),
					"password", Map.of(JsonProperty.class, Map.of("access", JsonProperty.Access.WRITE_ONLY))
				))
		   		.entityClassTableAnnotationType(Table.class) // Entity 클래스에 붙어야 할 기본 어노테이션
		   		.entityClassDefaultAnnotation(Map.of( // 그외 Entity 클래스에 별도로 더 붙이고 싶은 기본 어노테이션 설정
					Getter.class, Collections.emptyMap(),
					Setter.class, Collections.emptyMap(),
					Builder.class, Map.of("toBuilder", true),
					NoArgsConstructor.class, Collections.emptyMap(),
					AllArgsConstructor.class, Collections.emptyMap(),
					With.class, Collections.emptyMap(),
					ToString.class, Collections.emptyMap()
					//JsonIgnoreProperties.class, Map.of("ignoreUnknown", true),
					//JsonInclude.class, Map.of("value", JsonInclude.Include.NON_NULL)
				))
		   		// repository 클래스 생성시 pk가 없는 경우 CrudRepository에 제너릭이 없는 형태로 클래스 파일이 생성 되며 레포지토리 관련 옵션은 이미 파일이 존재하는 경우 대부분 적용되지 않습니다.
		   		.repositoryClassLastName("Repository") // 끝에 붙을 식별 이름 ex) SY_FILE_ICON => FileIcon{끝에 붙을 식별 이름} => FileIconRepository
	   			.repositoryExtendsClass(ReactiveCrudRepository.class) // 레포지토리가 상속받을 클래스
	   			/*.repositorySpecificPkClass(Map.of( // 특정 레포지토리의 제너릭 pk 클래스를 설정 (key = 테이블 명, value = pk에 설정할 자바 클래스) 파일이 이미 존재하는 경우 이 옵션은 동작 X
	   				"sy_file_icon", UUID.class,
	   				"sy_file", new UnderType<List<String>>() {}
	   			))*/
	   			.columnTypeMapper(Map.ofEntries( //db의 컬럼 데이터 타입과 매칭 될 자바 클래스 정의, 현재 설정은 postgresql 기준
		   				ColumnEntry.pair("int", Long.class),
		   				ColumnEntry.pair("int2", Long.class),
		   				ColumnEntry.pair("int4", Long.class),
		   				ColumnEntry.pair("int6", Long.class),
		   				ColumnEntry.pair("int8", Long.class),
		   				ColumnEntry.pair("bigint", Long.class),
		   				ColumnEntry.pair("serial", Long.class),
		   				ColumnEntry.pair("serial2", Long.class),
		   				ColumnEntry.pair("serial4", Long.class),
		   				ColumnEntry.pair("serial6", Long.class),
		   				ColumnEntry.pair("serial8", Long.class),
		   				ColumnEntry.pair("bigserial", Long.class),
		   				ColumnEntry.pair("_int", new UnderType<List<Long>>() {}),
		   				ColumnEntry.pair("_int2", new UnderType<List<Long>>() {}),
		   				ColumnEntry.pair("_int4", new UnderType<List<Long>>() {}),
		   				ColumnEntry.pair("_int6", new UnderType<List<Long>>() {}),
		   				ColumnEntry.pair("_int8", new UnderType<List<Long>>() {}),
		   				ColumnEntry.pair("_bigint", new UnderType<List<Long>>() {}),
		   				ColumnEntry.pair("timestamp", LocalDateTime.class),
		   				ColumnEntry.pair("varchar", String.class),
		   				ColumnEntry.pair("_varchar", new UnderType<List<String>>() {}),
		   				ColumnEntry.pair("bool", Boolean.class),
		   				ColumnEntry.pair("jsonb", Json.class),
		   				ColumnEntry.pair("json", Json.class)
	   			))
	   			.columnSpecificTypeMapper(Map.ofEntries( // 그 외 db 컬럼의 데이터 타입과 무관하게 특정 컬럼에만 별도로 매칭 될 자바 클래스 정의 (해당 설정이 columnTypeMapper 설정 보다 우선순위)
	   				ColumnEntry.pair("roles", new UnderType<List<Role>>() {}),
	   				ColumnEntry.pair("room_type", RoomType.class)
	   			))
	   			.repositoryCeateAfterCallBack((ctInterface, factory) -> {}) // 인터페이스 클래스 생성 후 별도로 콜백을 통해 추가하고 싶은 메소드가 있는 경우 콜백 함수 설정
	   			.entityCeateAfterCallBack((ctClass, factory) -> { // 엔티티 클래스를 생성 후 별도로 콜백을 통해 추가하고 싶은 메소드가 있는 경우 콜백 함수 설정
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
