package com.fruitshop.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware{

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	    context = applicationContext;
	}
	
	public static <T> T getBean(Class<T> beanClass) {
    if (context == null) {
        throw new IllegalStateException("ApplicationContext has not been set");
    }
    return context.getBean(beanClass);
}

}
