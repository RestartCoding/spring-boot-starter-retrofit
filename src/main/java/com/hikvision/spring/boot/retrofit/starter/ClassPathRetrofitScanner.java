package com.hikvision.spring.boot.retrofit.starter;

import com.hikvision.spring.boot.retrofit.starter.enums.RetrofitClient;
import okhttp3.Interceptor;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;
import retrofit2.Converter;

import java.util.Set;

/**
 * @author xiabiao
 * @date 2022-06-20
 */
class ClassPathRetrofitScanner extends ClassPathBeanDefinitionScanner {

  ClassPathRetrofitScanner(BeanDefinitionRegistry registry) {
    super(registry);
  }

  @Override
  protected boolean isCandidateComponent(MetadataReader metadataReader) {
    return metadataReader.getAnnotationMetadata().hasAnnotation(RetrofitClient.class.getName())
        && metadataReader.getAnnotationMetadata().isInterface()
        && metadataReader.getAnnotationMetadata().isIndependent();
  }

  @Override
  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
    return beanDefinition.getMetadata().isInterface()
        && beanDefinition.getMetadata().isIndependent();
  }

  @Override
  protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
    if (beanDefinitions.size() > 0) {
      processBeanDefinitions(beanDefinitions);
    }
    return beanDefinitions;
  }

  private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
    AbstractBeanDefinition beanDefinition;
    BeanDefinitionRegistry registry = getRegistry();
    for (BeanDefinitionHolder holder : beanDefinitions) {
      beanDefinition = (AbstractBeanDefinition) holder.getBeanDefinition();
      String className = beanDefinition.getBeanClassName();
      Class<?> clazz;
      try {
        assert className != null;
        clazz = ClassUtils.forName(className, Thread.currentThread().getContextClassLoader());
      } catch (ClassNotFoundException e) {
        logger.error(e);
        return;
      }
      RetrofitClient retrofitClient = clazz.getAnnotation(RetrofitClient.class);
      beanDefinition.setBeanClass(RetrofitFactoryBean.class);
      beanDefinition.getPropertyValues().add("clazz", clazz);
      beanDefinition.getPropertyValues().add("baseUrl", retrofitClient.baseUrl());
      beanDefinition
          .getPropertyValues()
          .add("interceptors", new RuntimeBeanReference(Interceptor.class));
      beanDefinition
          .getPropertyValues()
          .add("converterFactory", new RuntimeBeanReference(Converter.Factory.class));
      assert registry != null;
      registry.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
    }
  }
}
