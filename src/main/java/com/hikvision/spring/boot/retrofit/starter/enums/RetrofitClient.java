package com.hikvision.spring.boot.retrofit.starter.enums;

import okhttp3.Interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

/**
 * @author xiabiao
 * @date 2022-06-20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetrofitClient {

  /**
   * 基础路径。优先于baseUrlClass。支持${*}形式注入配置属性
   *
   * @return base url
   */
  String baseUrl() default "";

  /**
   * 获取baseUrl的方法。配置多个只有第一个生效
   *
   * @return base url supplier
   */
  Class<? extends Supplier<String>>[] baseUrlClass() default {};

  /**
   * 拦截器，按照数组顺序拦截
   *
   * @apiNote 必须提供无参构造器
   * @return interceptor class array
   */
  Class<? extends Interceptor>[] interceptors() default {};
}
