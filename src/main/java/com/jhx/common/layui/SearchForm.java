package com.jhx.common.layui;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 11:10 AM
 */
public class SearchForm extends Form {
    String width;

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        tagWriter.startTag("blockquote");
        writeAttr("class", "layui-elem-quote layui-quote-nm");
        writeAttr("style", "overflow: auto;padding-bottom:5px;");

        tagWriter.startTag("form");
        writeAttr("class", "layui-form");
        writeAttr("lay-filter", "search");
        writeAttr("action", resolveAction());
        writeAttr("method", getHttpMethod(false));

        writeDynamicAttrs();
        tagWriter.forceBlock();

        tagWriter.startTag("table");

        StringBuilder sb=new StringBuilder("width:");
        if(StringUtils.isNotBlank(width)){
            width=width.replace("px","");
            sb.append(width).append("px");
        }else{
            sb.append("1000px");
        }
        sb.append(";overflow:scroll;");
        writeAttr("style", sb.toString());

        tagWriter.forceBlock();


        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doAfterBody() throws JspException {
        tagWriter.endTag();
        tagWriter.endTag();
        tagWriter.endTag();
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }
}

