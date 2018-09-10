package com.jhx.common.util.web;

import com.jhx.common.util.LogUtil;
import com.jhx.common.util.validate.Display;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * 使用方法：
 * <p>1、在web项目中配置，示例：
 * <pre> {@code
 * @literal @Configuration
 * public class WebConfig extends WebMvcConfigurerAdapter{
 * @literal @Bean
 * public static ControllerCfg create() {
 * return new ControllerCfg("com.jhx.tradegateway.http.controller");
 * }
 * }
 * }</pre>
 * <p>
 * <p>2、Controller类上方不需要添加任何注解，action方法则根据get或post请求添加GetMapping或PostMapping注解，若返回Json，
 * 则再添加ResponseBody注解。
 * <p>Controller的Controller注解、RequestMapping注解以及action的Mapping映射url都是自动生成的。
 * <p>比如对于TestController.home来说，生成的url映射路径为test/home
 * <p>
 * <p>注意：只要Controller类有了Controller或者RestController注解，则WebUtil不再为其自动生成注解，需要手动添加RequestMapping，不过WebUtil依然会为其action方法
 * 生成Mapping映射（如果action没有配置映射值的话），如：
 * <pre>{@code
 * @literal @RestController("userRegister")        //由于有了RestController注解，所以不再为其生成RequestMapping注解
 * @literal @RequestMapping("customer")
 * public class UserController implements ControllerInterface{
 * @literal @GetMapping                        //其路径为user/home
 * public String home(){
 * return view();
 * }
 *
 * @literal @GetMapping("indexPage")           //其路径为user/indexPage
 * public String index(){
 * return view();
 * }
 * }
 * }
 * </pre>
 * <p>
 * author 钱智慧
 * date 2018/1/19 20:53
 */

public class WebUtil {

    private final static String ControllerSuffix = "Controller";

    private static void setMethodMappingValue(Class<?> controllerClass) throws NoSuchFieldException, IllegalAccessException {
        for (Method m : controllerClass.getDeclaredMethods()) {
            String actionName = StringUtils.uncapitalize(m.getName());
            GetMapping getAnnotation = m.getAnnotation(GetMapping.class);
            setMappingValue(getAnnotation, actionName);
            PostMapping postAnnotation = m.getAnnotation(PostMapping.class);
            setMappingValue(postAnnotation, actionName);
        }
    }

    private static boolean checkController(CtClass cc) throws NotFoundException {
        boolean ret = false;

        if (cc == null) {
            return false;
        }

        for (CtClass item : cc.getInterfaces()) {
            if (item.getName().equals(ControllerInterface.class.getName())) {
                ret = true;
            }
        }

        if (!ret) {
            return checkController(cc.getSuperclass());
        }

        return ret;
    }

