package com.hikvision.spring.boot.retrofit.starter;

import com.hikvision.spring.boot.retrofit.starter.enums.RetrofitClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

/**
 * @author xiabiao
 * @date 2022-06-20
 */
public class RetrofitScanConfigure implements BeanDefinitionRegistryPostProcessor {

  private String basePackage;

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    ClassPathRetrofitScanner scanner = new ClassPathRetrofitScanner(registry);
    scanner.addIncludeFilter(new AnnotationTypeFilter(RetrofitClient.class));
    scanner.scan(ClassUtils.getPackageName(basePackage));
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {}

  public String getBasePackage() {
    return basePackage;
  }

  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }
}
