package com.radcns.bird_plus;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.visitor.filter.AbstractFilter;

public class AutoDbMappingGenerater  {
	
	protected static volatile ConcurrentMap<String, TableRecord> tableMemory = new ConcurrentHashMap<>();
	protected static volatile ConcurrentMap<String, List<String>> packageHistory = new ConcurrentHashMap<>();
	protected static final SpoonAPI spoon = new Launcher();
	static {
		CtModel model = spoon.buildModel();
		Environment env = spoon.getEnvironment();
		env.setAutoImports(true);
		env.setNoClasspath(true);
		env.setShouldCompile(true);
		env.setComplianceLevel(14);
	}
	protected Mono<Map<String, Map<String, CtFieldReference<?>>>> entityFilter = Mono.fromCallable(()->{
		return spoon.getFactory().Package().getRootPackage().getElements(new AbstractFilter<CtClass<?>>() {
			@Override
			public boolean matches(CtClass<?> element) {
				return element.getSimpleName().equals("Entity");
			};
		});
	})
	.subscribeOn(Schedulers.boundedElastic())
	.flatMapMany(Flux::fromIterable)
	.collectMap(
			k -> k.getSimpleName(), 
			v -> v.getAllFields().stream().collect(Collectors.toMap(fieldsK -> fieldsK.getSimpleName(), fieldsV -> fieldsV)))
	;
	protected Mono<Map<String, Map<String, CtFieldReference<?>>>> repositoryFilter = Mono.fromCallable(()->{
		return spoon.getFactory().Package().getRootPackage().getElements(new AbstractFilter<CtClass<?>>() {
			@Override
			public boolean matches(CtClass<?> element) {
				return element.getSimpleName().equals("Repository");
			};
		});
	})
	.subscribeOn(Schedulers.boundedElastic())
	.flatMapMany(Flux::fromIterable)
	.collectMap(
			k -> k.getSimpleName(), 
			v -> v.getAllFields().stream().collect(Collectors.toMap(fieldsK -> fieldsK.getSimpleName(), fieldsV -> fieldsV)))
	;
	protected final AutoDbMappingGeneraterOption option;
    
	protected final String defaultRootDirectories = System.getProperty("user.dir") + "\\\\";
	
