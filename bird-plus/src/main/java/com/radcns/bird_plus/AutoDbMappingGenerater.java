package com.radcns.bird_plus;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.radcns.bird_plus.AutoDbMappingGenerater.UnderType.UnderTypeRecord;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.AbstractFilter;
import spoon.support.JavaOutputProcessor;

public class AutoDbMappingGenerater  {
	
	protected static volatile ConcurrentMap<String, TableRecord> tableMemory = new ConcurrentHashMap<>();
	protected static volatile ConcurrentMap<String, Path[]> packageHistory = new ConcurrentHashMap<>();
	protected final Launcher spoon = new Launcher();
	protected final JavaOutputProcessor javaOutputProcessor = new JavaOutputProcessor();
	protected final Mono<Map<String, Map<String, CtFieldReference<?>>>> entityFilter; 
	protected final Mono<Map<String, Map<String, CtFieldReference<?>>>> repositoryFilter; 
	protected final AutoDbMappingGeneraterOption option;
    
	protected final String defaultRootDirectories = System.getProperty("user.dir") + "\\\\";
	
	public AutoDbMappingGenerater(AutoDbMappingGeneraterOption option) {
		this.option = option;
		System.out.println(option.defaultRootPath.stream().collect(Collectors.joining("\\\\")));
		spoon.addInputResource(option.defaultRootPath.stream().collect(Collectors.joining("\\\\")));
		Environment env = spoon.getEnvironment();
		env.setAutoImports(true);
		env.setNoClasspath(true);
		env.setShouldCompile(true);
		env.setComplianceLevel(14);
		CtModel model = spoon.buildModel();
		javaOutputProcessor.setFactory(spoon.getFactory());

		System.out.println(
				Stream.of(
						spoon.getEnvironment().getSourceOutputDirectory().getPath().split("\\\\"), option.defaultRootPath.toArray(String[]::new)
					)
					.flatMap(e->Stream.of(e))
					.filter(e-> ! e.equals("spooned")).collect(Collectors.joining("\\\\"))
				);
		spoon.getEnvironment().setSourceOutputDirectory(
			new File(
				Stream.of(
					spoon.getEnvironment().getSourceOutputDirectory().getPath().split("\\\\"), option.defaultRootPath.toArray(String[]::new)
				)
				.flatMap(e->Stream.of(e))
				.filter(e-> ! e.equals("spooned")).collect(Collectors.joining("\\\\"))
			)
		);
		
		createDefaultDirectories();

		this.entityFilter = Mono.fromCallable(()->{
			return spoon.getFactory().Package().getRootPackage().getElements(new AbstractFilter<CtClass<?>>() {
				@Override
				public boolean matches(CtClass<?> element) {
					return element.getSimpleName().endsWith("Entity");
				};
			});
		})
		.subscribeOn(Schedulers.boundedElastic())
		.flatMapMany(Flux::fromIterable)
		.collectMap(
			k -> k.getSimpleName(), 
			v -> v.getAllFields().stream().collect(Collectors.toMap(fieldsK -> fieldsK.getSimpleName(), fieldsV -> fieldsV))
		)
		;

		this.repositoryFilter = Mono.fromCallable(()->{
			return spoon.getFactory().Package().getRootPackage().getElements(new AbstractFilter<CtInterface<?>>() {
				@Override
				public boolean matches(CtInterface<?> element) {
					return element.getSimpleName().endsWith("Repository");
				};
			});
		})
		.subscribeOn(Schedulers.boundedElastic())
		.flatMapMany(Flux::fromIterable)
		.collectMap(
				k -> k.getSimpleName(), 
				v -> v.getAllFields().stream().collect(Collectors.toMap(fieldsK -> fieldsK.getSimpleName(), fieldsV -> fieldsV))
		)
		;
		AtomicInteger i = new AtomicInteger();
		entityFilter.doOnNext(entityMap->{
			System.out.println(entityMap);
			repositoryFilter.doOnNext(repository->{
				System.out.println(repository);
				scanningDB().doOnNext(tableRecord->{
					if(i.get() == 0) {
						CtClass<?> entity = createEntity(tableRecord);
						
						tableRecord.columnMapper.entrySet().stream().map(e -> {
							CtField<?> field = createField(e.getValue());
							
							return null;
						});
					}
					i.getAndIncrement();
				}).subscribe();
			}).subscribe();
		}).subscribe();
		
		
		
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
		
	   	new AutoDbMappingGenerater(AutoDbMappingGeneraterOption.builder()
		   		.url("jdbc:postgresql://kor-zombi-rds-3.cylfrmmsl7kc.ap-northeast-2.rds.amazonaws.com:5432/kor_zombi_database")
		   		.username("kor_zombi_rds")
		   		.password("rlawngud1")
		   		.schema("bird_plus")
		   		.tableNameToEntityStartCharAt(0)
		   		.defaultRootPath( List.of("src", "main", "java", "com", "radcns", "bird_plus") )
		   		.entityClassLastName("Entity")
		   		.repositoryClassLastName("Repository")
	   			.build()
		   	);

	}
	private void createDefaultDirectories() {
		Path path = Paths.get(this.defaultRootDirectories + this.option.defaultRootPath.stream().collect(Collectors.joining("\\\\")));
		Path entityPath = Paths.get(
				this.defaultRootDirectories + 
				this.option.defaultRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +
				this.option.defaultPackageRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +		
				Character.toLowerCase(this.option.entityClassLastName.charAt(0)) + this.option.entityClassLastName.substring(1)
				);
		Path repositoryPath = Paths.get(
				this.defaultRootDirectories + 
				this.option.defaultRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +
				this.option.defaultPackageRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +		
				Character.toLowerCase(this.option.repositoryClassLastName.charAt(0)) + this.option.repositoryClassLastName.substring(1)
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
				this.option.defaultPackageRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +		
				Character.toLowerCase(this.option.entityClassLastName.charAt(0)) + this.option.entityClassLastName.substring(1) + "\\\\" +
				firstName
				);
		Path repositoryPath =  Paths.get(
				this.defaultRootDirectories + 
				this.option.defaultRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +
				this.option.defaultPackageRootPath.stream().collect(Collectors.joining("\\\\")) + "\\\\" +		
				Character.toLowerCase(this.option.repositoryClassLastName.charAt(0)) + this.option.repositoryClassLastName.substring(1) + "\\\\" +
				firstName
				);
		/*try {
			Files.createDirectories(entityPath);
			Files.createDirectories(repositoryPath);
		} catch (IOException ignore) {}
		*/
		packageHistory.put(tableName, new Path[] {entityPath, repositoryPath});
		return firstName;
	}
	
