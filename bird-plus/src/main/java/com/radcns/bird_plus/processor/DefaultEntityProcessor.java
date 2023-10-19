package com.radcns.bird_plus.processor;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import org.springframework.data.relational.core.mapping.Column;

import lombok.Getter;
import spoon.SpoonAPI;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.JavaOutputProcessor;

public class DefaultEntityProcessor extends AbstractProcessor<CtClass<?>>{
	
	public DefaultEntityProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process(CtClass<?> element) {
		// TODO Auto-generated method stub
		if( ! element.getSimpleName().endsWith("Entity")){
			return;
		}
		
		if( ! element.getSimpleName().equals("AccountEntity")) {
			return;
		}
		System.out.println("processor start!!!!");
		final CtTypeReference<Long> testMilsRef = getFactory().Code().createCtTypeReference(Long.class);
		final CtField<Long> testMilsField = getFactory().Core().<Long>createField();
		final CtAnnotationType<Column> columnAnnotationType = (CtAnnotationType<Column>) getFactory().Type().<Column>get(Column.class);
		final CtAnnotationType<Getter> getterAnnotationType = (CtAnnotationType<Getter>) getFactory().Type().<Getter>get(Getter.class);
		
		final CtAnnotation<Annotation> columnAnnotation = getFactory().Core().createAnnotation();
		columnAnnotation.setAnnotationType(columnAnnotationType.getReference());
		columnAnnotation.setElementValues(Map.of("value", "test_mils"));
		
		final CtAnnotation<Annotation> getterAnnotation = getFactory().Core().createAnnotation();
		getterAnnotation.setAnnotationType(getterAnnotationType.getReference());
		
		//final CtAnnotation<Annotation> Annotations = getFactory().Core().createAnnotation();
		//Annotations.setAnnotations(List.of(columnAnnotation, getterAnnotation));

		testMilsField.setSimpleName("testMils");
		testMilsField.setType(testMilsRef);
		testMilsField.addModifier(ModifierKind.PRIVATE);
		testMilsField.addAnnotation(columnAnnotation);
		testMilsField.addAnnotation(getterAnnotation);
		//testMilsField.addAnnotation(null)
		element.addField(testMilsField);
		//element.setFields(List.of(testMilsField));
		System.out.println(element);
		element.getTopLevelType();
		JavaOutputProcessor javaOutput = new JavaOutputProcessor();
		javaOutput.setFactory(getFactory());
		
		javaOutput.createJavaFile(element);
	}

}
