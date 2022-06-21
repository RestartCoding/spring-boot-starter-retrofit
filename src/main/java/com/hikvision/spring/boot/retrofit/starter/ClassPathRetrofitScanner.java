package com.hikvision.spring.boot.retrofit.starter;

import com.hikvision.spring.boot.retrofit.starter.enums.RetrofitClient;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import retrofit2.Converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.Supplier;

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
      String baseUrl = getBaseUrl(retrofitClient);
      beanDefinition.getPropertyValues().add("baseUrl", baseUrl);
      beanDefinition
          .getPropertyValues()
          .add("converterFactory", new RuntimeBeanReference(Converter.Factory.class));

      beanDefinition.getPropertyValues().add("interceptors", retrofitClient.interceptors());

      assert registry != null;
      registry.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
    }
  }

  private String getBaseUrl(RetrofitClient retrofitClient) {
    String baseUrl = retrofitClient.baseUrl();
    if (ObjectUtils.isEmpty(baseUrl) && retrofitClient.baseUrlClass().length > 0) {
      try {
        Constructor<? extends Supplier<String>> constructor =
            retrofitClient.baseUrlClass()[0].getDeclaredConstructor();
        baseUrl = constructor.newInstance().get();
      } catch (NoSuchMethodException
          | InstantiationException
          | IllegalAccessException
          | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    if (ObjectUtils.isEmpty(baseUrl)) {
      throw new IllegalStateException("Base url can not be empty.");
    }
    return baseUrl;
  }
}
