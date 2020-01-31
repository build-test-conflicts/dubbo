package org.apache.dubbo.config.spring.context.annotation;
import org.apache.dubbo.config.AbstractConfig;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.apache.dubbo.config.spring.util.AnnotatedBeanDefinitionRegistryUtils.registerBeans;
/** 
 * Dubbo  {@link AbstractConfig Config} {@link ImportBeanDefinitionRegistrar register}, which order can be configured
 * @see EnableDubboConfig
 * @see DubboConfigConfiguration
 * @see Ordered
 * @since 2.5.8
 */
public class DubboConfigConfigurationRegistrar implements ImportBeanDefinitionRegistrar {
  @Override public void registerBeanDefinitions(  AnnotationMetadata importingClassMetadata,  BeanDefinitionRegistry registry){
    AnnotationAttributes attributes=AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableDubboConfig.class.getName()));
    boolean multiple=attributes.getBoolean("multiple");
    if (multiple) {
      registerBeans(registry,DubboConfigConfiguration.Multiple.class);
    }
 else {
      registerBeans(registry,DubboConfigConfiguration.Single.class);
    }
  }
}
