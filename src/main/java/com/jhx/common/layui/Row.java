package com.jhx.common.layui;

import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 11:10 AM
 */
public class Row extends Base {
    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        tagWriter.startTag("tr");
        tagWriter.forceBlock();
        tagWriter.startTag("td");
        tagWriter.forceBlock();
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        tagWriter.startTag("tr");
        tagWriter.writeAttribute("style","height:10px");
        tagWriter.endTag();
        tagWriter.endTag();
        tagWriter.endTag();
        return super.doAfterBody();
    }
}

