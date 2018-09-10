package com.jhx.common.util;

import com.jhx.common.util.db.DbConstant;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.internal.util.ReflectHelper;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* @author 钱智慧
* @date 2017年9月13日 下午6:16:16
*/

@SuppressWarnings("unchecked")
public class CollectionUtil {
	/**
	 * desc 对集合List进行排序
	 * @param orderDirection 排序方向 asc升序，desc降序
	 * @param orderField 排序字段
	 * author 钱智慧
	 * date 2018/2/8 16:59
	 **/
	public static <T> List<T> sort(List<T> data, String orderField, String orderDirection) {

		if (data.size() == 0 || StringUtils.isBlank(orderField) || StringUtils.isBlank(orderDirection)) {
			return data;
		}

		if (!DbConstant.OrderDirections.contains(orderDirection.toLowerCase())) {
			throw new RuntimeException("排序方向" + orderDirection + "非法");
		}

		try {
			Class<?> clazz = data.get(0).getClass();
			final Method getter = ReflectHelper.findGetterMethod(clazz,orderField);
			Class<?> returnType = getter.getReturnType();
			if (Comparable.class.isAssignableFrom(returnType) || returnType.isPrimitive()) {
				data.sort((e1, e2) -> {
					try {
						Comparable val1 = (Comparable) getter.invoke(e1);
						Comparable val2 = (Comparable) getter.invoke(e2);
						return orderDirection.equals("asc") ? val1.compareTo(val2) : val2.compareTo(val1);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
			}else{
				throw new RuntimeException(orderField+"不支持排序");
			}
		} catch (Exception e) {
			LogUtil.err(CollectionUtil.class, e);
		}

		return data;
	}

	/**
	 *  将From类型的List复制成To类型的List
	 * 复制list,忽略值为null的属性<br>
	 * 如果sourceList为空或者T没有无参构造,则返回空列表
	 * @param sourceList
	 * @param targetClazz
	 * @return 
	 */
	public static <S,T> List<T> copy(List<S> sourceList, Class<T> targetClazz) {
		List<T> list = new ArrayList<>();
		if (sourceList == null || sourceList.size() < 1) {
			return list;
		}
		Assert.notNull(targetClazz, "TargetClazz must not be null");
		try {
			targetClazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LogUtil.err(MyBeanUtils.class, e.getMessage());
			return list;
		}

		Class<?> sourceClass = sourceList.get(0).getClass();
		HashMap<PropertyDescriptor, PropertyDescriptor> pdMap = MyBeanUtils.getPropertyDescriptorsMap(sourceClass, targetClazz);
		sourceList.stream().forEach(source -> {
			try {
				T target = targetClazz.newInstance();
				MyBeanUtils.copy(source, target, pdMap,true);
				list.add(target);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException("缺少无参构造");
			}
		});
		return list;
	}
}
