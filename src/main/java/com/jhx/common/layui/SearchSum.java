package com.jhx.common.layui;

import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 2:48 PM
 */
public class SearchSum extends Base {
    private String display;

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public int doEndTag() throws JspException {
        display=null;
        return super.doEndTag();
    }

    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        String[] displayArr = display.split(",");
        String[] forArr = forToStr().split(",");
        if (displayArr.length != forArr.length) {
            throw new RuntimeException(new StringBuilder("display和for数量不一致").toString());
        }

        tagWriter.startTag("tr");
        writeAttr("style", "font-size: 13px;");

        tagWriter.startTag("td");

        for (int i = 0; i < displayArr.length; i++) {
            tagWriter.startTag("span");
            if(i>=1){
                tagWriter.writeAttribute("style","padding-left:15px;");
            }
            tagWriter.appendValue(displayArr[i].endsWith("：") ? displayArr[i] : new StringBuilder(displayArr[i]).append("：").toString());
            tagWriter.startTag("span");
            tagWriter.writeAttribute("class", "text-red");
            tagWriter.appendValue(forArr[i]);
            tagWriter.endTag();
            tagWriter.endTag();
        }


        tagWriter.endTag();
        tagWriter.endTag();
        return EVAL_BODY_INCLUDE;

    }
}
