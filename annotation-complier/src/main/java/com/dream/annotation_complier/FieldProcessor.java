package com.dream.annotation_complier;

import com.dream.annotation_ann.annotion.Field;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.dream.annotation_complier.Consts.CLASS_HELPER_SUFFIX;
import static com.dream.annotation_complier.Consts.TEMPLETE_IFIELD_HELPER;


@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FieldProcessor extends BaseProcessor {

    private HashMap<TypeElement,List<Element>>  parentAndFieldsMap = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!set.isEmpty()){
            Set<? extends Element> fields = roundEnvironment.getElementsAnnotatedWith(Field.class);
            categories(fields);
            parseFields(fields);
            return true;
        }
        return false;
    }

    private void categories(Set<? extends Element> fields){
        if (null != fields && !fields.isEmpty()){
            for (Element element :
                    fields) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                if (parentAndFieldsMap.containsKey(typeElement)){
                    parentAndFieldsMap.get(typeElement).add(element);
                }else{
                    List<Element> list = new ArrayList<>();
                    list.add(element);
                    parentAndFieldsMap.put(typeElement,list);
                }
            }
        }
    }

    private void parseFields(Set<? extends Element> fields){

        TypeElement type_helper = elementUtils.getTypeElement(TEMPLETE_IFIELD_HELPER);

        ParameterSpec injectPara = ParameterSpec.builder(TypeName.OBJECT,"obj").build();

        MethodSpec.Builder injectMethod = MethodSpec.methodBuilder("inject")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(injectPara);

        for (Map.Entry<TypeElement,List<Element>> set:
               parentAndFieldsMap.entrySet()){
            TypeElement parent = set.getKey();
            List<Element> elementList = set.getValue();
            String qualifiedName = parent.getQualifiedName().toString();
            String packageName = "com.dream.xrouter.helper";
            String fileName = parent.getSimpleName() + CLASS_HELPER_SUFFIX;

            injectMethod.addStatement(
                    "$T target = ($T)obj",
                    ClassName.get(parent),
                    ClassName.get(parent)
            );

            for (Element element :
                    elementList) {
                Field field = element.getAnnotation(Field.class);
                injectMethod.addStatement(
                        "target." + field.name() + " = target.getIntent().getStringExtra($S)",
                        field.name()
                );
            }

            TypeSpec.Builder helper = TypeSpec.classBuilder(fileName)
                    .addSuperinterface(ClassName.get(type_helper))
                    .addModifiers(Modifier.PUBLIC);

            helper.addMethod(injectMethod.build());

            try {
                JavaFile.builder(packageName,helper.build()).build().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Field.class.getCanonicalName());
        return set;
    }
}
