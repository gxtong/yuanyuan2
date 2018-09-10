package com.jhx.common.layui;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 2018/7/1 下午8:20
 */
@Getter
@Setter
@Accessors(chain = true)

public class InputRadio extends Base {

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public InputRadio() {
        tagType = "input";
    }

    @Override
    public int doEndTag() throws JspException {
        value = null;
        return super.doEndTag();
    }

    protected int writeTagContent(TagWriter tagWriter) throws JspException {

        tagWriter.startTag(tagType);
        writeAttr("type", "radio");

        writeValueNameAndAnnoAndDynamic(tagWriter, false, true);

        writeAttr("value", value);

        if (_for != null && StringUtils.isNotBlank(_for.toString()) && _for.equals(value)) {
            writeAttr("checked", "");
        }


        tagWriter.endTag(true);
        return EVAL_BODY_INCLUDE;
    }
}
