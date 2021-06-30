package com.dream.annotation_complier;

import com.dream.annotation_ann.annotion.Dann;
import com.dream.annotation_ann.annotion.Para;
import com.dream.annotation_complier.base.BaseProcessor;
import com.dream.annotation_complier.base.bean.ClassEntity;
import com.dream.annotation_complier.base.bean.MethodEntity;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DProcessor extends BaseProcessor {

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        Class[] supportClazz = new Class[]{Dann.class};
        return supportClazz;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Map<String, ClassEntity> map = entityHandler.handlerElement(roundEnvironment, this);

        for (Map.Entry<String, ClassEntity> entry :
                map.entrySet()) {
            entityHandler.generateCode(generateFile(entry.getValue()));
        }
        return false;
    }

    private JavaFile generateFile(ClassEntity classEntity) {
        TypeElement element = classEntity.getElement();
        String pkgName = classEntity.getClassPackageName();
        String simpleName = classEntity.getClassSimpleName();
        ClassName clazzName = ClassName.get(pkgName, simpleName);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(simpleName + "_Impl")
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC);

        if (element.getKind().isInterface()) {
            classBuilder.addSuperinterface(clazzName);
        } else if (element.getKind().isClass()) {
            classBuilder.superclass(clazzName);
        }
        resolveMethod(classBuilder, classEntity);
        return JavaFile.builder("com.dream.testgenerate",classBuilder.build()).build();
    }

    private void resolveMethod(TypeSpec.Builder classBuilder, ClassEntity classEntity) {
        Map<String, MethodEntity> methodMap = classEntity.getMethods();

        for (Map.Entry<String, MethodEntity> entry :
                methodMap.entrySet()) {
            MethodEntity entity = entry.getValue();
            classBuilder.addMethod(generateMethod(entity));
        }
    }

    private MethodSpec generateMethod(MethodEntity methodEntity) {
        ExecutableElement methodElement = methodEntity.getMethodElement();
        String methodName = methodEntity.getMethodName();

        String jDoc = "auto generate by xingxing";
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addJavadoc(jDoc)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(Override.class);

        List<? extends VariableElement> paras = methodEntity.getParameterElements();
        for (VariableElement para :
                paras
        ) {
            Para paraann = para.getAnnotation(Para.class);
            if (paraann != null){
                entityHandler.printNormalMsg("name is " + para.getSimpleName().toString());
            }
            methodBuilder.addParameter(ParameterSpec.get(para));
        }
        return methodBuilder.build();
    }

}
