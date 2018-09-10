package com.jhx.common.layui;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.jhx.common.layui.dto.BaseModel;
import com.jhx.common.layui.dto.GetDataMapModel;
import com.jhx.common.layui.dto.RateModel;
import com.jhx.common.util.*;
import com.jhx.common.util.db.DbConstant;
import com.jhx.common.util.validate.RateControl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 钱智慧
 * date 6/26/18 9:11 PM
 */
public class LayUtil {
    //key:require,min_len……
    private static List<Map<String, BaseModel>> valMapList = new ArrayList<>(ValType.values().length);

    public static ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    private enum ValType {
        require,
        min_len,
        max_len,
        min_value,
        max_value,
        integer_num,
        fraction_num,
        reg_ex
    }

    static {
        for (int i = 0; i < ValType.values().length; i++) {
            valMapList.add(new HashMap<>());
        }
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * desc 输出客户端验证
     *
     * @author 钱智慧
     * date 6/28/18 5:49 PM
     **/
    public static void writeClientValidate(TagWriter tagWriter, Set<Annotation> annotations, Object model, String property) throws JspException {
        if (model == null) {
            return;
        }

        if (writeRateValidate(tagWriter, model, property)) {
            return;
        }

        for (int i = 0; i < valMapList.size(); i++) {
            ValType type = ValType.values()[i];
            writeClientValidate(tagWriter, annotations, type, model, property);
        }
    }

    public static GetDataMapModel getDataMapForBoolean(Base layBase, Object model, String propertyName) {
        GetDataMapModel ret = new GetDataMapModel();
        if (layBase.getData() != null) {
            return ret;
        }
        Field field = ReflectUtil.getField(model.getClass(), propertyName);
        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            String[] arr = Util.displayArr(model.getClass(), propertyName);
            String trueDisplay = StringUtils.isBlank(arr[1]) ? "是" : arr[1];
            String falseDisplay = StringUtils.isBlank(arr[2]) ? "否" : arr[2];

            Optional<Annotation> first = AnnoUtil.all(model.getClass()).stream().filter(a -> a.annotationType() == RateControl.class).findFirst();
            if (first.isPresent()) {
                RateControl control = (RateControl) first.get();
                for (int i = 0; i < control.by().length; i++) {
                    if (control.by()[i].equals(propertyName)) {
                        ret.getTargetNames().add(control.target()[i]);
                    }
                }
                if (ret.getTargetNames().size() > 0) {
                    trueDisplay = "定比";
                    falseDisplay = "定值";
                    ret.setRate(true);
                }
            }
            layBase.setData(ImmutableMap.of(false, falseDisplay, true, trueDisplay));
        }

        return ret;
    }

    public static boolean writeRateValidate(TagWriter tagWriter, Object model, String property) throws JspException {
        for (Annotation annotation : AnnoUtil.all(model.getClass())) {
            if (annotation instanceof RateControl) {
                RateControl control = (RateControl) annotation;
                int index = Arrays.asList(control.target()).indexOf(property);
                if (index < 0) {
                    continue;
                }

                Assert.isTrue(control.by().length == control.target().length, "RateControl注解的数组大小要一样");

                boolean rateCanBeZero;
                boolean decimalCanBeZero;

                if (control.rateCanBeZero().length == 1) {
                    rateCanBeZero = control.rateCanBeZero()[0];
                } else {
                    rateCanBeZero = control.rateCanBeZero()[index];
                }

                if (control.decimalCanBeZero().length == 1) {
                    decimalCanBeZero = control.decimalCanBeZero()[0];
                } else {
                    decimalCanBeZero = control.decimalCanBeZero()[index];
                }

                try {
                    String json = Constant.JacksonMapper.writeValueAsString(new RateModel(3, 2, 0, rateCanBeZero, 100, true)).replace('\"', '\'');
                    json = json.replace("_", "-");
                    tagWriter.writeAttribute("rate-true", json);

                    String json2 = Constant.JacksonMapper.writeValueAsString(new RateModel(DbConstant.DecimalPrecision, 2, 0, decimalCanBeZero, 9999999l, false)).replace('\"', '\'');
                    json2 = json2.replace("_", "-");
                    tagWriter.writeAttribute("rate-false", json2);
                } catch (Exception e) {
                    LogUtil.err(e);
                }

                return true;
            }
        }
        return false;
    }

    public static boolean isRequired(Object model, String property) {
        Set<Annotation> all = AnnoUtil.all(model.getClass(), property);
        BaseModel orAdd = getOrAdd(all, ValType.require, model, property);
        return orAdd.isHasAnnotation();
    }

    private static BaseModel getOrAdd(Set<Annotation> annotations, ValType type, Object model, String property) {
        String key = getKey(model, property);
        Map<String, BaseModel> map = valMapList.get(type.ordinal());

        BaseModel am = map.computeIfAbsent(key, k -> {
            BaseModel ret = new BaseModel();
            for (Annotation annotation : annotations) {
                Field propField = ReflectUtil.getField(model.getClass(), property);

                BaseModel annotationModel = getAnnoModel(annotation, type, propField);
                if (annotationModel != null) {
                    ret = annotationModel;
                    try {
                        ret.setJson(mapper.writeValueAsString(ret).replace('\"', '\''));
                        break;
                    } catch (JsonProcessingException e) {
                        LogUtil.err(e);
                    }
                }
            }
            return ret;
        });
        return am;
    }

    //require
    private static void writeClientValidate(TagWriter tagWriter, Set<Annotation> annotations, ValType type, Object model, String property) throws JspException {
        BaseModel am = getOrAdd(annotations, type, model, property);
        if (am.isHasAnnotation()) {
            tagWriter.writeAttribute(type.name().replace("_", "-"), am.getJson());
        }
    }

    private static BaseModel getAnnoModel(Annotation annotation, ValType type, Field propField) {
        switch (type) {
            case require:
                return ModelUtil.getRequiredModel(annotation, propField);
            case min_len:
                return ModelUtil.getMinMaxLenModel(annotation, true, propField);
            case max_len:
                return ModelUtil.getMinMaxLenModel(annotation, false, propField);
            case min_value:
                return ModelUtil.getMinMaxValueModel(annotation, true, propField);
            case max_value:
                return ModelUtil.getMinMaxValueModel(annotation, false, propField);
            case integer_num:
                return ModelUtil.getIntegerNumModel(annotation, propField);
            case fraction_num:
                return ModelUtil.getFractionNumModel(annotation, propField);
            case reg_ex:
                return ModelUtil.getRegExModel(annotation, propField);
            default:
                return null;
        }
    }

    private static String getKey(Object model, String property) {
        String key = new StringBuilder(model.getClass().getName()).append("#").append(property).toString();
        return key;
    }
}
