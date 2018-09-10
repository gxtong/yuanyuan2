package com.jhx.common.layui;

import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 11:10 AM
 */
public class SearchBtn extends AbstractHtmlElementTag {
    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        tagWriter.startTag("button");
        tagWriter.writeAttribute("class", "layui-btn layui-btn-sm");



        tagWriter.startTag("i");
        tagWriter.writeAttribute("class", "layui-icon");
        tagWriter.appendValue("&#xe615;");
        tagWriter.endTag();

        tagWriter.appendValue("检索");

        tagWriter.endTag();

        return EVAL_BODY_INCLUDE;
    }

}

