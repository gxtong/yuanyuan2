package com.jhx.common.layui;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 2018/7/1 下午8:20
 */
@Getter
@Setter
@Accessors(chain = true)

public class InputCheck extends Base {

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public InputCheck() {
        tagType = "input";
    }

    @Override
    public int doEndTag() throws JspException {
        value = null;
        return super.doEndTag();
    }

    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        tagWriter.startTag(tagType);
        writeAttr("type", "checkbox");

        String layFilter=null;
        if (auto) {
            layFilter = getElementId(propertyName);
            writeAttr("lay-filter", layFilter);
        }

        writeValueNameAndAnnoAndDynamic(tagWriter, false, true);

        writeAttr("value", value);

        if (StringUtils.isNotBlank(_for.toString()) && _for.toString().equals(value)) {
            writeAttr("checked", "");
        }


        tagWriter.endTag(true);

        if(auto){
            tagWriter.startTag("script");

            String script = new StringBuilder("layui.form.on('checkbox(layFilter)', function (data) {\n")
                    .append("$(data.elem).closest(\"form\").submit();\n")
                    .append("})").toString();

            script = script.replace("layFilter", layFilter);
            tagWriter.appendValue(script);
            tagWriter.endTag(true);
        }


        return EVAL_BODY_INCLUDE;
    }

}
