package com.hikvision.spring.boot.retrofit.starter;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.FactoryBean;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.util.List;

/**
 * @author xiabiao
 * @date 2022-06-20
 */
public class RetrofitFactoryBean<T> implements FactoryBean<T> {

  private Class<T> clazz;

  private String baseUrl;

  private List<Interceptor> interceptors;

  private Converter.Factory converterFactory;

  @Override
  public T getObject() throws Exception {
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    for (Interceptor interceptor : interceptors) {
      clientBuilder.addInterceptor(interceptor);
    }
    Retrofit.Builder builder = new Retrofit.Builder();
    builder.baseUrl(baseUrl);
    builder.client(clientBuilder.build());

    builder.addConverterFactory(converterFactory);
    return builder.build().create(clazz);
  }

  @Override
  public Class<?> getObjectType() {
    return clazz;
  }

  public Class<T> getClazz() {
    return clazz;
  }

  public void setClazz(Class<T> clazz) {
    this.clazz = clazz;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public List<Interceptor> getInterceptors() {
    return interceptors;
  }

  public void setInterceptors(List<Interceptor> interceptors) {
    this.interceptors = interceptors;
  }

  public Converter.Factory getConverterFactory() {
    return converterFactory;
  }

  public void setConverterFactory(Converter.Factory converterFactory) {
    this.converterFactory = converterFactory;
  }
}
