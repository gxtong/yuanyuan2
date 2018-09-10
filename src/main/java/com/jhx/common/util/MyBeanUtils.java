
package com.jhx.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * 使用BeanUtils.copyProperties有一个问题就是当src对象的键值为Null时就会把target对象的对应键值覆盖成空，这不科学。
 * 这个方法是在拷贝属性时忽略空值
 */
public class MyBeanUtils extends BeanUtils {

	/**
	 * 忽略值为null的属性<br>
	 * 如果T没有无参构造,则返回null
	 * @param source
	 * @param targetClazz
	 * @return
	 */
	public static <S,T> T copy(S source, Class<T> targetClazz) {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(targetClazz, "TargetClazz must not be null");
		Class<?> sourceClass = source.getClass();
		HashMap<PropertyDescriptor, PropertyDescriptor> pdMap = getPropertyDescriptorsMap(sourceClass, targetClazz);
		try {
			T target = targetClazz.newInstance();
			copy(source, target, pdMap,true);
			return target;
		} catch (InstantiationException | IllegalAccessException e) {
			LogUtil.err(MyBeanUtils.class, e.getMessage());
		}
		return null;
	}

	public static HashMap<PropertyDescriptor, PropertyDescriptor> getPropertyDescriptorsMap(Class<?> sourceClass,
			Class<?> targetClazz) {
		PropertyDescriptor[] targetPds = getPropertyDescriptors(targetClazz);
		HashMap<PropertyDescriptor, PropertyDescriptor> pdMap = new HashMap<>();
		for (PropertyDescriptor targetPd : targetPds) {
			pdMap.put(targetPd, getPropertyDescriptor(sourceClass, targetPd.getName()));
		}
		return pdMap;
	}
	
	public static <S,T> void copy(S source, T target, HashMap<PropertyDescriptor, PropertyDescriptor> pdMap, boolean ignoreNull) {
		pdMap.forEach((PropertyDescriptor targetPd, PropertyDescriptor sourcePd) -> {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && sourcePd != null) { // && (ignoreList == null ||
															// !ignoreList.contains(targetPd.getName()))) {
				Method readMethod = sourcePd.getReadMethod();
				if (readMethod != null
						&& ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
					try {
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						if(ignoreNull && value == null) return; // 忽略掉值为null的
						if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
							writeMethod.setAccessible(true);
						}
						writeMethod.invoke(target, value);
					} catch (Throwable ex) {
						throw new FatalBeanException(
								"Could not copy property '" + targetPd.getName() + "' from source to target", ex);
					}
				}
			}
		});
	}
}