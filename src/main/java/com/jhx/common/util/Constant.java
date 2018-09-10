package com.jhx.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Constant {
    public static final String BadArg = "参数非法";
    public static final String NoAuth = "无权操作";
    public static final String PatternPhone = "^1[3456789]\\d{9}$";
    public static final String PatternPhoneOrEmpty = "^(1[3456789]\\d{9})|(\\s?)$";

    //ip或域名
    public static final String PatternIpOrDomain = "^(?:(?:\\w+\\.)+[a-zA-Z]+)$|^(?:(?:25[0-5]|2[0-4]\\d|(?:(?:1\\d{2})|(?:[1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|(?:(?:1\\d{2})|(?:[1-9]?\\d)))$";

    //ip
    public static final String PatternIp = "(((\\d{1,2})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))";

    /**
     * HH:mm:ss"正则
     */
    public static final String PatternHHmmss = "^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
    public static final String RequiredTip = "{display}必填";

    public static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter HMS = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter YMDHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter YMDHMSSSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    /**
     * 不建议使用gson，其性能远不如Jackson
     *
     * @see Constant#JacksonMapper
     */
    @Deprecated
    public static final Gson gson = new GsonBuilder().setPrettyPrinting() // 对json格式化
            .registerTypeHierarchyAdapter(String.class, new JsonDeserializer<String>() {
                @Override
                public String deserialize(JsonElement json, Type type,
                                          JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    String src = json.getAsJsonPrimitive().getAsString();
                    if (src != null) {
                        return src.trim();
                    }
                    return null;
                }
            })
            .registerTypeHierarchyAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                    if (src != null) {
                        return new JsonPrimitive(src.format(YMDHMS));
                    }
                    return new JsonPrimitive("");
                }
            }).registerTypeHierarchyAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                    if (src != null) {
                        return new JsonPrimitive(src.format(YMD));
                    }
                    return new JsonPrimitive("");
                }
            }).registerTypeHierarchyAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type type,
                                                 JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    String datetime = json.getAsJsonPrimitive().getAsString();
                    if (StringUtils.isNotBlank(datetime)) {
                        return LocalDateTime.parse(datetime, YMDHMS);
                    }
                    return null;
                }
            }).registerTypeHierarchyAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type type,
                                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    String datetime = json.getAsJsonPrimitive().getAsString();
                    if (StringUtils.isNotBlank(datetime)) {
                        return LocalDate.parse(datetime, YMD);
                    }
                    return null;
                }
            }).setDateFormat("yyyy-MM-dd HH:mm:ss") // 时间格式化
            .create();

    /**
     * desc 基于MessagePack的Jackson Mapper
     *
     * @author 钱智慧
     * date 4/4/18 11:21 AM
     * @see Constant#JacksonMapper
     **/
    @Deprecated
    public static ObjectMapper MpMapper = new ObjectMapper(new MessagePackFactory())
            .findAndRegisterModules();

    public static ObjectMapper JacksonMapper = new ObjectMapper().findAndRegisterModules();

    public static LinkedHashMap<String, String> CodeMap = new LinkedHashMap<String, String>() {
        {
            put("AUDUSD", "澳元");
            put("USDJPY", "日元");
            put("USDCHF", "瑞士法郎");
            put("GBPUSD", "英镑");
            put("EURUSD", "欧元");
            put("NZDUSD", "新西兰元");
            put("USDCAD", "加元");
            put("XAGUSD", "白银");
            put("XAUUSD", "黄金");
            put("CL_Spot", "原油");
            put("COPPER", "铜");
            put("COFFEE", "咖啡");
            put("HSI", "恒生指数");
            put("A50", "富时中国");
            put("BTCUSD", "比特币美元");
            put("ETHUSD", "以太坊美元");
            put("LTCUSD", "莱特币美元");
            put("BTCUSDT", "比特币泰达币");
            put("ETHUSDT", "以太坊泰达币");
            put("LTCUSDT", "莱特币泰达币");
            put("BCHUSDT", "比特现金泰达币");
            put("ETCUSDT", "以太坊经典泰达币");
            put("EOSUSDT", "柚子币泰达币");
            put("DASHUSDT", "达世币泰达币");
        }
    };

    public static LinkedHashMap<String, String> codeMap() {
        LinkedHashMap<String, String> ret = new LinkedHashMap<>(CodeMap.size());
        CodeMap.forEach((code, name) -> ret.put(code, name + "【" + code + "】"));
        return ret;
    }

    public static Map<DayOfWeek, String> DayOfWeekMap = new LinkedHashMap<DayOfWeek, String>() {
        {
            put(DayOfWeek.MONDAY, "周一");
            put(DayOfWeek.TUESDAY, "周二");
            put(DayOfWeek.WEDNESDAY, "周三");
            put(DayOfWeek.THURSDAY, "周四");
            put(DayOfWeek.FRIDAY, "周五");
            put(DayOfWeek.SATURDAY, "周六");
            put(DayOfWeek.SUNDAY, "周日");
        }
    };
}
