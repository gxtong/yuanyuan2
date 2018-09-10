package com.jhx.common.util.web;

import com.jhx.common.util.AnnoUtil;
import com.jhx.common.util.LogUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * spring mvc引入这个配置类后，会将程序中所有实现了ControllerInterface接口且类名以"Controller"结尾
 * 的Java类视为Controller，并自动为该类添加"@Controller、@RequestMapping(类名)"注解，同时会为action
 * 方法的@GetMapping和@PostMapping注解添加方法名映射。会优先采用开发者自己添加的注解
 * <pre>使用方式如下（即给Spring注入一个ControllerCfg实例即可）：</pre>
 *
 * <pre>
 * {@code
 * @literal @Configuration
 * public class Config {
 * @literal @Bean
 * public static ControllerCfg create(){
 * return new ControllerCfg("com.jhx.controller");
 * }
 * }
 * }
 *
 * </pre>
 * <p>
 * author 钱智慧
 * date 2018/1/20 19:42
 */
public class JhxConfig implements BeanDefinitionRegistryPostProcessor {

    private String appPkgName;

    public JhxConfig(Class<?> appClazz, String... extraPackages) {
        appPkgName = appClazz.getPackage().getName();

        //初始化SameAs注解
        List<String> list = new ArrayList<>(Arrays.asList(extraPackages));
        list.add(appPkgName);
        AnnoUtil.initSameAs(list.toArray(new String[0]));
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            for (String className : WebUtil.getAllControllerClassName(appPkgName)) {
                //创建Controller class，设置注解
                Class<?> controllerClass = WebUtil.createControllerClass(className);
                if (controllerClass != null) {
                    //注册到Spring容器中
                    GenericBeanDefinition bd = new GenericBeanDefinition();
                    bd.setBeanClass(controllerClass);
                    registry.registerBeanDefinition(controllerClass.getSimpleName(), bd);
                }
            }
        } catch (Exception e) {
            LogUtil.err(e);
        }
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
