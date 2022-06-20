package com.hikvision.spring.boot.retrofit.starter.enums;

import com.hikvision.spring.boot.retrofit.starter.RetrofitScanRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiabiao
 * @date 2022-06-20
 */
@Import(RetrofitScanRegister.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableRetrofitClient {}
