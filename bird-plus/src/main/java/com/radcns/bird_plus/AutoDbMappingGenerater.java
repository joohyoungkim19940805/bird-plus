package com.radcns.bird_plus;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.AbstractFilter;
import spoon.support.JavaOutputProcessor;

public class AutoDbMappingGenerater  {
	
	protected static volatile ConcurrentMap<String, TableRecord> tableMemory = new ConcurrentHashMap<>();
	protected static volatile ConcurrentMap<String, Path[]> packageHistory = new ConcurrentHashMap<>();
	protected final Launcher spoon = new Launcher();
	protected final JavaOutputProcessor javaOutputProcessor = new JavaOutputProcessor();
	//protected final Mono<Map<String, Entry<CtClass<?>, Map<String, CtFieldReference<?>>>>> entityFilter; 
	//protected final Mono<Map<String, Entry<CtInterface<?>, Map<String, CtFieldReference<?>>>>> repositoryFilter; 
	protected final AutoDbMappingGeneraterOption option;
    
	protected final String defaultRootDirectories = System.getProperty("user.dir") + "\\\\";
	
	public AutoDbMappingGenerater(AutoDbMappingGeneraterOption option) {
		String loading = "loading";
		var interval = Flux.interval(Duration.ofMillis(500))
		.doOnNext(counter -> {
			System.out.print(String.format("\033[%dA", 1));
			System.out.print("\033[2K");
			System.out.print("\033[H\033[2J");
			String dot = ".";
			int dotCount = (int)(counter % 3) + 1;
			System.out.print(loading + dot.repeat(dotCount));
		    System.out.flush();
		})
		.subscribe();
		
		this.option = option;
		spoon.addInputResource(option.defaultRootPath.stream().collect(Collectors.joining("\\\\")));
		Environment env = spoon.getEnvironment();
		env.setAutoImports(true);
		env.setNoClasspath(true);
		env.setShouldCompile(true);
		env.setComplianceLevel(14);
		@SuppressWarnings("unused")
		CtModel model = spoon.buildModel();
		javaOutputProcessor.setFactory(spoon.getFactory());

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

		System.out.println();
		System.out.println("loading end!");
		interval.dispose();
		
	    System.out.println("enter your command");
	    System.out.println("0.close");
    	System.out.println("1.run");
    	System.out.println("2.interval observe");
	    @SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
	    Integer command = null;
	    Integer intervalOfMinutes = null;
	    while (input.hasNext()) {
	    	String inputValue = input.next();
	    	command = parserOnlyNumber(inputValue);
	    	System.out.println("                            - run command : " + command);
	    	if(command == 0) {
	    		break;
	    	}else if(command == 1) {
	    		run().doOnSubscribe(e->{
	    			System.out.println("                            - create start!");
	    		}).subscribe();
	    		command = null;
	    	}else if(command == 2) {
	    		System.out.println("enter miutes, 0 is cancle");
	    		inputValue = input.next();
	    		intervalOfMinutes = parserOnlyNumber(inputValue);
	    		if(intervalOfMinutes == 0) {
	    			continue;
	    		}
	    		break;
	    	}
		    System.out.println("enter your command");
		    System.out.println("0.close");
	    	System.out.println("1.run");
	    	System.out.println("2.interval observe");
		}
	    input.close();
	    if(command == 0) {
	    	return;
	    }
		var observe = Flux.interval(Duration.ofMinutes(intervalOfMinutes == null ? 1 : intervalOfMinutes))
		.onBackpressureDrop()
		.subscribeOn(Schedulers.single())
		.doOnNext(counter -> {
			run().doOnSubscribe(e->{
    			System.out.println("                            - create start!");
    		}).subscribe();
		})
		.subscribe()
		;
		
		System.out.println("you want exist? enter 0");
		Scanner input2 = new Scanner(System.in);
		while (input2.hasNext() && intervalOfMinutes == null) {
			String inputValue = input2.next();
			if(parserOnlyNumber(inputValue) == 0) {
				observe.dispose();
				break;
			}
		}
		input2.close();
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

	private Mono<Map<String, Entry<CtClass<?>, Map<String, CtFieldReference<?>>>>> run() {
		AtomicInteger i = new AtomicInteger();
		if(option.isTest) {
			i.set(0);
			//System.out.println("count ::: " + counter);
		}
		
		Mono<Map<String, Entry<CtClass<?>, Map<String, CtFieldReference<?>>>>> entityFilter = Mono.fromCallable(()->{
			var entityLastName = option.entityClassLastName;
			return spoon.getFactory().Package().getRootPackage().getElements(new AbstractFilter<CtClass<?>>() {
				@Override
				public boolean matches(CtClass<?> element) {
					return element.getSimpleName().endsWith(entityLastName);
				};
			});
		})
		.subscribeOn(Schedulers.boundedElastic())
		.flatMapMany(Flux::fromIterable)
		.collectMap(
			k -> k.getSimpleName(), 
			v -> Map.entry(v, v.getAllFields().stream().collect(Collectors.toMap(fieldsK -> fieldsK.getSimpleName(), fieldsV -> fieldsV)))
		)
		;

		Mono<Map<String, Entry<CtInterface<?>, Map<String, CtFieldReference<?>>>>> repositoryFilter = Mono.fromCallable(()->{
			var repositoryLastName = option.repositoryClassLastName;
			return spoon.getFactory().Package().getRootPackage().getElements(new AbstractFilter<CtInterface<?>>() {
				@Override
				public boolean matches(CtInterface<?> element) {
					return element.getSimpleName().endsWith(repositoryLastName);
				};
			});
		})
		.subscribeOn(Schedulers.boundedElastic())
		.flatMapMany(Flux::fromIterable)
		.collectMap(
				k -> k.getSimpleName(), 
				v -> Map.entry(v, v.getAllFields().stream().collect(Collectors.toMap(fieldsK -> fieldsK.getSimpleName(), fieldsV -> fieldsV)))
		)
		;
		
		return entityFilter.doOnNext(entityFilterMap->{
			repositoryFilter.doOnNext(repositoryFilterMap->{
				scanningDB().flatMap(tableRecord->{
					return Mono.delay(Duration.ofMillis(100)).flatMap(next->{
						if(i.get() > 0) {
							return Mono.empty();
						}
	
						Entry<CtClass<?>, Map<String, CtFieldReference<?>>> memoryEntity = entityFilterMap.get(Character.toUpperCase(tableRecord.camelName.charAt(0)) + tableRecord.camelName.substring(1) + option.entityClassLastName);
						CtClass<?> entity = createClass(tableRecord, memoryEntity);
						
						if( ! entity.hasAnnotation(option.entityClassTableAnnotationType)) {
							CtAnnotation<?> entityTableAnnotationType = createAnnotation(option.entityClassTableAnnotationType, Map.of("value", tableRecord.name));
							entity.addAnnotation(entityTableAnnotationType);
						}
						
						var classAnnotationMono = Flux.fromIterable(option.entityClassDefaultAnnotation.entrySet()).flatMap(e-> Mono.delay(Duration.ofMillis(100)).doOnNext(delay->{
							if(entity.hasAnnotation(e.getKey())) {
								return;
							}
							CtAnnotation<?> annotation = createAnnotation(e.getKey(), e.getValue());
							entity.addAnnotation(annotation);
						}));
						
						var fieldMono = Flux.fromIterable(tableRecord.columnMapper.entrySet()).flatMap(entry -> {
							//if(entity.getField(entry.getValue().camelName) != null) {
							//	return Flux.empty();
							//}
							CtField<?> field = createField(entry.getValue(), (memoryEntity == null ? null : memoryEntity.getValue()), tableRecord);
							if( ! field.hasAnnotation(option.entityClassFieldColumnAnnotationType)) {
								field.addAnnotation(
									createAnnotation(option.entityClassFieldColumnAnnotationType, Map.of("value", entry.getValue().name))
								);
							}
							if(entry.getValue().isPk && option.entityClassFieldPkAnnotationType != null && ! field.hasAnnotation(option.entityClassFieldPkAnnotationType)) {
								field.addAnnotation(
									createAnnotation(option.entityClassFieldPkAnnotationType, Collections.emptyMap())
								);
							}
							entity.addField(field);
							return Flux.fromIterable(option.entityClassFieldDefaultAnnotationType.entrySet()).flatMap( e-> Mono.delay(Duration.ofMillis(100)).doOnNext(d -> {
								if(field.hasAnnotation(e.getKey())) {
									return;
								}
								CtAnnotation<?> annotation = createAnnotation(e.getKey(), e.getValue());
								field.addAnnotation(annotation);
							})).doFinally((f) -> {
								Map<Class<? extends Annotation>, Map<String, Object>> specificField = option.entityClassSpecificFieldAnnotation.get(entry.getValue().name);
								if(specificField != null) {
									specificField.entrySet().forEach(e->{
										if(field.hasAnnotation(e.getKey())) {
											return;
										}
										CtAnnotation<?> annotation = createAnnotation(e.getKey(), e.getValue());
										field.addAnnotation(annotation);
									});
								}	
							});
						});
						
						return Flux.merge(classAnnotationMono, fieldMono).doFinally(f->{
							Entry<CtInterface<?>, Map<String, CtFieldReference<?>>> memoryRepositry = repositoryFilterMap.get(Character.toUpperCase(tableRecord.camelName.charAt(0)) + tableRecord.camelName.substring(1) + option.repositoryClassLastName);
							CtInterface<?> interfaze = createInterface(tableRecord, memoryRepositry);
							CtTypeReference<?> parentType = spoon.getFactory().Code().createCtTypeReference(option.repositoryExtendsClass);
							CtTypeReference<?> parentGernericOfPk = null;

							if(option.repositorySpecificPkClass.containsKey(tableRecord.name)) {
								Object objType = option.repositorySpecificPkClass.get(tableRecord.name);
								if(objType == null) {
									parentGernericOfPk = null;
								}else if(objType.getClass().getSuperclass().equals(UnderType.class)) {
									UnderType<?> underType = UnderType.class.cast(objType);
									CtTypeReference<?> type = spoon.getFactory().Code().createCtTypeReference(underType.getTopClass());
									var list = underType.getClassPath();
									CtTypeReference<?> prevType = type;
									for(int j = 1, len = list.size() ; j < len ; j += 1) {
										UnderTypeRecord underTypeRecord = list.get(j);
										CtTypeReference<?> genericType = spoon.getFactory().Code().createCtTypeReference(underTypeRecord.clazz());
										genericType.setActualTypeArguments(
											underTypeRecord.childList().stream()
											.map(e-> spoon.getFactory().Code().createCtTypeReference(e.clazz()))
											.toList()
										);
										prevType.setActualTypeArguments(List.of(genericType));
										prevType = genericType;
									}
									parentGernericOfPk = type;
								}else {
									parentGernericOfPk = spoon.getFactory().Code().createCtTypeReference((Class<?>)objType);
								}
								
							}else if(tableRecord.columnMapper.containsKey(tableRecord.pkName)){
								var pkColumn = tableRecord.columnMapper.get(tableRecord.pkName);
								var pkClass = option.columnTypeMapper.get(pkColumn.dataTypeName);
								if(pkClass != null) {
									parentGernericOfPk = spoon.getFactory().Code().createCtTypeReference((Class<?>)pkClass);
								}
							}

							if(parentGernericOfPk != null) {
								parentType.setActualTypeArguments(List.of(entity.getReference(), parentGernericOfPk));
							}
							interfaze.addSuperInterface(parentType);
							
							option.entityCeateAfterCallBack.accept(entity, spoon.getFactory());
							option.repositoryCeateAfterCallBack.accept(interfaze, spoon.getFactory());
							
							javaOutputProcessor.createJavaFile(entity);
							
							javaOutputProcessor.createJavaFile(interfaze);
							if(option.isTest) {
								i.getAndIncrement();
							}
							System.out.println(
									"                            - create success file : " + 
									entity.getQualifiedName()
									+ "  &&  " +
									interfaze.getQualifiedName()
							);
						}).collectList();
					});
				})
				.subscribe();
			}).subscribe();
		});
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
		String firstName = Stream.of(tableName.substring(option.tableNameToEntityStartCharAt).split("_")).filter(e-> ! e.isBlank()).toArray(String[]::new)[0].toLowerCase();
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
			if(i.getAndIncrement() == 0 &&  ! isTable) {
				return s;
			}
			return Character.toUpperCase(s.charAt(0)) + s.substring(1);
		}).collect(Collectors.joining());
	}
	private Flux<TableRecord> scanningDB() {
		return Mono.fromCallable(() -> {
			try (Connection connection = DriverManager.getConnection(option.url, option.username, option.password)){
	        	var databaseMetaData = connection.getMetaData();
	        	try (var result = databaseMetaData.getColumns(connection.getCatalog(), option.schema, null, null)){	        		
		        	while (result.next()) {
	        			String tableName = result.getString(3);//.toLowerCase();
	            		String columnName = result.getString(4);//.toLowerCase();
						String dataTypeName = result.getString(6);
	            		TableRecord tableRecord = tableMemory.get(tableName);
	            		ColumnRecord columnRecord = null;
	            
	            		if(tableRecord == null) {
	            			String camelTableName = convertUnderbarToCamelName(tableName);
	            			String camelColumnName = convertUnderbarToCamelName(columnName, tableName);
	            			Map<String, ColumnRecord> cloumnMapper = new HashMap<>();

	            			List<String> pkNameList = new ArrayList<>();
	            			String pkName;
	            			try(var res = databaseMetaData.getPrimaryKeys(connection.getCatalog(), option.schema, tableName)){
				        		while(res.next()){// && pkName == null) {
				        			pkNameList.add(res.getString("COLUMN_NAME"));
				        		}
				        	}
	            			if(pkNameList.isEmpty() || pkNameList.size() > 1) {
	            				pkName = null;
	            			}else {
	            				pkName = pkNameList.get(0);
	            			}
	            			columnRecord = new ColumnRecord(columnName, camelColumnName, dataTypeName, columnName.equals(pkName));
	            			cloumnMapper.put(columnName, columnRecord);
	            			tableRecord = new TableRecord(tableName, camelTableName, createGroupPackage(tableName), cloumnMapper, pkName);
	            			tableMemory.put(tableName, tableRecord);
	            		}else if(! tableRecord.columnMapper.containsKey(columnName)) {
	            			String camelColumnName = convertUnderbarToCamelName(columnName, tableName);
	            			columnRecord = new ColumnRecord(columnName, camelColumnName, dataTypeName, columnName.equals(tableRecord.pkName));
	            			tableRecord.columnMapper.put(columnName, columnRecord);
	            		}	
			    	}
	        	}
	        	return tableMemory.values();
	        } catch (SQLException e) {
				// TODO Auto-generated catch block
	        	e.printStackTrace();
	        	System.out.println(option.url);
	        	return null;
			}
		}).flatMapMany(Flux::fromIterable);
	}
	
	private CtInterface<?> createInterface(TableRecord tableRecord, Entry<CtInterface<?>, Map<String, CtFieldReference<?>>> memoryRepository){
		if(memoryRepository != null) {
			return memoryRepository.getKey();
		}
		String packageNames = 
				Stream.of(
						option.defaultPackageRootPath.stream(), 
						Stream.of(packageHistory.get(tableRecord.name)[1].getParent().getFileName().toString(), packageHistory.get(tableRecord.name)[0].getFileName().toString())
					).flatMap(e->e).collect(Collectors.joining("."))
					+ ".";
		CtInterface<?> interfaze = spoon.getFactory().Interface().create(packageNames + Character.toUpperCase(tableRecord.camelName.charAt(0)) + tableRecord.camelName.substring(1) + option.repositoryClassLastName);
		
		interfaze.addModifier(ModifierKind.PUBLIC);

		return interfaze;
	}
	
	private CtClass<?> createClass(TableRecord tableRecord, Entry<CtClass<?>, Map<String, CtFieldReference<?>>> memoryEntity) {
		if(memoryEntity != null) {
			return memoryEntity.getKey();
		}
		String packageNames = 
				Stream.of(
						option.defaultPackageRootPath.stream(), 
						Stream.of(packageHistory.get(tableRecord.name)[0].getParent().getFileName().toString(), packageHistory.get(tableRecord.name)[0].getFileName().toString())
					).flatMap(e->e).collect(Collectors.joining("."))
					+ ".";
		CtClass<?> clazz = spoon.getFactory().Class().create(packageNames + Character.toUpperCase(tableRecord.camelName.charAt(0)) + tableRecord.camelName.substring(1) + option.entityClassLastName);
		
		clazz.addModifier(ModifierKind.PUBLIC);
		
		return clazz;
	}
	private CtField<?> createField(ColumnRecord columnRecord, Map<String, CtFieldReference<?>> fieldEntry, TableRecord tableRecord){

		CtField<?> field;
		if(fieldEntry != null && fieldEntry.containsKey(columnRecord.camelName)) {
			field = fieldEntry.get(columnRecord.camelName).getFieldDeclaration();
		}else {
			field = spoon.getFactory().Core().createField();
		}
		field.addModifier(ModifierKind.PRIVATE);
		field.setSimpleName(columnRecord.camelName);
		
		Object objType = option.columnSpecificTypeMapper.get(columnRecord.name);
		objType = objType != null ? objType : option.columnTypeMapper.get(columnRecord.dataTypeName);
		CtTypeReference<?> type;
		if(objType == null) {
			System.out.println("                     warning - %s(your table name) : %s(your column name) is undefined java type. so i convert Object class.".formatted(tableRecord.name, columnRecord.name));
			type = spoon.getFactory().Code().createCtTypeReference(Object.class);
		}else if(objType.getClass().getSuperclass().equals(UnderType.class)) {
			UnderType<?> underType = UnderType.class.cast(objType);
			type = spoon.getFactory().Code().createCtTypeReference(underType.getTopClass());
			var list = underType.getClassPath();
			CtTypeReference<?> prevType = type;
			for(int i = 1, len = list.size() ; i < len ; i += 1) {
				UnderTypeRecord underTypeRecord = list.get(i);
				CtTypeReference<?> genericType = spoon.getFactory().Code().createCtTypeReference(underTypeRecord.clazz());
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
	
	private CtAnnotation<?> createAnnotation(Class<?> targetClass, Map<String, Object> targetValues) {
		CtAnnotationType<?> annotationType = (CtAnnotationType<?>) spoon.getFactory().Type().get(targetClass);
		CtAnnotation<Annotation> annotation = spoon.getFactory().Core().createAnnotation();
		
		annotation.setAnnotationType(annotationType.getReference());
		
		//if(targetValues.equals(Collections.emptyMap())) {
		annotation.setElementValues(targetValues);
		//}
		
		return annotation;
	}

	private Integer parserOnlyNumber(CharSequence value) {
		if(value == null || value.length() == 0) {
			return null;
		}
		char[] result = new char[value.length()];
		int cursor = 0;
		CharBuffer buffer = CharBuffer.wrap(value);
		
		while(buffer.hasRemaining()) {
			char c = buffer.get();
			if(c > 47 && c < 58) {
				result[cursor++] = c;
			}
		}
		
		return Integer.valueOf(new String(result, 0, cursor));
	}

    private record TableRecord(
    		   String name,
    		   String camelName,
    		   String packagePath,
    		   Map<String, ColumnRecord> columnMapper,
    		   String pkName
    		) {}
    private record ColumnRecord(
    			String name,
    			String camelName,
    			String dataTypeName,
    			Boolean isPk
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
		@SuppressWarnings("unused")
		private final Class<?> entityExtendsClass;
		private final Class<?> repositoryExtendsClass;
		private final Map<String, ?> repositorySpecificPkClass;
		private final Map<String, Map<Class<? extends Annotation>, Map<String, Object>>> entityClassSpecificFieldAnnotation;
		private final Class<? extends Annotation> entityClassFieldColumnAnnotationType;
		private final Map<Class<? extends Annotation>, Map<String, Object>> entityClassFieldDefaultAnnotationType;
		private final Map<Class<? extends Annotation>, Map<String, Object>> entityClassDefaultAnnotation;
		private final Class<? extends Annotation> entityClassTableAnnotationType;
		private final Class<? extends Annotation> entityClassFieldPkAnnotationType;
		private final Map<String, ?> columnTypeMapper;
		private final Map<String, ?> columnSpecificTypeMapper;
		private final BiConsumer<CtClass<?>, Factory> entityCeateAfterCallBack;
		private final BiConsumer<CtInterface<?>, Factory> repositoryCeateAfterCallBack;
		private final Boolean isTest;

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
			repositorySpecificPkClass = builder.repositorySpecificPkClass;
			entityClassSpecificFieldAnnotation = builder.entityClassSpecificFieldAnnotation;
			entityClassFieldColumnAnnotationType = builder.entityClassFieldColumnAnnotationType;
			entityClassFieldDefaultAnnotationType = builder.entityClassFieldDefaultAnnotationType;
			entityClassDefaultAnnotation = builder.entityClassDefaultAnnotation;
			entityClassTableAnnotationType = builder.entityClassTableAnnotationType;
			entityClassFieldPkAnnotationType = builder.entityClassFieldPkAnnotationType;
			columnTypeMapper = builder.columnTypeMapper;
			columnSpecificTypeMapper = builder.columnSpecificTypeMapper;
			entityCeateAfterCallBack = builder.entityCeateAfterCallBack;
			repositoryCeateAfterCallBack = builder.repositoryCeateAfterCallBack;
			isTest = builder.isTest;
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
			private List<String> defaultRootPath = Collections.emptyList();
			private List<String> defaultPackageRootPath = Collections.emptyList();
			private String entityClassLastName = "Entity";
			private String repositoryClassLastName = "Repository";
			private Class<?> entityExtendsClass;
			private Class<?> repositoryExtendsClass;
			private Map<String, ?> repositorySpecificPkClass = Collections.emptyMap();
			private Map<String, Map<Class<? extends Annotation>, Map<String, Object>>> entityClassSpecificFieldAnnotation = Collections.emptyMap();
			private Class<? extends Annotation> entityClassFieldColumnAnnotationType;
			private Map<Class<? extends Annotation>, Map<String, Object>> entityClassFieldDefaultAnnotationType = Collections.emptyMap();
			private Map<Class<? extends Annotation>, Map<String, Object>> entityClassDefaultAnnotation = Collections.emptyMap();
			private Class<? extends Annotation> entityClassTableAnnotationType;
			private Class<? extends Annotation> entityClassFieldPkAnnotationType;
			private Map<String, ?> columnTypeMapper = Collections.emptyMap();
			private Map<String, ?> columnSpecificTypeMapper = Collections.emptyMap();
			private BiConsumer<CtClass<?>, Factory> entityCeateAfterCallBack = ((ctElement, factory)->{});
			private BiConsumer<CtInterface<?>, Factory> repositoryCeateAfterCallBack = ((ctElement, factory)->{});
			private Boolean isTest = false;

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
			/*public AutoDbMappingGeneraterOptionBuilder entityExtendsClass(Class<?> entityExtendsClass) {
				this.entityExtendsClass = entityExtendsClass;
				return this;
			}*/
			public AutoDbMappingGeneraterOptionBuilder repositoryExtendsClass(Class<?> repositoryExtendsClass) {
				this.repositoryExtendsClass = repositoryExtendsClass;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder repositorySpecificPkClass(Map<String, ?> repositorySpecificPkClass) {
				this.repositorySpecificPkClass = repositorySpecificPkClass;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityClassFieldColumnAnnotationType(Class<? extends Annotation> entityClassFieldColumnAnnotationType) {
				this.entityClassFieldColumnAnnotationType = entityClassFieldColumnAnnotationType;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityClassFieldDefaultAnnotationType(Map<Class<? extends Annotation>, Map<String, Object>> entityClassFieldDefaultAnnotationType) {
				this.entityClassFieldDefaultAnnotationType = entityClassFieldDefaultAnnotationType;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityClassTableAnnotationType(Class<? extends Annotation> entityClassTableAnnotationType) {
				this.entityClassTableAnnotationType = entityClassTableAnnotationType;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityClassFieldPkAnnotationType(Class<? extends Annotation> entityClassFieldPkAnnotationType) {
				this.entityClassFieldPkAnnotationType = entityClassFieldPkAnnotationType;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder columnTypeMapper(Map<String, ?> columnTypeMapper) {
				this.columnTypeMapper = columnTypeMapper;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder columnSpecificTypeMapper(Map<String, ?> columnSpecificTypeMapper) {
				this.columnSpecificTypeMapper = columnSpecificTypeMapper;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder entityCeateAfterCallBack(BiConsumer<CtClass<?>, Factory> entityCeateAfterCallBack) {
				this.entityCeateAfterCallBack = entityCeateAfterCallBack;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder repositoryCeateAfterCallBack(BiConsumer<CtInterface<?>, Factory> repositoryCeateAfterCallBack) {
				this.repositoryCeateAfterCallBack = repositoryCeateAfterCallBack;
				return this;
			}
			public AutoDbMappingGeneraterOptionBuilder isTest(Boolean isTest) {
				this.isTest = isTest;
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
