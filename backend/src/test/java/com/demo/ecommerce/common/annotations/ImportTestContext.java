package com.demo.ecommerce.common.annotations;

import com.demo.ecommerce.SecurityConfig;
import io.micrometer.common.lang.NonNullApi;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.lang.annotation.*;
import java.util.Map;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(ImportTestContextSelector.class)
public @interface ImportTestContext {
    Class<?>[] value();
}

@NonNullApi
class ImportTestContextSelector implements ImportSelector {

    @Override
    public String[] selectImports(@NotNull AnnotationMetadata importingClassMetadata) {
        // Extract classes from the annotation
        List<String> imports = new ArrayList<>();

        // Add required classes for testing
        // TODO: Look into automatic discovery to avoid hardcoding this
        imports.add(SecurityConfig.class.getName());

        // Read user-specified classes
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(ImportTestContext.class.getName());

        // Add user imported classes
        if (attributes != null) {
            Class<?>[] extraImports = (Class<?>[]) attributes.get("value");
            for (Class<?> importedClass : extraImports) {
                imports.add(importedClass.getName());
            }
        }

        return imports.toArray(new String[0]);
    }
}
