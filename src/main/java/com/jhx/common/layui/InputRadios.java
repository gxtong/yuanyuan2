package com.jhx.common.layui;

import com.jhx.common.layui.dto.GetDataMapModel;
import com.jhx.common.util.ReflectUtil;
import com.jhx.common.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 2018/7/1 下午9:03
 */
public class InputRadios extends Base {
    private boolean isRate = false;

    public InputRadios() {
        tagType = "input";
    }

    @Override
    public int doEndTag() throws JspException {
        isRate = false;
        return super.doEndTag();
    }

    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        boolean hasExtra = StringUtils.isNotBlank(extra) && ReflectUtil.getField(model.getClass(), propertyName).getType() == Boolean.class;
        if (hasExtra) {
            data = Util.displayBoolMap(model.getClass(), propertyName);
            writeExtraRadio();
        }

        GetDataMapModel ret = LayUtil.getDataMapForBoolean(this, model, propertyName);
        isRate = ret.isRate();

        writeOutDiv(() -> writeMapOrEnum(this::writeRadio));

        writeScript(ret);

        return EVAL_BODY_INCLUDE;
    }

    private void writeExtraRadio() throws JspException {
        tagWriter.startTag("input");
        tagWriter.writeAttribute("type", "radio");
        tagWriter.writeAttribute("title", extra);
        tagWriter.writeAttribute("value", "");
        tagWriter.writeAttribute("name", propertyName);

        if (_for == null) {
            tagWriter.writeAttribute("checked", "");
        }

        tagWriter.endTag(true);
    }

    private void writeScript(GetDataMapModel model) throws JspException {
        if (model.isRate()) {
            tagWriter.startTag("script");

            String layFilter = getElementId(propertyName);

            StringBuilder script = new StringBuilder("layui.form.on('radio(" + layFilter + ")', function (data) {\n")
                    .append("var isRate = data.value == 'true';\n");
            for (String target : model.getTargetNames()) {
                String targetId = getElementId(target);


                script.append("$('#" + targetId + "').attr('isRate', isRate);\n")
                        .append("var prev" + targetId + "=$('#" + targetId + "').prev('.append-icon');\n")
                        .append("if(prev" + targetId + ".length==0){prev" + targetId + "=$(\"<i class='append-icon'/>\");$('#" + targetId + "').before(prev" + targetId + ");}\n")
                        .append("prev" + targetId + ".text(isRate?'%':'');\n");
            }
            script.append("})");


            tagWriter.appendValue(script.toString());

            tagWriter.endTag(true);
        }

        if (auto) {
            tagWriter.startTag("script");
            String layFilter = getElementId(propertyName);
            String script = new StringBuilder("layui.form.on('radio(layFilter)', function (data) {\n")
                    .append("$(data.elem).closest(\"form\").submit();\n")
                    .append("})").toString();

            script = script.replace("layFilter", layFilter);
            tagWriter.appendValue(script);
            tagWriter.endTag(true);
        }
    }

    private void writeRadio(String text, Object value, boolean isEnum) throws JspException {
        tagWriter.startTag("input");
        tagWriter.writeAttribute("type", "radio");
        tagWriter.writeAttribute("value", value.toString());
        tagWriter.writeAttribute("title", text);
        tagWriter.writeAttribute("name", propertyName);


        if (isRate || auto) {
            tagWriter.writeAttribute("lay-filter", getElementId(propertyName));
        }

        writeDynamicAttrs();

        if (value.equals(_for) || value.toString().equals(forToStr())) {
            tagWriter.writeAttribute("checked", "");
        }

        tagWriter.endTag(true);
    }
}
