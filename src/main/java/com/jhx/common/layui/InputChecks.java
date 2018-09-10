package com.jhx.common.layui;

import com.jhx.common.util.LogUtil;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;
import java.util.Collection;
import java.util.List;

/**
 * @author 钱智慧
 * date 2018/7/1 下午9:03
 */
public class InputChecks extends Base {
    public InputChecks() {
        tagType = "input";
    }

    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        writeOutDiv(() -> writeMapOrEnum(this::writeCheckbox));
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    public void writeCheckbox(String text, Object value, boolean isEnum) throws JspException {
        tagWriter.startTag("input");
        tagWriter.writeAttribute("type", "checkbox");
        tagWriter.writeAttribute("value", value.toString());
        tagWriter.writeAttribute("title", text);
        tagWriter.writeAttribute("name", propertyName);

        if (_for != null) {
            if (_for instanceof List<?>) {
                for (Object item : (Collection<?>) _for) {
                    if (item.getClass().isEnum()) {
                        Enum anEnum = (Enum) item;
                        if (anEnum.name().equals(value)) {
                            tagWriter.writeAttribute("checked", "");
                            break;
                        }
                    } else if (item.equals(value)) {
                        tagWriter.writeAttribute("checked", "");
                        break;
                    }
                }
            } else {

                String errMsg = new StringBuilder("for类型只能是List，不支持：").append(_for.getClass().getName()).toString();
                LogUtil.err(errMsg);
                throw new RuntimeException(errMsg);
            }
        }

        tagWriter.endTag(true);
    }
}
