package com.radcns.bird_plus.processor;

import java.io.File;
import java.util.List;

import spoon.SpoonAPI;
import spoon.processing.AbstractProcessor;
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
		testMilsField.setSimpleName("testMils");
		testMilsField.setType(testMilsRef);
		testMilsField.addModifier(ModifierKind.PUBLIC);
		element.addField(testMilsField);
		element.setFields(List.of(testMilsField));
		System.out.println(element);
		element.getTopLevelType();
		JavaOutputProcessor javaOutput = new JavaOutputProcessor();
		javaOutput.setFactory(getFactory());
		javaOutput.createJavaFile(element);
	}

}
