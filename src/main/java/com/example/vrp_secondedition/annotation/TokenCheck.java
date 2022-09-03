package com.example.vrp_secondedition.annotation;

import com.example.vrp_secondedition.enums.token.TokenType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TokenCheck {
    boolean required() default true;

    TokenType TYPE() default TokenType.User;
}

