package com.jhx.common.layui;

import com.jhx.common.layui.dto.CheckRateModel;
import com.jhx.common.util.*;
import com.jhx.common.util.validate.PositiveRate;
import com.jhx.common.util.validate.RateControl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * @author 钱智慧
 * date 6/26/18 2:48 PM
 */
public class InputText extends InputBase {
    protected static final String PlaceholderNone = "none";
    private String placeholder;

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    private String datefmt;

    public void setDatefmt(String datefmt) {
        this.datefmt = datefmt;
    }

    @Override
    public int doEndTag() throws JspException {
        placeholder = null;
        datefmt = null;
        return super.doEndTag();
    }

    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        writeOutDiv(this::writeContent);
        return EVAL_BODY_INCLUDE;
    }

    private CheckRateModel checkRate() {
        CheckRateModel ret = new CheckRateModel();
        checkModel();
        Set<Annotation> all = AnnoUtil.all(model.getClass(), propertyName);
        boolean b1 = all.stream().anyMatch(a -> a.annotationType() == PositiveRate.class);
        if (b1) {
            return ret.setRate(b1);
        } else {

            Optional<Annotation> first = AnnoUtil.all(model.getClass()).stream().filter(a -> a.annotationType() == RateControl.class).findFirst();
            if (first.isPresent()) {
                RateControl control = (RateControl) first.get();
                int i = Arrays.asList(control.target()).indexOf(propertyName);
                if (i >= 0) {
                    ret.setRate((boolean) ReflectUtil.getValue(model, control.by()[i]));
                    ret.setControl(true);
                    return ret;
                }
            }

            return ret;
        }
    }

    private void writeContent() {
        try {
            CheckRateModel model = checkRate();
            if (model.isRate()) {
                tagWriter.startTag("i");
                tagWriter.writeAttribute("class", "append-icon");
                tagWriter.appendValue("%");
                tagWriter.endTag(true);
            }

            tagWriter.startTag(tagType);

            writePlaceholder();

            writeValueNameAndAnnoAndDynamic(tagWriter, false, true);

            writeAttr("autocomplete", "off");

            if (model.isRate()) {
                tagWriter.writeAttribute("isRate", "true");
            }

            writeValue(model);

            writeAttr("class", "layui-input");
            writeAttr("type", type);
            writeAttr("id", getElementId(propertyName));

            tagWriter.endTag(true);
        } catch (Exception e) {
            LogUtil.err(e);
        }
    }

    private void writeValue(CheckRateModel model) throws JspException, IllegalAccessException {
        String value = tryGetDateValue(forToStr());

        if (StringUtils.isNotBlank(value)) {
            if (model.isRate()) {
                value = Util.rate(new BigDecimal(value)).replace("%", "");
            } else if (model.isControl()) {
                value = Util.money(new BigDecimal(value));
            }

            writeAttr("value", value);

        }
    }

    private void writePlaceholder() throws JspException {
        if (!StringUtils.equalsIgnoreCase(PlaceholderNone, placeholder)) {
            String ph = placeholder;
            if (StringUtils.isBlank(placeholder)) {
                ph = Util.display(model.getClass(), propertyName);
            }
            writeAttr("placeholder", ph);
        }
    }

    private String tryGetDateValue(String value) throws JspException, IllegalAccessException {
        Field field = ReflectUtil.getField(model.getClass(), propertyName);
        String type = null;
        DateTimeFormatter fmt = null;
        Object dateValue = null;
        boolean isDate = false;
        if (field.getType() == LocalDate.class) {
            type = "date";
            fmt = Constant.YMD;
            isDate = true;
        } else if (field.getType() == LocalTime.class) {
            type = "time";
            fmt = Constant.HMS;
            isDate = true;
        } else if (field.getType() == LocalDateTime.class) {
            type = "datetime";
            fmt = Constant.YMDHMS;
            isDate = true;
        }
        if (isDate) {
            writeAttr("readonly", "");
            writeAttr("jhx-date", type);
            field.setAccessible(true);
            dateValue = field.get(model);
            if (dateValue != null) {
                value = fmt.format((TemporalAccessor) dateValue);
            }
        }
        return value;
    }


    public InputText() {
        type = "text";
    }
}
