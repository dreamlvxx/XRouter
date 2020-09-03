package com.dream.annotation_complier;

import com.dream.annotation_ann.annotion.Field;
import com.google.auto.service.AutoService;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;


@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FieldProcessor extends BaseProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Field.class.getCanonicalName());
        return set;
    }
}
