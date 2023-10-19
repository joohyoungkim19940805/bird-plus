package com.radcns.bird_plus;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.radcns.bird_plus.entity.account.AccountEntity;
import com.radcns.bird_plus.processor.DefaultEntityProcessor;

import spoon.Launcher;
import spoon.OutputType;
import spoon.SpoonAPI;
import spoon.SpoonModelBuilder.InputType;
import spoon.compiler.Environment;
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

public class Test {
	public static void main(String a[]) {
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
}
