package com.dream.annotation_complier.base;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class BaseProcessor extends AbstractProcessor {

    protected EntityHandler entityHandler;
    protected Types typeUtils;
    protected Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        entityHandler = new EntityHandler(processingEnv);
        typeUtils = entityHandler.getTypeUtils();
        elementUtils = entityHandler.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        Class[] typeStrings = getSupportedAnnotations();
        for (Class type : typeStrings) {
            types.add(type.getCanonicalName());
        }
        return types;
    }

    protected abstract Class<? extends Annotation>[] getSupportedAnnotations();

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
