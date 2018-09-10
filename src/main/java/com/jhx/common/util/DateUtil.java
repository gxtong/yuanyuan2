package com.jhx.common.util;

import lombok.Getter;
import lombok.Setter;

import java.time.*;

/**
 * @author 钱智慧
 * @date 2017年9月22日 下午3:31:48
 */
public class DateUtil {

    private static LocalDateTime MinDate;
    private static LocalDateTime MaxDate;

    static {
        try {
            MinDate = LocalDateTime.of(1970, 1, 1, 0, 0);
            MaxDate = LocalDateTime.of(2970, 1, 1, 0, 0);
        } catch (Exception e) {
        }
    }

    /**
     * 该方法主要用于按时间范围检索时对起始和截止时间进行调整
     * 情景1、客户端传递过来的时间范围，有可能没有传递起始时间，有可能没有传递截止时间，有可能都没有传递，
     * 若Server端每次都进行这样的判断然后再拼装Sql语句未免太麻烦
     * 情景2、按天检索时，若起始和截止日期一样，其实是想查找这一天内的数据，需要对截止时间处理一下：增加一天，然后按照左闭右开的区间进行sql检索
     * <p>
     * 为了简化上述情景，可以使用该函数进行日期调整
     *
     * @param from
     * @param to
     * @param isByDay 是否按天检索
     * @return
     * @author 钱智慧
     * @date 2017年9月22日 下午4:01:32
     */
    public static AdjustDateResult adjustDate(LocalDateTime from, LocalDateTime to, boolean isByDay) {
        AdjustDateResult ret = new AdjustDateResult();
        ret.from = from == null ? MinDate : from;
        ret.to = to == null ? MaxDate : to;
        if (isByDay) {
            ret.to = ret.to.plusDays(1);
        }
        return ret;
    }

    /**
     * 该方法主要用于按时间范围检索时对起始和截止时间进行调整
     * 使用场景：客户端传递过来的时间范围，有可能没有传递起始时间，有可能没有传递截止时间，有可能都没有传递，
     * 若Server端每次都进行这样的判断然后再拼装Sql语句未免太麻烦
     * <p>
     * 为了简化上述情景，可以使用该函数进行日期调整
     * @author 钱智慧
     * date 2018/7/9 下午9:13
     **/
    public static AdjustDateResult adjustDate(LocalDateTime from, LocalDateTime to) {
        return adjustDate(from,to,false);
    }

    public static AdjustDateResult adjustDate(LocalDate from, LocalDate to) {
        return adjustDate(from == null ? null : LocalDateTime.of(from, LocalTime.MIN), to == null ? null : LocalDateTime.of(to, LocalTime.MIN), true);
    }

    /**
     * 判断target是否属于[from,to]
     *
     * @param target
     * @param from
     * @param to
     * @return
     */
    public static boolean isBetween(LocalTime target, LocalTime from, LocalTime to) {
        return !target.isBefore(from) && !target.isAfter(to);
    }

    /**
     * 判断target是否属于(from,to)
     *
     * @param target
     * @param from
     * @param to
     * @return
     */
    public static boolean isBetweenExclude(LocalTime target, LocalTime from, LocalTime to) {
        return target.isAfter(from) && target.isBefore(to);
    }

    /**
     * 判断target是否属于[from,to)
     *
     * @param target
     * @param from
     * @param to
     * @return
     */
    public static boolean isBetweenRightExclude(LocalTime target, LocalTime from, LocalTime to) {
        return !target.isBefore(from) && target.isBefore(to);
    }

    /**
     * 判断target是否属于[from,to]
     *
     * @param target
     * @param from
     * @param to
     * @return
     */
    public static boolean isBetween(LocalDate target, LocalDate from, LocalDate to) {
        return !target.isBefore(from) && !target.isAfter(to);
    }

    /**
     * 判断target是否属于[from,to]
     *
     * @param target
     * @param from
     * @param to
     * @return
     */
    public static boolean isBetween(LocalDateTime target, LocalDateTime from, LocalDateTime to) {
        return !target.isBefore(from) && !target.isAfter(to);
    }

    /**
     * desc 将unix时间戳转为LocalDateTime
     * author 钱智慧
     * date 2017/11/13 11:12
     **/
    public static LocalDateTime fromUnix(int ticks) {
        return LocalDateTime.ofEpochSecond(ticks, 0, OffsetDateTime.now().getOffset());
    }

    /**
     * desc 将unix时间戳转为LocalDateTime
     * author 钱智慧
     * date 2017/11/13 11:14
     **/
    public static int toUnix(LocalDateTime time) {
        return (int) time.toEpochSecond(OffsetDateTime.now().getOffset());
    }

    /**
     * desc 转为毫秒时间戳
     * @author 钱智慧
     * date 4/20/18 9:16 AM
     **/
    public static long toMilli(LocalDateTime time){
        return time.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
    }

    /**
     * desc 将毫秒时间戳转为LocalDateTime
     * @author 钱智慧
     * date 4/20/18 9:23 AM
     **/
    public static LocalDateTime fromMilli(long milli){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
    }

    @Getter
    @Setter
    public static class AdjustDateResult {
        private LocalDateTime from;
        private LocalDateTime to;
    }

    /**
     * 导表时的时间限制时间检索范围不能超过1个月
     *
     * @author Liu xp
     * @date 2017/12/5 14:21
     */
    public static AdjustDateResult adjustExcelDate(LocalDateTime startTime, LocalDateTime endTime, boolean isByDay) {
        if (startTime == null && endTime != null) {
            startTime = endTime.plusMonths(-1);
        } else if (startTime != null && endTime == null) {
            endTime = startTime.plusMonths(1);
        } else if (startTime == null && endTime == null) {
            endTime = LocalDateTime.now();
            startTime = endTime.plusMonths(-1);
        } else {
            if (endTime.isAfter(startTime.plusMonths(1))) {
                startTime = endTime.plusMonths(-1);
            }
        }
        return DateUtil.adjustDate(startTime, endTime, isByDay);
    }

    public static AdjustDateResult adjustExcelDate(LocalDate startTime, LocalDate endTime) {
        return adjustExcelDate(startTime == null ? null : LocalDateTime.of(startTime, LocalTime.MIN), endTime == null ? null : LocalDateTime.of(endTime, LocalTime.MIN), true);
    }

    public static AdjustDateResult adjustExcelDate(LocalDate startTime, LocalDate endTime, boolean isByDay) {
        return adjustExcelDate(startTime == null ? null : LocalDateTime.of(startTime, LocalTime.MIN), endTime == null ? null : LocalDateTime.of(endTime, LocalTime.MIN), isByDay);
    }
}
