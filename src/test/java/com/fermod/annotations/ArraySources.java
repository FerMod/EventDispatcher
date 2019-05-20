package com.fermod.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.RetentionPolicy;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(ArrayArgumentsProvider.class)
public @interface ArraySources {
    ArraySource[] arrays();
}
