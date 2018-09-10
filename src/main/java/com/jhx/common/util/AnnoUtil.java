package com.jhx.common.util;

import com.jhx.common.util.validate.Display;
import com.jhx.common.util.validate.SameAs;
import com.jhx.common.util.validate.SameDisplayAs;
import com.jhx.common.util.web.WebUtil;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注解相关工具类
 *
 * @author 钱智慧
 * date 6/26/18 8:21 PM
 */
public class AnnoUtil {
    /**
     * desc 注解缓存
     * key:className+propertyName
     *
     * @author 钱智慧
     * date 6/26/18 8:15 PM
     **/
    private static ConcurrentHashMap<String, Set<Annotation>> cacheMap = new ConcurrentHashMap<>();

    /**
     * desc 注解缓存
     * key:className
     *
     * @author 钱智慧
     * date 7/12/18 2:49 PM
     **/
    private static ConcurrentHashMap<String, Set<Annotation>> classCacheMap = new ConcurrentHashMap<>();


    /**
     * desc 获取指定class的属性上的所有注解
     *
     * @author 钱智慧
     **/
    public static Set<Annotation> all(Class<?> clazz, String propertyName) {
        String k = clazz.getName() + "#" + propertyName;
        return cacheMap.computeIfAbsent(k, k1 -> {
            HashSet<Annotation> m = new HashSet<>();

            ReflectUtil.reflectAllField(clazz, field -> {
                if (field.getName().equals(propertyName)) {
                    m.addAll(Arrays.asList(field.getAnnotations()));
                    return false;
                }
                return true;
            });


            return m;
        });
    }

    /**
     * desc 获取指定class上的所有注解
     *
     * @author 钱智慧
     * date 7/12/18 2:48 PM
     **/
    public static Set<Annotation> all(Class<?> aClass) {
        String k = aClass.getName();
        return classCacheMap.computeIfAbsent(k, k1 -> {
            HashSet<Annotation> m = new HashSet<>();
            m.addAll(Arrays.asList(aClass.getAnnotations()));
            return m;
        });
    }