	public AutoDbMappingGenerater(AutoDbMappingGeneraterOption option) {
		this.option = option;
		createDefaultDirectories();
		spoon.addInputResource(option.defaultRootPath.stream().collect(Collectors.joining("\\\\")));

		/*
		this.dataSource = DataSourceBuilder.create()
				.driverClassName(option.driverClassName.value)
				.url(option.url)
				.username(option.username)
				.password(option.password)
				.type()
				.build();
		*///
		//run();
	}
	public static void main(String a[]) {
		/*
	   	new AutoDbMappingGenerater(AutoDbMappingGeneraterOption.builder()
		   		.url("jdbc:postgresql://kor-zombi-rds-3.cylfrmmsl7kc.ap-northeast-2.rds.amazonaws.com:5432/kor_zombi_database")
		   		.username("kor_zombi_rds")
		   		.password("rlawngud1")
		   		.schema("bird_plus")
		   		.tableNameToEntityStartCharAt(0)
	   			.build()
		   	);
		*/
		System.out.println(System.getProperty("user.dir"));
		System.out.println(AutoDbMappingGenerater.class.getPackageName());



		SpoonAPI spoon = new Launcher();
		spoon.addInputResource("src/main/java/com/radcns/bird_plus");
		spoon.buildModel();
		Mono.fromCallable(()->{
			return spoon.getFactory().Package().getRootPackage().getElements(new AbstractFilter<CtClass<?>>() {
				@Override
				public boolean matches(CtClass<?> element) {
					return element.getSimpleName().endsWith("Entity");
				};
			});
		})
		.flatMapMany(Flux::fromIterable)
		.doOnComplete(()->{
			System.out.println("complate!!");
		})
		.subscribe(e->{
			System.out.println(e.getSimpleName());
		})
		;

		try {
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	private void createDefaultDirectories() {
		Path path = Paths.get(this.defaultRootDirectories + this.option.defaultRootPath.stream().collect(Collectors.joining("\\\\")));
		Path entityPath = Paths.get(
				this.defaultRootDirectories + 
				this.option.defaultRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +
				Character.toUpperCase(this.option.entityName.charAt(0)) + this.option.entityName.substring(1)
				);
		Path repositoryPath = Paths.get(
				this.defaultRootDirectories + 
				this.option.defaultRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +
				Character.toUpperCase(this.option.repositoryName.charAt(0)) + this.option.repositoryName.substring(1)
				);
		try {
			Files.createDirectories(path);
			Files.createDirectories(entityPath);
			Files.createDirectories(repositoryPath);
		} catch (IOException ignore) {}
	}
	
	private String createGroupPackage(String tableName) {
		String firstName = tableName.substring(option.tableNameToEntityStartCharAt).split("_")[0].toLowerCase();
		if(packageHistory.containsKey(tableName)) {
			return firstName;
		}
		Path entityPath =  Paths.get(
				this.defaultRootDirectories + 
				this.option.defaultRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +
				Character.toUpperCase(this.option.entityName.charAt(0)) + this.option.entityName.substring(1) + "\\\\" +
				firstName
				);
		Path repositoryPath =  Paths.get(
				this.defaultRootDirectories + 
				this.option.defaultRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +
				Character.toUpperCase(this.option.repositoryName.charAt(0)) + this.option.repositoryName.substring(1) + "\\\\" +
				firstName
				);
		try {
			Files.createDirectories(entityPath);
			Files.createDirectories(repositoryPath);
		} catch (IOException ignore) {}
		packageHistory.put(tableName, List.of(entityPath.toString(), repositoryPath.toString()));
		return firstName;
	}
	
	private String convertUnderbarToCamelName(String name) {
		return convertUnderbarToCamelName(name, null);
	}
	private String convertUnderbarToCamelName(String name, String tableName) {
		boolean isTable = tableName == null;
		String camelName = isTable ? tableMemory.get(name).camelName() : tableMemory.get(tableName).columnMapper.get(name).camelName();
		if(camelName != null) {
			return camelName;
		}
		AtomicInteger i = new AtomicInteger();
		return Stream.of(name.substring(isTable ? option.tableNameToEntityStartCharAt : 0).split("_")).filter(e-> ! e.isBlank()).map(e->{
			String s = e.trim().toLowerCase();
			
			if(i.getAndIncrement() == 0) {
				return e;
			}
			
			return Character.toUpperCase(s.charAt(0)) + s.substring(1);
		}).collect(Collectors.joining());
	}

    private void run() {
    	try (Connection connection = DriverManager.getConnection(option.url, option.username, option.password)){
        	var databaseMetaData = connection.getMetaData();
        	//var result = databaseMetaData.getTables(connection.getCatalog(), option.schema, null, new String[]{"TABLE"});
        	//while (result.next()) {
	    	//	System.out.println(result.getString(3));
	    	//	
	    	//}
        	var result = databaseMetaData.getColumns(connection.getCatalog(), option.schema, null, null);
        	while (result.next()) {
        		Mono.fromRunnable(() -> {
        			try {
	        			String tableName = result.getString(3);
	            		String columnName = result.getString(4);
	            		TableRecord tableRecord = tableMemory.get(tableName);
	            		ColumnRecord columnRecord;
	            		if(tableRecord == null) {
	            			String camelTableName = convertUnderbarToCamelName(tableName);
	            			String camelColumnName = convertUnderbarToCamelName(columnName, tableName);
	            			Map<String, ColumnRecord> cloumnMapper = new HashMap<>();
	            			columnRecord = new ColumnRecord(columnName, camelColumnName);
	            			cloumnMapper.put(columnName, columnRecord);
	            			tableRecord = new TableRecord(tableName, camelTableName, createGroupPackage(tableName), cloumnMapper);
	            			AutoDbMappingGenerater.tableMemory.put(tableName, tableRecord);
	            		}else if(! tableRecord.columnMapper.containsKey(columnName)) {
	            			String camelColumnName = convertUnderbarToCamelName(columnName, tableName);
	            			columnRecord = new ColumnRecord(columnName, camelColumnName);
	            			tableRecord.columnMapper.put(columnName, columnRecord);
	            		}
        			}catch (SQLException e) {
        				e.printStackTrace();
					}
        		}).subscribeOn(Schedulers.boundedElastic()).subscribe();
	    	}
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private record TableRecord(
    		   String name,
    		   String camelName,
    		   String packagePath,
    		   Map<String, ColumnRecord> columnMapper
    		) {}
    private record ColumnRecord(
    			String name,
    			String camelName
    		) {}
	public static class AutoDbMappingGeneraterOption{
		private final String schema;
		private final String url;
		private final String username;
		private final String password;
		private final Integer tableNameToEntityStartCharAt;
		private final List<String> defaultRootPath;
		private final String entityName;
		private final String repositoryName;
		private final Class<?> entityExtendsClass;
		private final List<Class<?>> entityImplamentList;
		private final Class<?> repositoryExtendsClass;
		private final List<Class<?>> repositoryImplamentList;
		private final Map<String, Map<Class<? extends Annotation>, Map<String, Object>>> defaultEntityFieldAnnotation;
		private final Map<Class<? extends Annotation>, Map<String, Object>> defaultEntityClassAnnotation;

		protected AutoDbMappingGeneraterOption(AutoDbMappingGeneraterOptionBuilder builder){
			schema = builder.schema;
			url = builder.url;
			username = builder.username;
			password = builder.password;
			tableNameToEntityStartCharAt = builder.tableNameToEntityStartCharAt;
			defaultRootPath = builder.defaultRootPath;
			entityName = builder.entityName;
			repositoryName = builder.repositoryName;
			entityExtendsClass = builder.entityExtendsClass;
			entityImplamentList = builder.entityImplamentList;
			repositoryExtendsClass = builder.repositoryExtendsClass;
			repositoryImplamentList = builder.repositoryImplamentList;
			defaultEntityFieldAnnotation = builder.defaultEntityFieldAnnotation;
			defaultEntityClassAnnotation = builder.defaultEntityClassAnnotation;
			
		}
		
		public static AutoDbMappingGeneraterOptionBuilder builder(){
			return new AutoDbMappingGeneraterOptionBuilder();
		}
		protected static class AutoDbMappingGeneraterOptionBuilder{
			private String schema;
			private String url;
			private String username;
			private String password;
			private Integer tableNameToEntityStartCharAt = 0;
			private List<String> defaultRootPath;
			//List.of("src", "main", "java", "com", "radncs", "bird_plus");
			private String entityName = "Entity";
			private String repositoryName = "Repository";
			private Class<?> entityExtendsClass;
			private List<Class<?>> entityImplamentList;
			private Class<?> repositoryExtendsClass;
			private List<Class<?>> repositoryImplamentList;
			private Map<String, Map<Class<? extends Annotation>, Map<String, Object>>> defaultEntityFieldAnnotation = Collections.emptyMap(); /* = Map.of(
						"create_at", Map.of(CreatedDate.class, Collections.emptyMap()),
						"create_by", Map.of(CreatedBy.class, Collections.emptyMap()),
						"update_at", Map.of(LastModifiedDate.class, Collections.emptyMap()),
						"update_by", Map.of(LastModifiedBy.class, Collections.emptyMap()),
						"password", Map.of(JsonProperty.class, Map.of("access", JsonProperty.Access.WRITE_ONLY))
					);*/
			private Map<Class<? extends Annotation>, Map<String, Object>> defaultEntityClassAnnotation = Collections.emptyMap(); /* = Map.of(
						Getter.class, Collections.emptyMap(),
						Setter.class, Collections.emptyMap(),
						Builder.class, Map.of("toBuilder", true),
						NoArgsConstructor.class, Collections.emptyMap(),
						AllArgsConstructor.class, Collections.emptyMap(),
						With.class, Collections.emptyMap(),
						ToString.class, Collections.emptyMap(),
						JsonIgnoreProperties.class, Map.of("ignoreUnknown", true),
						JsonInclude.class, Map.of("value", JsonInclude.Include.NON_NULL)
					);*/
			public AutoDbMappingGeneraterOptionBuilder schema(String schema) {
				this.schema = schema;
				return this;
			}
			
			public AutoDbMappingGeneraterOptionBuilder url(String url) {
				this.url = url;
				return this;
			}

			public AutoDbMappingGeneraterOptionBuilder username(String username) {
				this.username = username;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder password(String password) {
				this.password = password;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder tableNameToEntityStartCharAt(Integer tableNameToEntityStartCharAt) {
				this.tableNameToEntityStartCharAt = tableNameToEntityStartCharAt;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder defaultRootPath(List<String> defaultRootPath) {
				this.defaultRootPath = defaultRootPath;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityName(String entityName) {
				this.entityName = entityName;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder repositoryName(String repositoryName) {
				this.repositoryName = repositoryName;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder defaultEntityFieldAnnotation( Map<String, Map<Class<? extends Annotation>, Map<String, Object>>> defaultEntityFieldAnnotation) {
				this.defaultEntityFieldAnnotation = defaultEntityFieldAnnotation;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder defaultEntityClassAnnotation(Map<Class<? extends Annotation>, Map<String, Object>> defaultEntityClassAnnotation) {
				this.defaultEntityClassAnnotation = defaultEntityClassAnnotation;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityExtendsClass(Class<?> entityExtendsClass) {
				this.entityExtendsClass = entityExtendsClass;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityImplamentList(List<Class<?>> entityImplamentList) {
				this.entityImplamentList = entityImplamentList;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder repositoryExtendsClass(Class<?> repositoryExtendsClass) {
				this.repositoryExtendsClass = repositoryExtendsClass;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder repositoryImplamentList(List<Class<?>> repositoryImplamentList) {
				this.repositoryImplamentList = repositoryImplamentList;
				return this;
			}
			public AutoDbMappingGeneraterOption build() {
				return new AutoDbMappingGeneraterOption(this);
			}
		}
	}
	/*
	public enum DriverClassKind{
		MYSQL("com.mysql.jdbc.Driver"),
		H2("org.h2.Driver"),
		POSTGRE("org.postgresql.Driver"),
		HS("org.hsqldb.jdbcDriver"),
		DERBY("org.apache.derby.jdbc.EmbeddedDriver"),
		DB2("com.ibm.db2.icc.DB2Driver"),
		MS("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
		ORACLE("oracle.jdbc.driver.OracleDriver"),
		OF(""),
		NONE("")
		;
		private String value;
		DriverClassKind(String value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return this.value;
		}
		public static DriverClassKind of(String value) {
			DriverClassKind of = DriverClassKind.OF;
			of.value = value;
			return of;
		}
	}
	*/
}
