package com.jhx.common.layui;

import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 2:48 PM
 */
public abstract class InputBase extends Base {
    //input 的 type
    protected String type;

    public InputBase() {
        tagType = "input";
    }


    protected void writeInput(TagWriter tagWriter) throws JspException {
        writeValueNameAndAnnoAndDynamic(tagWriter, true, true);

        writeAttr("class", "layui-input");
        writeAttr("type", type);
        writeAttr("id",getElementId(propertyName));
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }
}
