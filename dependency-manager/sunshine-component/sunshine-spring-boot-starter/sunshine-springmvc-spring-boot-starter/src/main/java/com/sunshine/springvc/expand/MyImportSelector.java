package com.sunshine.springvc.expand;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
//        return new String[]{ImportClass.class.getName()};
        return null;
    }
}
