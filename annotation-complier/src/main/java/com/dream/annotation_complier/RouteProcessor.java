package com.dream.annotation_complier;

import com.dream.annotation_ann.annotion.Field;
import com.dream.annotation_ann.annotion.Route;
import com.dream.annotation_ann.model.RouteMeta;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

import static com.dream.annotation_complier.Consts.ACTIVITY;
import static com.dream.annotation_complier.Consts.CLASS_ROUMAP_NAME;
import static com.dream.annotation_complier.Consts.GENERATE_FILE_ROUTE;
import static com.dream.annotation_complier.Consts.METHOD_LOAD_INTO;
import static com.dream.annotation_complier.Consts.TEMPLETE_IROUTERMAP;
import static javax.lang.model.element.Modifier.PUBLIC;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class RouteProcessor extends BaseProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!set.isEmpty()) {
            Set<? extends Element> routes = roundEnvironment.getElementsAnnotatedWith(Route.class);
            this.parseRoute(routes);
            return true;
        }
        return false;
    }

    private void parseRoute(Set<? extends Element> routes) {
        TypeMirror type_Activity = elementUtils.getTypeElement(ACTIVITY).asType();
        TypeElement type_iroutermap = elementUtils.getTypeElement(TEMPLETE_IROUTERMAP);

        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class)
        );

        ParameterSpec loadintoPara = ParameterSpec.builder(parameterizedTypeName, "map").build();

        MethodSpec.Builder loadIntoMethod = MethodSpec.methodBuilder(METHOD_LOAD_INTO)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(loadintoPara);

        for (Element element : routes) {
            TypeMirror typeMirror = element.asType();
            if (types.isSubtype(typeMirror, type_Activity)) {
                Route route = element.getAnnotation(Route.class);

                loadIntoMethod.addStatement(
                        "map.put($S,new RouteMeta($S,$T.class))",
                        route.path(),
                        route.path(),
                        ClassName.get((TypeElement) element)
                );
            }
        }
        try {
            JavaFile.builder(GENERATE_FILE_ROUTE,
                    TypeSpec.classBuilder(CLASS_ROUMAP_NAME)
                            .addSuperinterface(ClassName.get(type_iroutermap))
                            .addModifiers(PUBLIC)
                            .addMethod(loadIntoMethod.build())
                            .build()
            ).build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Route.class.getCanonicalName());
        set.add(Field.class.getCanonicalName());
        return set;
    }
}
