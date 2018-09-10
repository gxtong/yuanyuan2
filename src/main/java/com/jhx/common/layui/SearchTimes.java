package com.jhx.common.layui;

import com.jhx.common.util.*;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.el.ELClass;
import javax.el.ELContext;
import javax.el.EvaluationListener;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Set;

/**
 * @author 钱智慧
 * date 2018/7/1 下午9:03
 */
public class SearchTimes extends AbstractHtmlElementTag {
    protected TagWriter tagWriter;
    protected Object model;
    protected String propertyNameFrom;
    protected String propertyNameTo;

    private Set<Annotation> annotationsFrom;
    private Set<Annotation> annotationsTo;

    private String from;
    private String to;

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public int doEndTag() throws JspException {
        tagWriter = null;
        model = null;
        from = null;
        propertyNameFrom = null;
        to = null;
        propertyNameTo = null;
        annotationsFrom=null;
        annotationsTo=null;
        return super.doEndTag();
    }

    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        //写from
        writeOutDiv(() -> writeInput(true));
        //写分隔线
        writeSeparator();
        //写to
        writeOutDiv(() -> writeInput(false));
        return EVAL_BODY_INCLUDE;
    }

    private void writeSeparator() {
        try {
            tagWriter.startTag("div");
            tagWriter.writeAttribute("class", "layui-input-inline");
            tagWriter.appendValue("&nbsp-&nbsp");
            tagWriter.endTag();
        } catch (Exception e) {
            LogUtil.err(e);
        }
    }


    private void writePlaceholder(boolean isFrom) {
        try {
            String display = Util.display(model.getClass(), isFrom ? propertyNameFrom : propertyNameTo);
            tagWriter.writeAttribute("placeholder", display);
        } catch (Exception e) {
            LogUtil.err(e);
        }
    }

    private void writeValue(String value, boolean isFrom) {
        Field field = ReflectUtil.getField(model.getClass(), isFrom ? propertyNameFrom : propertyNameTo);
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
            try {
                tagWriter.writeAttribute("jhx-date", type);
                tagWriter.writeAttribute("readonly", "");
                field.setAccessible(true);
                dateValue = field.get(model);
                if (dateValue != null) {
                    value = fmt.format((TemporalAccessor) dateValue);
                }
                tagWriter.writeAttribute("value", value);
            } catch (Exception e) {
                LogUtil.err(e);
            }
        }
    }

    @Override
    protected TagWriter createTagWriter() {
        tagWriter = super.createTagWriter();
        return tagWriter;
    }

    protected void writeOutDiv(Runnable func) throws JspException {
        tagWriter.startTag("div");
        tagWriter.writeAttribute("class", "layui-input-inline");

        func.run();

        tagWriter.endTag(true);
    }

    protected void writeInput(boolean isFrom) {
        try {

            tagWriter.startTag("input");
            tagWriter.writeAttribute("class", "layui-input");

            tagWriter.writeAttribute("name",isFrom?propertyNameFrom:propertyNameTo);

            writePlaceholder(isFrom);
            writeValue(isFrom ? from : to, isFrom);

            tagWriter.endTag(true);
        } catch (Exception e) {
            LogUtil.err(e);
        }
    }

    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        pageContext.getELContext().addEvaluationListener(new EvaluationListener() {
            @Override
            public void propertyResolved(ELContext context, Object base, Object property) {
                super.propertyResolved(context, base, property);

                if (base != null) {

                    //排除枚举绑定，如：<lay:checks for="${model.hobby}"  data="${Hobby.values()}"  />
                    if (base instanceof ELClass) {
                        if (((ELClass) base).getKlass().isEnum()) {
                            return;
                        }
                    }

                    model = base;
                    if (property.toString().endsWith("From")) {
                        propertyNameFrom = property.toString();
                        annotationsFrom = AnnoUtil.all(model.getClass(), propertyNameFrom);
                    } else if (property.toString().endsWith("To")) {
                        propertyNameTo = property.toString();
                        annotationsTo = AnnoUtil.all(model.getClass(), propertyNameTo);
                    }

                }
            }
        });
    }
}
