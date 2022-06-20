package com.hikvision.spring.boot.retrofit.starter.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiabiao
 * @date 2022-06-20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetrofitClient {

  /**
   * 基础路径
   *
   * @return
   */
  String baseUrl();
}
