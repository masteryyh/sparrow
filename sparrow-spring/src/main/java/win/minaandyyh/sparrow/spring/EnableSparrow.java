package win.minaandyyh.sparrow.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import win.minaandyyh.sparrow.spring.bean.SparrowClientBeanDefinitionRegistrar;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for enabling Sparrow client autoconfiguration and injection
 *
 * @author masteryyh
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration
@Import(SparrowClientBeanDefinitionRegistrar.class)
public @interface EnableSparrow {
    String[] value() default {};
}
