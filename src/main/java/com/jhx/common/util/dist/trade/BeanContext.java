package com.jhx.common.util.dist.trade;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 用来手动获取ApplicationContext 和 Bean
 * @author t.ch
 * @time 2017-08-17 20:54:35
 * @description Description for this class
 */
public class BeanContext  {


	private static ApplicationContext APPLICATION_CONTEXT;

	public static void setApplicationContext(ApplicationContext applicationContext)  {
		APPLICATION_CONTEXT = applicationContext;
	}
	
	/**
	 * 获取ApplicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return APPLICATION_CONTEXT;
	}
	
	/**
	 * 根据bean名获取bean
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return APPLICATION_CONTEXT.getBean(beanName);
	}
	
	/**
	 * 根据Class获取bean
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return APPLICATION_CONTEXT.getBean(clazz);
	}
}
