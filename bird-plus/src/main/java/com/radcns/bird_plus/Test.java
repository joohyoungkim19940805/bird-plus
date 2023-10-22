package com.radcns.bird_plus;

import java.io.File;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.radcns.bird_plus.AutoDbMappingGenerater.AutoDbMappingGeneraterOption;
import com.radcns.bird_plus.AutoDbMappingGenerater.UnderType;
import com.radcns.bird_plus.processor.DefaultEntityProcessor;

import io.r2dbc.postgresql.codec.Json;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spoon.Launcher;
import spoon.OutputType;
import spoon.SpoonAPI;
import spoon.SpoonModelBuilder.InputType;
import spoon.compiler.Environment;
import spoon.experimental.SpoonifierVisitor;
import spoon.processing.AbstractProcessor;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.AbstractFilter;
import spoon.support.JavaOutputProcessor;

import static java.util.Map.entry;
import static com.radcns.bird_plus.AutoDbMappingGenerater.ColumnEntry;
public class Test {
	public static void main1(String a[]) {
		SpoonAPI spoon = new Launcher();
		spoon.addInputResource("src/main/java/com/radcns/bird_plus");
		CtModel model = spoon.buildModel();
		Environment env = spoon.getEnvironment();
		env.setAutoImports(true);
		env.setNoClasspath(true);
		env.setShouldCompile(true);
		env.setComplianceLevel(14);
		
		//spoon.getModelBuilder().generateProcessedSourceFiles(OutputType.CLASSES);
		//spoon.getModelBuilder().compile(InputType.FILES);
		
		//System.out.println(Stream.of(spoon.getEnvironment().getSourceClasspath()).toList());
		
		System.out.println(spoon.getEnvironment().getBinaryOutputDirectory());
		/*
		System.out.println(
		Stream.of(
			spoon.getEnvironment().getSourceOutputDirectory().getPath().split("\\\\")
		).filter(e-> ! e.equals("spooned")).collect(Collectors.joining("\\\\"))
		);
		spoon.getEnvironment().setSourceOutputDirectory(new File(
				Stream.of(
						spoon.getEnvironment().getSourceOutputDirectory().getPath().split("\\\\")
					).filter(e-> ! e.equals("spooned")).collect(Collectors.joining("\\\\"))		
		));
		*/
		/*spoon.getEnvironment().setBinaryOutputDirectory(
				Stream.of(
						spoon.getEnvironment().getSourceOutputDirectory().getPath().split("\\\\")
					).filter(e-> ! e.equals("spooned-classes")).collect(Collectors.joining("\\\\"))	 + "\\\\target\\\\classes"	
		);*/
		model.processWith(new DefaultEntityProcessor());
		//model.processWith(new JavaOutputProcessor());
		//spoon.getEnvironment().setSourceOutputDirectory(new File())
		//CtModel model = spoon.getModel();
		//System.out.println(spoon.getFactory());
		//System.out.println(spoon.getFactory().Package());
		//System.out.println(spoon.getFactory().Package().getRootPackage());
		//System.out.println(spoon.getFactory().Package().getAll());
		/*System.out.println(
				spoon.getFactory().Package().getRootPackage().getElements(new AbstractFilter<CtClass>() {
					@Override
					public boolean matches(CtClass element) {
						System.out.println(element.getSimpleName());
						//return element.getSimpleName().endsWith("Entity");
						return element.getSimpleName().equals("AccountEntity");
					};
				})
				);*/
		//model.getRootPackage().accept(new DefaultEntityProcessor());
		//model.processWith(new DefaultEntityProcessor());
		//new AccountEntity().testMils;
	}
	public static void main2(String a[]) throws ClassNotFoundException {
		
		new AutoDbMappingGenerater(AutoDbMappingGeneraterOption.builder()
		   		.url("jdbc:postgresql://kor-zombi-rds-3.cylfrmmsl7kc.ap-northeast-2.rds.amazonaws.com:5432/kor_zombi_database")
		   		.username("kor_zombi_rds")
		   		.password("rlawngud1")
		   		.schema("bird_plus")
		   		.tableNameToEntityStartCharAt(0)
		   		.defaultRootPath( List.of("src", "main", "java") )
		   		.defaultPackageRootPath( List.of("com", "radcns", "bird_plus") )
		   		.entityClassLastName("Entity")
		   		.entityClassFieldColumnAnnotationType(Column.class)
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
	   			.columnColumnEntry(Map.ofEntries(
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
		   		.build()
		   	);
	   	try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main3(String a[]) {
		int len = 5;
		AtomicInteger ii = new AtomicInteger();
		List<Mono<Integer>> testList = new ArrayList<>();
		for(int i = 0;i < len;i+=1) {
			testList.add(
			Mono.fromCallable(() -> {
				System.out.println(ii.get());
				if(ii.get() == 1) {
					ii.getAndIncrement();
					return null;
				}
				return ii.getAndIncrement();
			})
			);
		}
		var test = Flux.fromIterable(testList)
		.flatMap(e->e)
		.collectList()
		.block()
		;
		System.out.println(test);
	}
	public static void main(String a[]) throws ClassNotFoundException {
		var under = new AutoDbMappingGenerater.UnderType<List<Map<String, String>>>() {
		};
		String test = "java.util.List<java.util.Map<java.lang.String, java.lang.String>>";
		test = test.replaceAll(">", "");
		System.out.println(
		Stream.of(test.split("<")).flatMap(e->Stream.of(e.split(","))).toList()
		);
		System.out.println(
		Stream.of(test.split("<")).toList().size()
		);
		
		System.out.println(
				Stream.of(test.split("<")).flatMap(e->Stream.of(e.split(","))).toList()
				);

	}
}
