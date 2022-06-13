package win.minaandyyh.sparrow.spring.bean;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import win.minaandyyh.sparrow.annotation.SparrowClient;
import win.minaandyyh.sparrow.spring.EnableSparrow;

import java.util.ArrayList;
import java.util.List;

/**
 * Sparrow client spring bean definition registrar
 *
 * @author masteryyh
 */
public class SparrowClientBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String ENABLE_SPARROW_ANNOTATION = EnableSparrow.class.getName();

    private static final String SPARROW_CLIENT_ANNOTATION = SparrowClient.class.getName();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean enabled = importingClassMetadata.hasAnnotation(ENABLE_SPARROW_ANNOTATION);
        if (!enabled) {
            return;
        }

        String[] basePackages = {};
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(ENABLE_SPARROW_ANNOTATION));
        if (attributes != null) {
            basePackages = attributes.getStringArray("value");
            if (ArrayUtils.isEmpty(basePackages)) {
                String className = importingClassMetadata.getClassName();
                try {
                    Class<?> annotatedClass = Class.forName(className);
                    basePackages = new String[]{annotatedClass.getPackageName()};
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Cannot load annotated class " + className + ".");
                }
            }
        }

        List<Class<?>> classes = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter((reader, factory) -> {
            if (!reader.getAnnotationMetadata().hasAnnotation(SPARROW_CLIENT_ANNOTATION)) {
                return false;
            }

            String className = reader.getClassMetadata().getClassName();
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Cannot load class " + className + ".");
            }
            return true;
        });

        for (String basePackage : basePackages) {
            scanner.findCandidateComponents(basePackage);
        }

        for (Class<?> clazz : classes) {
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                    .genericBeanDefinition(SparrowClientFactoryBean.class)
                    .getRawBeanDefinition();
            ConstructorArgumentValues constructorValues = new ConstructorArgumentValues();
            constructorValues.addIndexedArgumentValue(0, clazz);
            beanDefinition.setConstructorArgumentValues(constructorValues);
            registry.registerBeanDefinition(clazz.getName(), beanDefinition);
        }
    }
}
