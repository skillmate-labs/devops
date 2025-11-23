package com.skillmate.skillmate.config;

import java.util.Locale;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(3600);
    messageSource.setFallbackToSystemLocale(false);
    messageSource.setDefaultLocale(Locale.ENGLISH);
    return messageSource;
  }

  @Bean
  public LocaleResolver localeResolver() {
    return new CustomLocaleResolver();
  }

  @Bean
  public FilterRegistrationBean<OncePerRequestFilter> localeContextFilter() {
    FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new LocaleContextFilter((CustomLocaleResolver) localeResolver()));
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
    registration.addUrlPatterns("/api/*");
    return registration;
  }

  @Bean
  public LocalValidatorFactoryBean validator(MessageSource messageSource) {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }

  @Override
  public org.springframework.validation.Validator getValidator() {
    return validator(messageSource());
  }
}
