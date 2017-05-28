package com.chenghui.agriculture.core.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class RestConfiguration {

	@Bean
	public View jsonTemplate(){
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		view.setPrettyPrint(false);
		return view;
	}
	
	@Bean
	public ViewResolver jsonViewResolver(){
		return new BeanNameViewResolver();
	}
}