    /**
     * desc SameAs注解相关的初始化
     *
     * @author 钱智慧
     * date 2018/7/4 下午10:18
     **/
    public static void initSameAs(String... packages) {
        try {
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(SameAs.class));
            scanner.addIncludeFilter(new AnnotationTypeFilter(SameDisplayAs.class));
            for (String pkgName : packages) {
                for (BeanDefinition bean : scanner.findCandidateComponents(pkgName)) {
                    ScannedGenericBeanDefinition sb = (ScannedGenericBeanDefinition) bean;
                    boolean b = sb.getMetadata().hasAnnotation(SameAs.class.getName());
                    if (b) {
                        setSameAs(bean.getBeanClassName());
                    } else {
                        setSameDisplayAs(bean.getBeanClassName());
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.err(WebUtil.class, e);
        }
    }

    private static void setSameDisplayAs(String fullClassName) throws NotFoundException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, CannotCompileException {
        CtClass cc = getCtClass(fullClassName);
        Class<?> targetClazz = geTargetClass(cc, SameDisplayAs.class);

        if (targetClazz != null) {
            for (CtField field : cc.getDeclaredFields()) {
                AnnotationsAttribute annosAttr = new AnnotationsAttribute(cc.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);

                //若target有Display，则其覆盖过来
                Optional<Annotation> target = AnnoUtil.all(targetClazz, field.getName()).stream().filter(a -> a.annotationType() == Display.class).findFirst();
                if (!target.isPresent()) {
                    tryAddDisplayForFromOrTo(field, targetClazz, annosAttr);
                } else {
                    javassist.bytecode.annotation.Annotation newAnno = new javassist.bytecode.annotation.Annotation(Display.class.getName(), cc.getClassFile().getConstPool());
                    annosAttr.addAnnotation(newAnno);
                }

                //自己本来有的保留
                for (Annotation annotation : selfAnnos(field).values()) {
                    javassist.bytecode.annotation.Annotation newAnno = new javassist.bytecode.annotation.Annotation(annotation.annotationType().getName(), cc.getClassFile().getConstPool());
                    setSameAs(newAnno, annotation);
                    annosAttr.addAnnotation(newAnno);
                }


                field.getFieldInfo().addAttribute(annosAttr);
            }

            Class modelClazz = cc.toClass();

            //复制Display：强制以目标类为准
            for (Field field : modelClazz.getDeclaredFields()) {
                Set<Annotation> all = AnnoUtil.all(targetClazz, field.getName());
                if (all.size() == 0) {
                    continue;
                }
                for (Annotation annotation : all) {
                    if (annotation.annotationType() == Display.class) {
                        Annotation fieldAnno = field.getDeclaredAnnotation(annotation.annotationType());
                        setSameAs(fieldAnno, annotation);
                    }
                }
            }
        }
    }

    private static void tryAddDisplayForFromOrTo(CtField field, Class<?> targetClazz, AnnotationsAttribute annosAttr) throws NoSuchFieldException, IllegalAccessException {
        //针对日期范围检索
        boolean isFrom = false;
        String targetName = null;
        if (field.getName().endsWith("From")) {
            isFrom = true;
            targetName = field.getName().substring(0, field.getName().lastIndexOf("From"));
        } else if (field.getName().endsWith("To")) {
            targetName = field.getName().substring(0, field.getName().lastIndexOf("To"));
        }
        if (targetName != null) {
            Optional<Annotation> target = AnnoUtil.all(targetClazz, targetName).stream().filter(a -> a.annotationType() == Display.class).findFirst();
            if (target.isPresent()) {
                javassist.bytecode.annotation.Annotation newAnno = new javassist.bytecode.annotation.Annotation(target.get().annotationType().getName(), field.getDeclaringClass().getClassFile().getConstPool());
                String display = Util.display(targetClazz, targetName);
                display = isFrom ? new StringBuilder("起始").append(display).toString() : new StringBuilder("截止").append(display).toString();
                newAnno.addMemberValue("value", new StringMemberValue(display, field.getDeclaringClass().getClassFile().getConstPool()));
                annosAttr.addAnnotation(newAnno);
            }
        }
    }

    private static CtClass getCtClass(String fullClassName) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(WebUtil.class);
        pool.insertClassPath(classPath);
        return pool.get(fullClassName);
    }

    private static Map<String, Annotation> selfAnnos(CtField field) throws ClassNotFoundException {
        Object[] annotations = field.getAnnotations();
        Map<String, Annotation> map = new HashMap<>();
        for (Object annotation : annotations) {
            Annotation annotation1 = (Annotation) annotation;
            map.put(annotation1.annotationType().getName(), annotation1);
        }
        return map;
    }


    private static void setSameAs(String fullClassName) throws Exception {
        CtClass cc = getCtClass(fullClassName);
        Class<?> targetClazz = geTargetClass(cc, SameAs.class);

        if (targetClazz != null) {
            for (CtField field : cc.getDeclaredFields()) {
                Set<Annotation> targetAnnos = AnnoUtil.all(targetClazz, field.getName());
                Map<String, Annotation> selfAnnos = selfAnnos(field);
                mergeAnnos(field, targetAnnos, selfAnnos);
            }

            Class modelClazz = cc.toClass();

            for (Field field : modelClazz.getDeclaredFields()) {
                Set<Annotation> all = AnnoUtil.all(targetClazz, field.getName());
                if (all.size() == 0) {
                    continue;
                }
                Field targetField = ReflectUtil.getField(targetClazz, field.getName());

                boolean sameType = Util.isSameType(targetField.getType(), field.getType());

                for (Annotation annotation : all) {
                    if (sameType || !sameType && annotation.annotationType() == Display.class) {
                        Annotation fieldAnno = field.getDeclaredAnnotation(annotation.annotationType());
                        setSameAs(fieldAnno, annotation);
                    }
                }
            }
        }
    }

    //合并注解：若target上有src没有的，则加进来，自己独有的也要加进来（优先级要高于target上的）
    private static void mergeAnnos(CtField field, Set<Annotation> targetAnnos, Map<String, Annotation> selfAnnos) throws NoSuchFieldException, IllegalAccessException {
        CtClass cc = field.getDeclaringClass();
        AnnotationsAttribute annosAttr = new AnnotationsAttribute(cc.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);

        //添加自己有的
        for (Annotation annotation : selfAnnos.values()) {
            javassist.bytecode.annotation.Annotation newAnno = new javassist.bytecode.annotation.Annotation(annotation.annotationType().getName(), cc.getClassFile().getConstPool());
            setSameAs(newAnno, annotation);
            annosAttr.addAnnotation(newAnno);
        }

        //添加target有的且自己没有的
        for (Annotation annotation : targetAnnos) {
            if (!selfAnnos.containsKey(annotation.annotationType().getName())) {
                javassist.bytecode.annotation.Annotation newAnno = new javassist.bytecode.annotation.Annotation(annotation.annotationType().getName(), cc.getClassFile().getConstPool());
                annosAttr.addAnnotation(newAnno);
            }
        }


        field.getFieldInfo().addAttribute(annosAttr);
    }

    private static void setSameAs(javassist.bytecode.annotation.Annotation src, Annotation target) throws NoSuchFieldException, IllegalAccessException {
        Map srcMap = getMemberMap(src);
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(target);
        Map targetMap = null;
        if (invocationHandler instanceof AnnotationImpl) {
            AnnotationImpl handler = (AnnotationImpl) invocationHandler;
            Field field = handler.getAnnotation().getClass().getDeclaredField("members");
            field.setAccessible(true);
            targetMap = (Map) field.get(handler.getAnnotation());
        }


        if (targetMap != null) {
            for (Object o : targetMap.keySet()) {
                Object v = targetMap.get(o);
                srcMap.put(o, v);
            }
        }
    }

    private static Class<?> geTargetClass(CtClass cc, Class<?> annoClazz) throws ClassNotFoundException {
        Object annotation = cc.getAnnotation(annoClazz);
        if (annotation == null) {
            return null;
        }
        AnnotationImpl handler = (AnnotationImpl) Proxy.getInvocationHandler(annotation);
        ClassMemberValue value = (ClassMemberValue) handler.getAnnotation().getMemberValue("value");
        if (StringUtils.isNotBlank(value.getValue())) {
            return Class.forName(value.getValue());
        } else {
            return null;
        }
    }

    private static void setSameAs(Annotation src, Annotation target) throws NoSuchFieldException, IllegalAccessException {
        Map srcMap = getMemberMap(src);
        Map targetMap = getMemberMap(target);

        for (Object o : targetMap.keySet()) {
            Object v = targetMap.get(o);
            srcMap.put(o, v);
        }
    }

    private static Map getMemberMap(Annotation annotation) throws NoSuchFieldException, IllegalAccessException {
        InvocationHandler handler = Proxy.getInvocationHandler(annotation);
        Field field = handler.getClass().getDeclaredField("memberValues");
        field.setAccessible(true);
        return (Map) field.get(handler);
    }

    private static Map getMemberMap(javassist.bytecode.annotation.Annotation annotation) throws NoSuchFieldException, IllegalAccessException {
        Field field = annotation.getClass().getDeclaredField("members");
        field.setAccessible(true);
        if (field.get(annotation) == null) {
            field.set(annotation, new LinkedHashMap<>());
        }
        return (Map) field.get(annotation);
    }
}
