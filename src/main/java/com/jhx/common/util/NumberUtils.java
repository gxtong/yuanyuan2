package com.jhx.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;

/**
 * 数字类型相关的工具
 * 
 * @author t.ch
 * @time 2017-08-24 12:05:36
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils{

	/**
	 * <p>
	 * 比较两个 {@code BigDecimal} 类型的大小.
	 * </p>
	 * 
	 * <pre>
	 * NumberUtils.contains(0, 0)   = 0
	 * NumberUtils.contains(null, 0)   = 0
	 * NumberUtils.contains(null, 2.1) = -1
	 * NumberUtils.contains(2, null)  = 1
	 * NumberUtils.contains(2, 1.3)   = 1
	 * </pre>
	 *
	 * @param x
	 *            the first {@code BigDecimal} to compare
	 * @param y
	 *            the second {@code BigDecimal} to compare
	 * @return {@code x == y} 返回 {@code 0}<br>
	 *         {@code x < y} 返回 {@code -1}<br>
	 *         {@code x > y} 返回 {@code 1}<br>
	 */
	public static int compare(BigDecimal x, BigDecimal y) {
		if (x == null)
			x = BigDecimal.ZERO;
		if (y == null)
			y = BigDecimal.ZERO;
		return x.compareTo(y);
	}
	
	/**
	 * <p>
	 * {@code String} 转 {@code BigDecimal}
	 * </p>
	 * 
	 * <pre>
	 * NumberUtils.parseBigDecimal(null) = BigDecimal.ZERO
	 * NumberUtils.parseBigDecimal("ABC") = BigDecimal.ZERO
	 * NumberUtils.parseBigDecimal("20.34") = new BigDecimal(20.34)
	 * </pre>
	 *
	 * @param src
	 * @return 
	 */
	public static BigDecimal parseBigDecimal(String src) {
		if(StringUtils.isBlank(src)) {
			return BigDecimal.ZERO;
		}
		try {
			return new BigDecimal(src);
		}catch(NumberFormatException e) {
			return BigDecimal.ZERO;
		}
	}
	
	/**
	 * @return (x) * 100保留两位小数
	 */
	public static BigDecimal multiplyFloatBaseWithRate(BigDecimal x) {
		return x.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.DOWN);
	}

	/**
	 * @return (x) * 100取整
	 */
	public static BigDecimal multiplyInteger(BigDecimal x) {
		return x.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.DOWN);
	}
	
	/**
	 * 包装了BigDecimal本身的“加”操作，参数为null的话会作为0处理
	 * @param left
	 * @param right
	 * @return
	 * @author 钱智慧
	 * @date 2017年11月2日 下午6:07:37
	 */
	public static BigDecimal add(BigDecimal left,BigDecimal right) {
		if(left==null) {
			left=BigDecimal.ZERO;
		}
		if(right==null) {
			right=BigDecimal.ZERO;
		}
		return left.add(right);
	}
	
	/**
	 *  包装了BigDecimal本身的“减”操作，参数为null的话会作为0处理
	 * @param left
	 * @param right
	 * @return
	 * @author 钱智慧
	 * @date 2017年11月2日 下午6:08:30
	 */
	public static BigDecimal subtract(BigDecimal left,BigDecimal right) {
		if(left==null) {
			left=BigDecimal.ZERO;
		}
		if(right==null) {
			right=BigDecimal.ZERO;
		}
		return left.subtract(right);
	}

}
