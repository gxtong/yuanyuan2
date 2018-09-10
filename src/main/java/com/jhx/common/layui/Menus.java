package com.jhx.common.layui;

import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 11:10 AM
 */
public class Menus extends Base {
    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        tagWriter.startTag("a");
        tagWriter.writeAttribute("class", "layui-btn layui-btn-primary layui-btn-sm");
        tagWriter.writeAttribute("jhx-menu", "");
        tagWriter.appendValue("操作 &nbsp;<i class=\"layui-icon\">&#xe61a;</i>");
        tagWriter.endTag();

        tagWriter.startTag("ul");
        tagWriter.writeAttribute("style", "display:none");
        tagWriter.forceBlock();


        return EVAL_BODY_INCLUDE;
    }


    @Override
    public int doAfterBody() throws JspException {

        tagWriter.endTag();
        return super.doAfterBody();
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }
}

