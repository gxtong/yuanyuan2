package com.jhx.common.layui;

import lombok.experimental.Accessors;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 2:48 PM
 */
@Accessors(chain = true)
public class InputHidden extends InputBase {
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        tagWriter.startTag(tagType);
        writeInput(tagWriter);
        tagWriter.endTag(true);
        return EVAL_BODY_INCLUDE;
    }

    public InputHidden() {
        type = "hidden";
        tagType = "input";
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }
}
