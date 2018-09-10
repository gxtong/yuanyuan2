package com.jhx.common.layui;

import com.jhx.common.util.Util;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 2:48 PM
 */
public class TextArea extends Base {

    public TextArea() {
        tagType = "textarea";
    }

    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        tagWriter.startTag(tagType);

        writeValueNameAndAnnoAndDynamic(tagWriter, true,true);

        writeAttr("class", "layui-textarea");

        StringBuilder sb=new StringBuilder(Util.display(model.getClass(),forToStr()));
        tagWriter.appendValue(sb.append("：").toString());

        tagWriter.endTag(true);

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }
}
