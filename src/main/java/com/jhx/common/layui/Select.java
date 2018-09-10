package com.jhx.common.layui;

import com.jhx.common.util.LogUtil;
import com.jhx.common.util.Util;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 2:48 PM
 */
@Accessors(chain = true)
public class Select extends Base {
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        writeOutDiv(this::writeSelect);
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    @Override
    protected void writeOutDiv(Runnable func) throws JspException {
        if ("search".equals(divClass)) {
            extra = StringUtils.isBlank(extra) ? "请选择{display}" : extra;
            divClass = "layui-inline jhx-select-search";
        }
        super.writeOutDiv(func);
    }

    private void writeSelect() {
        try {
            tagWriter.startTag(tagType);

            writeValueNameAndAnnoAndDynamic(tagWriter, false, true);
            if (StringUtils.isNotBlank(extra)) {
                tagWriter.startTag("option");
                tagWriter.writeAttribute("value", "");
                String display = Util.display(model.getClass(), propertyName);
                tagWriter.appendValue(extra.replace("{display}", display));
                tagWriter.endTag(true);
            }

            LayUtil.getDataMapForBoolean(this, model, propertyName);

            writeMapOrEnum(this::writeOption);


            tagWriter.endTag(true);
        } catch (Exception e) {
            LogUtil.err(e);
        }

    }

    public void writeOption(String text, Object value, boolean isEnum) throws JspException {
        tagWriter.startTag("option");
        tagWriter.writeAttribute("value", value.toString());
        if (value.equals(forToStr()) || value.toString().equals(forToStr())) {
            tagWriter.writeAttribute("selected", "");
        }
        tagWriter.appendValue(text);

        tagWriter.endTag(true);
    }

    public Select() {
        tagType = "select";
    }
}
