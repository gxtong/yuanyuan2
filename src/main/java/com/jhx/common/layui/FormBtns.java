package com.jhx.common.layui;

import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 11:10 AM
 */
public class FormBtns extends AbstractHtmlElementTag {
    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        tagWriter.startTag("div");
        tagWriter.writeAttribute("class", "jhx-dlg-btns");

        tagWriter.startTag("button");
        tagWriter.writeAttribute("class", "layui-btn layui-btn-sm");
        tagWriter.appendValue("取消");
        tagWriter.endTag();

        tagWriter.startTag("button");
        tagWriter.writeAttribute("lay-submit","");
        tagWriter.writeAttribute("class", "layui-btn layui-btn-sm");
        tagWriter.appendValue("提交");
        tagWriter.endTag();

        tagWriter.endTag();
        return EVAL_BODY_INCLUDE;
    }

}