    public static Class<?> createControllerClass(String fullClassName) throws Exception {
        if (fullClassName.endsWith(ControllerSuffix)) {
            Class<?> cls;
            ClassPool pool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(WebUtil.class);
            pool.insertClassPath(classPath);
            CtClass cc = pool.get(fullClassName);

            if (!checkController(cc)) {
                return null;
            }

            String controllerMappingValue = StringUtils.uncapitalize(StringUtils.replace(cc.getSimpleName(), ControllerSuffix, ""));
            if (!hasControllerAnnotaion(cc)) {
                ClassFile ccFile = cc.getClassFile();
                ConstPool constpool = ccFile.getConstPool();
                AnnotationsAttribute attr = (AnnotationsAttribute) ccFile.getAttribute(AnnotationsAttribute.visibleTag);
                if (attr == null) {
                    attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
                }

                //添加Controller注解
                javassist.bytecode.annotation.Annotation annot1 = new javassist.bytecode.annotation.Annotation(Controller.class.getName(), constpool);
                attr.addAnnotation(annot1);

                //添加RequestMapping注解
                javassist.bytecode.annotation.Annotation annot = new javassist.bytecode.annotation.Annotation(RequestMapping.class.getName(), constpool);
                ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constpool);
                StringMemberValue sv = new StringMemberValue(constpool);
                sv.setValue(controllerMappingValue);
                MemberValue[] arr = {sv};
                arrayMemberValue.setValue(arr);
                annot.addMemberValue("value", arrayMemberValue);
                attr.addAnnotation(annot);

                ccFile.addAttribute(attr);
                cls = cc.toClass();

                if (!ControllerInterface.class.isAssignableFrom(cls)) {
                    throw new Exception(cls.getName() + "没有实现" + ControllerInterface.class.getName());
                }

                setMethodMappingValue(cls);

            } else {
                //如果手动加了RestController或Controller注解，则不再动态添加任何注解，只是尝试修改值，这种情况由于有Controller注解，
                //所以bean一早就被spring纳入了容器中，故不再返回cls
                Class springCls = Class.forName(fullClassName);
                setControllerMapping(springCls, controllerMappingValue);
                setMethodMappingValue(springCls);
                cls = null;
            }

            return cls;
        }
        return null;
    }

    private static void setControllerMapping(Class<?> cls, String controllerMappingValue) throws Exception {
        RequestMapping getAnnotation = cls.getAnnotation(RequestMapping.class);
        if (getAnnotation == null) {
            throw new Exception("RestController或Controller注解要和RequestMapping搭配使用");
        }
        setMappingValue(getAnnotation, controllerMappingValue);
    }

    private static boolean hasControllerAnnotaion(CtClass cc) throws ClassNotFoundException {
        for (Object a : cc.getAnnotations()) {
            String ac = a.toString();
            if (ac.contains(".Controller") || ac.contains(".RestController")) {
                return true;
            }
        }
        return false;
    }

    public static Set<String> getAllControllerClassName(String pkgName) {
        Set<String> ret = new HashSet<>();

        try {
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
            scanner.addIncludeFilter(new AssignableTypeFilter(ControllerInterface.class));
            for (BeanDefinition beanDefinition : scanner.findCandidateComponents(pkgName)) {
                if (beanDefinition.getBeanClassName().endsWith("Controller")) {
                    ret.add(beanDefinition.getBeanClassName());
                }
            }
        } catch (Exception e) {
            LogUtil.err(WebUtil.class, e);
        }

        return ret;
    }

    public static List<Class<?>> getAllController(String pkgName) {
        List<Class<?>> ret = new ArrayList<>();

        try {
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
            scanner.addIncludeFilter(new AssignableTypeFilter(ControllerInterface.class));
            for (BeanDefinition beanDefinition : scanner.findCandidateComponents(pkgName)) {
                Class<?> controllerClass = Class.forName(beanDefinition.getBeanClassName());
                if (ControllerInterface.class.isAssignableFrom(controllerClass)) {
                    ret.add(controllerClass);
                }
            }
        } catch (Exception e) {
            LogUtil.err(WebUtil.class, e);
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    private static void setMappingValue(Annotation annotation, String newValue) throws NoSuchFieldException, IllegalAccessException {
        if (annotation == null) {
            return;
        }
        InvocationHandler h = Proxy.getInvocationHandler(annotation);
        Field hField = h.getClass().getDeclaredField("memberValues");
        hField.setAccessible(true);
        // 获取 memberValues
        Map memberValues = (Map) hField.get(h);
        Object value = memberValues.get("value");
        if (value == null || ((String[]) value).length == 0) {
            String[] arr = {newValue};
            memberValues.put("value", arr);
        }
    }


    /**
     * desc 从bindingResult生成错误信息
     * author 钱智慧
     * date 2018/2/5 21:54
     **/
    public static String getErrMsg(BindingResult bindingResult) {
        String ret = "没有错误";
        try {
            for (FieldError error : bindingResult.getFieldErrors()) {
                Field field = bindingResult.getTarget().getClass().getDeclaredField(error.getField());
                Display displayAnnotation = field.getDeclaredAnnotation(Display.class);
                ret = displayAnnotation.value() + "：" + error.getDefaultMessage();
                return ret;
            }
        } catch (Exception e) {
            LogUtil.err(WebUtil.class, e);
        }


        return ret;
    }
}
