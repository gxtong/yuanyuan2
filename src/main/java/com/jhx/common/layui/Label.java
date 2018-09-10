package com.jhx.common.layui;

import com.jhx.common.util.Util;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 2:48 PM
 */
public class Label extends Base {

    public Label() {
        tagType = "label";
    }

    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        checkModel();
        tagWriter.startTag(tagType);

        writeDynamicAttrs();

        writeAttr("class", "layui-form-label");

        StringBuilder sb=new StringBuilder(Util.display(model.getClass(),propertyName)).append("：");

        tagWriter.appendValue(Util.display(model.getClass(),sb.toString()));

        if(LayUtil.isRequired(model,propertyName)){
            tagWriter.startTag("span");
            writeAttr("class","text-red");
            tagWriter.appendValue("*");
            tagWriter.endTag(true);
        }

        tagWriter.endTag(true);

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }
}
