package com.jhx.common.layui;

import com.jhx.common.util.AnnoUtil;
import com.jhx.common.util.Constant;
import com.jhx.common.util.EnumUtil;
import com.jhx.common.util.LogUtil;
import com.jhx.common.util.encrypt.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.el.ELClass;
import javax.el.ELContext;
import javax.el.EvaluationListener;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;

/**
 * 每类签标运行时虽然只有一个实例，但是线程安全的，具体可以参考TagHandlerPool
 *
 * @author 钱智慧
 * date 6/26/18 2:48 PM
 */
public abstract class Base extends AbstractHtmlElementTag {

    protected TagWriter tagWriter;
    private Set<Annotation> annotations;
    protected Object model;
    protected String propertyName;

    //标签类型，比如input textarea等
    protected String tagType;

    protected Object _for;

    public void setFor(Object _for) {
        this._for = _for;
    }

    protected String extra = null;

    public void setExtra(String extra) {
        this.extra = extra;
    }

    protected Object data;

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    protected String divClass = DivBlock;

    protected boolean auto;

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    protected static final String DivNone = "none";
    protected static final String DivInline = "layui-input-inline";
    protected static final String DivBlock = "layui-input-block";

    @Override
    public int doEndTag() throws JspException {
        model = null;
        propertyName = null;
        _for = null;
        divClass = DivBlock;
        extra = null;
        data = null;
        tagWriter = null;
        auto = false;
        return super.doEndTag();
    }

    public void setDivClass(String cls) {
        if (StringUtils.isBlank(cls) || StringUtils.equalsIgnoreCase(cls, "block")) {
            cls = DivBlock;
        } else if (StringUtils.equalsIgnoreCase(cls, "inline")) {
            cls = DivInline;
        }

        divClass = cls;
    }

    protected String forToStr() {
        if (_for == null) {
            return "";
        } else {
            if (_for instanceof BigDecimal && ((BigDecimal) _for).signum() == 0) {
                return ((BigDecimal) _for).toPlainString();
            } else {
                return _for.toString();
            }
        }
    }

    //以页面url生成md5串
    protected String getPageId() {
        return Md5Util.md5Hex(getRequestContext().getRequestUri());
    }

    //为数据项生成唯一标识作为html元素的id
    protected String getElementId(String propertyName) {
        return new StringBuilder(propertyName).append("_").append(getPageId()).toString();
    }

    protected void writeValueNameAndAnnoAndDynamic(TagWriter tagWriter, boolean writeForAsValue, boolean writeValidation) throws JspException {
        checkModel();

        if (writeForAsValue) {
            if (StringUtils.isNotBlank(forToStr())) {
                writeAttr("value", forToStr());
            }
        }

        if (writeValidation) {
            LayUtil.writeClientValidate(tagWriter, annotations, model, propertyName);
        }

        writeAttr("name", propertyName);

        writeDynamicAttrs();
    }

    protected void checkModel() {
        Assert.isTrue(model != null, "页面上的model数据不能为空");
    }

    protected void writeDynamicAttrs() throws JspException {
        if (!CollectionUtils.isEmpty(getDynamicAttributes())) {
            for (String attr : getDynamicAttributes().keySet()) {
                tagWriter.writeAttribute(attr, getDisplayString(getDynamicAttributes().get(attr)));
            }
        }
    }

    protected void writeAttr(String attrName, String value) throws JspException {
        if (!isDynamic(attrName)) {
            tagWriter.writeAttribute(attrName, value);
        }
    }

    protected boolean isDynamic(String attrName) {
        return getDynamicAttributes() != null && getDynamicAttributes().containsKey(attrName);
    }

    @Override
    protected TagWriter createTagWriter() {
        tagWriter = super.createTagWriter();
        return tagWriter;
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

                    propertyName = property.toString();
                    model = base;
                    annotations = AnnoUtil.all(model.getClass(), propertyName);
                }
            }
        });
    }

    protected void writeOutDiv(Runnable func) throws JspException {
        boolean hasOutDiv = StringUtils.isNotBlank(divClass) && !DivNone.equals(divClass);
        if (hasOutDiv) {
            tagWriter.startTag("div");
            writeAttr("class", divClass);
        }

        func.run();

        if (hasOutDiv) {
            tagWriter.endTag(true);
        }
    }

    protected void writeMapOrEnum(WriteFunction<String, Object, Boolean> writer) {
        Assert.isTrue(data != null, "data数据为空");

        try {
            if (data instanceof Map<?, ?>) {
                Map<?, ?> map = (Map<?, ?>) data;
                for (Object key : map.keySet()) {
                    Object value = map.get(key);
                    writer.write(value.toString(), key, false);
                }
            } else if (data.getClass().isArray()) {
                for (Enum item : (Enum[]) data) {
                    String display;

                    if (item instanceof DayOfWeek) {
                        display = Constant.DayOfWeekMap.get((DayOfWeek) item);
                    } else {
                        display = EnumUtil.getDisplay(item);
                    }

                    writer.write(display, item.name(), true);
                }
            } else {
                String errMsg = new StringBuilder("data类型只能是枚举数组或者Map，不支持：").append(data.getClass().getName()).toString();
                LogUtil.err(errMsg);
                throw new RuntimeException(errMsg);
            }
        } catch (Exception e) {
            LogUtil.err(e);
        }
    }

    @FunctionalInterface
    public interface WriteFunction<TText, TValue, TBool> extends Serializable {
        void write(TText text, TValue value, TBool isEnum) throws JspException;
    }
}
