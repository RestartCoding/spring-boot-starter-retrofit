package com.hikvision.spring.boot.retrofit.starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author xiabiao
 * @date 2022-06-20
 */
@Configuration
public class RetrofitAutoConfiguration {

  @ConditionalOnClass(JacksonConverterFactory.class)
  @ConditionalOnMissingBean(Converter.Factory.class)
  @Bean
  public Converter.Factory jacksonConverterFactory() {
    return JacksonConverterFactory.create();
  }
}
