package com.hikvision.spring.boot.retrofit.starter;

import com.hikvision.spring.boot.retrofit.starter.enums.EnableRetrofitClient;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
      List<String> basePackages = new ArrayList<>();
      basePackages.addAll(
          Arrays.stream(attributes.getStringArray("value"))
              .filter(StringUtils::hasText)
              .collect(Collectors.toList()));

      basePackages.addAll(
          Arrays.stream(attributes.getStringArray("basePackages"))
              .filter(StringUtils::hasText)
              .collect(Collectors.toList()));

      basePackages.addAll(
          Arrays.stream(attributes.getClassArray("basePackageClasses"))
              .map(ClassUtils::getPackageName)
              .collect(Collectors.toList()));

      if (basePackages.isEmpty()) {
        basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
      }
      builder.addPropertyValue(
          "basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));

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