	private String convertUnderbarToCamelName(String name) {
		return convertUnderbarToCamelName(name, null);
	}
	private String convertUnderbarToCamelName(String name, String tableName) {
		boolean isTable = tableName == null;
		TableRecord tableRecord = isTable ? tableMemory.get(name) : tableMemory.get(tableName);
		String camelName;
		if(tableRecord == null) {
			camelName = null;
		}else if(isTable){
			camelName = tableRecord.camelName();
		}else {
			ColumnRecord columnRecord = tableRecord.columnMapper.get(name);
			camelName = columnRecord != null ? columnRecord.camelName : null;
		}

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
	private Flux<TableRecord> scanningDB() {
		return Mono.fromCallable(() -> {
			try (Connection connection = DriverManager.getConnection(option.url, option.username, option.password)){
	        	var databaseMetaData = connection.getMetaData();
	        	var result = databaseMetaData.getColumns(connection.getCatalog(), option.schema, null, null);
	        	List<Mono<TableRecord>> list = new ArrayList<>();
	        	while (result.next()) {
        			String tableName = result.getString(3);
            		String columnName = result.getString(4);
					String dataTypeName = result.getString(6);
	        		Mono<TableRecord> mono = Mono.fromCallable(() -> {
	            		TableRecord tableRecord = tableMemory.get(tableName);
	            		ColumnRecord columnRecord = null;
	            		if(tableRecord == null) {
	            			String camelTableName = convertUnderbarToCamelName(tableName);
	            			String camelColumnName = convertUnderbarToCamelName(columnName, tableName);
	            			Map<String, ColumnRecord> cloumnMapper = new HashMap<>();
	            			columnRecord = new ColumnRecord(columnName, camelColumnName, dataTypeName);
	            			cloumnMapper.put(columnName, columnRecord);
	            			tableRecord = new TableRecord(tableName, camelTableName, createGroupPackage(tableName), cloumnMapper);
	            			tableMemory.put(tableName, tableRecord);
	            		}else if(! tableRecord.columnMapper.containsKey(columnName)) {
	            			String camelColumnName = convertUnderbarToCamelName(columnName, tableName);
	            			columnRecord = new ColumnRecord(columnName, camelColumnName, dataTypeName);
	            			tableRecord.columnMapper.put(columnName, columnRecord);
	            		}
	            		return tableRecord;
	        		});
	        		list.add(mono);
		    	}
	        	return list;
	        } catch (SQLException e) {
				// TODO Auto-generated catch block
	        	e.printStackTrace();
	        	return null;
			}
		}).flatMapMany(Flux::fromIterable).flatMap(e->e);
	}
	
	private CtClass<?> createEntity(TableRecord tableRecord) {
		Path packagePath = packageHistory.get(tableRecord.name)[0];
		String packageNames = 
				Stream.of(
						option.defaultPackageRootPath.stream(), 
						Stream.of(packagePath.getParent().getFileName().toString(), packagePath.getFileName().toString())
					).flatMap(e->e).collect(Collectors.joining("."))
					+ ".";
		
		/*
		var clazz = spoon.getFactory().Class().create(packageNames + tableRecord.camelName + option.entityClassLastName);
		
		CtField<?> testField = spoon.getFactory().Core().createField();
		
		CtTypeReference listType = spoon.getFactory().Code().createCtTypeReference(List.class);
		CtTypeReference mapType = spoon.getFactory().Code().createCtTypeReference(Map.class);
		CtTypeReference stringType = spoon.getFactory().Code().createCtTypeReference(String.class);
		
		mapType.setActualTypeArguments((List<? extends CtTypeReference<?>>) List.of(stringType, stringType.clone()));
		listType.setActualTypeArguments((List<? extends CtTypeReference<?>>) List.of(mapType));
		
		testField.setType(listType);
		testField.setSimpleName("test");
		testField.addModifier(ModifierKind.PRIVATE);
		clazz.addField(testField);
		
		javaOutputProcessor.createJavaFile(clazz);
		*/
		
		return spoon.getFactory().Class().create(packageNames + tableRecord.camelName + option.entityClassLastName);
	}
	private CtField<?> createField(ColumnRecord columnRecord){
		CtField<?> field = spoon.getFactory().Core().createField();
		Object objType = option.columnColumnEntry.get(columnRecord.name);
		CtTypeReference<?> type;
		if(objType == null) {
			type = spoon.getFactory().Code().createCtTypeReference(Object.class);
		}else if(objType.getClass().equals(UnderType.class)) {
			UnderType<?> underType = UnderType.class.cast(objType);
			type = spoon.getFactory().Code().createCtTypeReference(underType.getTopClass());
			var list = underType.getClassPath();
			CtTypeReference<?> prevType = type;
			for(int i = 1, len = list.size() ; i < len ; i += 1) {
				UnderTypeRecord underTypeRecord = list.get(i);
				CtTypeReference<?> genericType = spoon.getFactory().Code().createCtTypeReference(underTypeRecord .clazz());
				genericType.setActualTypeArguments(
					underTypeRecord.childList().stream()
					.map(e-> spoon.getFactory().Code().createCtTypeReference(e.clazz()))
					.toList()
				);
				prevType.setActualTypeArguments(List.of(genericType));
				prevType = genericType;
			}
		}else{
			type = spoon.getFactory().Code().createCtTypeReference((Class<?>)objType);
		}
		field.setType(type);
		
		return field;
	}
	
	private CtAnnotation<?> createAnnotation(Class<?> targetClass) {
		return createAnnotation(targetClass, null);
	}
	
	private CtAnnotation<?> createAnnotation(Class<?> targetClass, Map<String, Object> targetValues) {
		CtAnnotationType<?> annotationType = (CtAnnotationType<?>) spoon.getFactory().Type().get(targetClass);
		CtAnnotation<Annotation> annotation = spoon.getFactory().Core().createAnnotation();
		
		annotation.setAnnotationType(annotationType.getReference());
		
		if(targetValues != null) {
			annotation.setElementValues(targetValues);
		}
		
		return annotation;
	}
	
	private void output(CtClass clazz) {
		javaOutputProcessor.createJavaFile(clazz);
	}
	
    private void run() {
    	
    }

    private record TableRecord(
    		   String name,
    		   String camelName,
    		   String packagePath,
    		   Map<String, ColumnRecord> columnMapper
    		) {}
    private record ColumnRecord(
    			String name,
    			String camelName,
    			String dataTypeName
    		) {}
	public static class AutoDbMappingGeneraterOption{
		private final String schema;
		private final String url;
		private final String username;
		private final String password;
		private final Integer tableNameToEntityStartCharAt;
		private final List<String> defaultRootPath;
		private final List<String> defaultPackageRootPath;
		private final String entityClassLastName;
		private final String repositoryClassLastName;
		private final Class<?> entityExtendsClass;
		private final Class<?> repositoryExtendsClass;
		private final Class<?> repositoryPkClass;
		private final Map<String, Map<Class<? extends Annotation>, Map<String, Object>>> entityClassSpecificFieldAnnotation;
		private final Class<?> entityClassFieldColumnAnnotationType;
		private final Map<Class<? extends Annotation>, Map<String, Object>> entityClassDefaultAnnotation;
		private final Class<?> entityClassTableAnnotationType;
		private final Map<String, ?> columnColumnEntry;
		protected AutoDbMappingGeneraterOption(AutoDbMappingGeneraterOptionBuilder builder){
			schema = builder.schema;
			url = builder.url;
			username = builder.username;
			password = builder.password;
			tableNameToEntityStartCharAt = builder.tableNameToEntityStartCharAt;
			defaultRootPath = builder.defaultRootPath;
			defaultPackageRootPath = builder.defaultPackageRootPath;
			entityClassLastName = builder.entityClassLastName;
			repositoryClassLastName = builder.repositoryClassLastName;
			entityExtendsClass = builder.entityExtendsClass;
			repositoryExtendsClass = builder.repositoryExtendsClass;
			repositoryPkClass = builder.repositoryPkClass;
			entityClassSpecificFieldAnnotation = builder.entityClassSpecificFieldAnnotation;
			entityClassFieldColumnAnnotationType = builder.entityClassFieldColumnAnnotationType;
			entityClassDefaultAnnotation = builder.entityClassDefaultAnnotation;
			entityClassTableAnnotationType = builder.entityClassTableAnnotationType;
			columnColumnEntry = builder.columnColumnEntry;
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
			private List<String> defaultPackageRootPath;
			private String entityClassLastName = "Entity";
			private String repositoryClassLastName = "Repository";
			private Class<?> entityExtendsClass;
			private Class<?> repositoryExtendsClass;
			private Class<?> repositoryPkClass = Long.class;
			private Map<String, Map<Class<? extends Annotation>, Map<String, Object>>> entityClassSpecificFieldAnnotation = Collections.emptyMap();
			private Class<?> entityClassFieldColumnAnnotationType;
			private Map<Class<? extends Annotation>, Map<String, Object>> entityClassDefaultAnnotation = Collections.emptyMap();
			private Class<?> entityClassTableAnnotationType;
			private Map<String, ?> columnColumnEntry;
		
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
			public AutoDbMappingGeneraterOptionBuilder defaultPackageRootPath(List<String> defaultPackageRootPath) {
				this.defaultPackageRootPath = defaultPackageRootPath;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityClassLastName(String entityClassLastName) {
				this.entityClassLastName = entityClassLastName;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder repositoryClassLastName(String repositoryClassLastName) {
				this.repositoryClassLastName = repositoryClassLastName;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityClassSpecificFieldAnnotation( Map<String, Map<Class<? extends Annotation>, Map<String, Object>>> entityClassSpecificFieldAnnotation) {
				this.entityClassSpecificFieldAnnotation = entityClassSpecificFieldAnnotation;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityClassDefaultAnnotation(Map<Class<? extends Annotation>, Map<String, Object>> entityClassDefaultAnnotation) {
				this.entityClassDefaultAnnotation = entityClassDefaultAnnotation;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityExtendsClass(Class<?> entityExtendsClass) {
				this.entityExtendsClass = entityExtendsClass;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder repositoryExtendsClass(Class<?> repositoryExtendsClass) {
				this.repositoryExtendsClass = repositoryExtendsClass;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder repositoryPkClass(Class<?> repositoryPkClass) {
				this.repositoryPkClass = repositoryPkClass;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityClassFieldColumnAnnotationType(Class<?> entityClassFieldColumnAnnotationType) {
				this.entityClassFieldColumnAnnotationType = entityClassFieldColumnAnnotationType;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityClassTableAnnotationType(Class<?> entityClassTableAnnotationType) {
				this.entityClassTableAnnotationType = entityClassTableAnnotationType;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder columnColumnEntry(Map<String, ?> columnColumnEntry) {
				System.out.println(columnColumnEntry.getClass());

				if( ! columnColumnEntry.entrySet().stream().allMatch(e-> e.getClass().equals(ColumnEntry.class))) {
					throw new IllegalArgumentException("argument type mismatch. type is not :" + ColumnEntry.class.getName());
				}
				
				this.columnColumnEntry = columnColumnEntry;
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
	public static abstract class UnderType<T> {
		private final Type type;
		private Class<?> topClass;
		private UnderTypeRecord topRecord;
		private List<UnderTypeRecord> classPath = new ArrayList<>();
		public UnderType() throws ClassNotFoundException {
			Type type = this.getClass().getGenericSuperclass();
			this.type = ((ParameterizedType) type).getActualTypeArguments()[0];
			
			String[] classNameList = this.type.getTypeName().replaceAll(">", "").split("<");
			
			for( int i = 0, iLen = classNameList.length ; i < iLen ; i += 1 ) {
				
				String[] childList = classNameList[i].split(",");
				var prevItemIndex = classPath.size() - 1;
				
				for( int j = 0, jLen = childList.length ; j < jLen ; j += 1) {
					String name = childList[j].trim();
					Class<?> clazz = Class.forName(name);
					UnderTypeRecord underTypeRecord = new UnderTypeRecord(
						clazz,
						new ArrayList<>()
					);
					
					classPath.add(underTypeRecord);
					
					if( i == 0) {
						this.topClass = clazz;
						this.topRecord = underTypeRecord;
						continue;
					}
					
					classPath.get(prevItemIndex).addChild(underTypeRecord);
					
				}
			}
			
		}
		public Class<?> getTopClass() {
			return this.topClass;
		}
		public List<UnderTypeRecord> getClassPath(){
			return this.classPath;
		}
		public UnderTypeRecord getTopRecord() {
			return this.topRecord;
		}
		public record UnderTypeRecord(
				Class<?> clazz,
				List<UnderTypeRecord> childList) {
			
			public void setChild(List<UnderTypeRecord> childList){
				this.childList.addAll(childList);
			}
			public void addChild(UnderTypeRecord child) {
				this.childList.add(child);
			}
		}
	}

	public static class ColumnEntry<K,V> implements Map.Entry<K, V>{
		private K k;
		private V v;
		
		private ColumnEntry(K k, V v) {
			this.k = k;
			this.v = v;
		}
		
		@Override
		public K getKey() {
			// TODO Auto-generated method stub
			return this.k;
		}

		@Override
		public V getValue() {
			// TODO Auto-generated method stub
			return this.v;
		}

		@Override
		public V setValue(V value) {
			// TODO Auto-generated method stub
			this.v = value;
			return this.v;
		}
		
		public static <K, T> ColumnEntry<K, Class<?>> pair(K k, Class<?> v) {
			return new ColumnEntry<K, Class<?>>(k,v);
		}
		public static <K, T> ColumnEntry<K, UnderType<?>> pair(K k, UnderType<?> v) {
			return new ColumnEntry<K, UnderType<?>>(k,v);
		}
		
	}
}
