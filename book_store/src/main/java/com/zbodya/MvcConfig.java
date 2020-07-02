package com.zbodya;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


@Configuration
public class MvcConfig implements WebMvcConfigurer
{

	@Override
	public void addViewControllers(ViewControllerRegistry registry)
	{
		registry.addViewController("/login").setViewName("service/login");
	}

	@Bean(name = "localeResolver")
	public LocaleResolver getLocaleResolver() 
	{		
		CookieLocaleResolver resolver = new CookieLocaleResolver();			
//		resolver.setCookieDomain("localeCookie");	
//		resolver.setCookieMaxAge(60*60);
		return resolver;
	}
	
	@Bean(name = "messageSource")
	public MessageSource getMessageSource() 
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) 
	{
		LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
		localeInterceptor.setParamName("lang");
		registry.addInterceptor(localeInterceptor).addPathPatterns("/**");
	}
	
	
	
	

	
	
}
