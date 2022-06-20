package com.hikvision.spring.boot.retrofit.starter;

import com.hikvision.spring.boot.retrofit.starter.enums.EnableRetrofitClient;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

/**
 * @author xiabiao
 * @date 2022-06-20
 */
public class RetrofitScanRegister implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

    AnnotationAttributes attributes =
        AnnotationAttributes.fromMap(
            importingClassMetadata.getAnnotationAttributes(EnableRetrofitClient.class.getName()));
    if (attributes != null) {
      BeanDefinitionBuilder builder =
          BeanDefinitionBuilder.genericBeanDefinition(RetrofitScanConfigure.class);
      builder.addPropertyValue(
          "basePackage", ClassUtils.getPackageName(importingClassMetadata.getClassName()));
      registry.registerBeanDefinition(
          RetrofitScanConfigure.class.getName(), builder.getBeanDefinition());
    }
  }

  private void registerBeanDifenitions(
      AnnotationMetadata annotationMetadata,
      AnnotationAttributes annotationAttributes,
      BeanDefinitionRegistry registry,
      String beanName) {}
}
