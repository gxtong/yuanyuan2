package com.jhx.common.layui;

import com.jhx.common.util.LogUtil;
import com.jhx.common.util.ReflectUtil;
import com.jhx.common.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 钱智慧
 * date 2018/7/1 下午8:20
 */
public class Th extends Base {
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    private String cfg;

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }

    private String width;

    public void setWidth(String width) {
        this.width = width;
    }

    private String fixed;

    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    private String type;

    public void setType(String type) {
        this.type = type;
    }


    //type的取值有以下三种
    private static final String TypeCheckBox = "checkbox";//复选框
    private static final String TypeTdDo = "tdDo";//td内的操作
    private static final String TypeMenuDo = "menuDo";//菜单操作


    public Th() {
        tagType = "th";
    }


    protected int writeTagContent(TagWriter tagWriter) throws JspException {

        tagWriter.startTag(tagType);

        if (TypeTdDo.equals(type) || TypeMenuDo.equals(type)) {
            writeForDo();
        }else if (StringUtils.isNotBlank(text)){
            writeLayData(text,false);
            tagWriter.appendValue(text);
        } else {
            boolean isCheckBox = TypeCheckBox.equals(type);
            //th标题
            String display = Util.display(model.getClass(), propertyName);
            //lay-data配置
            writeLayData(display, isCheckBox);
            if (!isCheckBox) {
                tagWriter.appendValue(display);
            }

        }

        tagWriter.endTag(true);
        return EVAL_BODY_INCLUDE;
    }

    private void writeForDo() {
        boolean menuDo = TypeMenuDo.endsWith(type);
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("field", "_");
            map.put("minWidth", menuDo ? 105 : StringUtils.isBlank(width)?150:Integer.parseInt(width));
            map.put("fixed", "right");

            Map<String, Object> cfgMap = cfg == null ? new HashMap<>() : LayUtil.mapper.readValue(cfg, Map.class);

            for (String s : cfgMap.keySet()) {
                map.put(s, cfgMap.get(s));
            }

            String layData = LayUtil.mapper.writeValueAsString(map).replace('\"', '\'');

            writeAttr("lay-data", layData);

            if (!menuDo) {
                tagWriter.appendValue("操作");
            }else if(menuDo){
                tagWriter.appendValue(" ");
            }
        } catch (Exception e) {
            LogUtil.err(e);
        }
    }

    private int getWidth(String display) {
        if (StringUtils.isBlank(display)) {
            return 0;
        }
        //最小宽度40（padding导致的），每个字符另加14（因为字体大小是14），操作列和LocalDateTime列、LocalDate列特别处理
        int width = 40 + display.length() * 14;
        if(StringUtils.isBlank(text)){
            Field field = ReflectUtil.getField(model.getClass(), propertyName);
            if (field.getType() == LocalDate.class) {
                width = Integer.max(120, width);
            } else if (field.getType() == LocalDateTime.class) {
                width = Integer.max(180, width);
            }
        }

        return width;
    }

    private void writeLayData(String display, boolean isCheckBox) {
        boolean isText = StringUtils.isBlank(text);
        try {
            Map<String, Object> cfgMap = cfg == null ? new HashMap<>() : LayUtil.mapper.readValue(cfg, Map.class);
            if (isText && !cfgMap.containsKey("field")) {
                cfgMap.put("field", propertyName);
            }

            if(!isText){
                cfgMap.put("field", text);
            }

            if (!isCheckBox && !cfgMap.containsKey("width") && !cfgMap.containsKey("minWidth")) {
                int widthValue = getWidth(display);
                if (this.width == null) {
                    if (widthValue > 0) {
                        cfgMap.put("minWidth", widthValue);
                    }
                } else {
                    cfgMap.put("minWidth", this.width);
                }
            }

            if (!cfgMap.containsKey("fixed")) {
                if (fixed != null) {
                    cfgMap.put("fixed", fixed);
                } else if (isCheckBox) {
                    cfgMap.put("fixed", "left");
                }
            }

            if (!cfgMap.containsKey("type")) {
                if (type != null) {
                    cfgMap.put("type", type);
                }
            }

            String layData = LayUtil.mapper.writeValueAsString(cfgMap).replace('\"', '\'');
            writeAttr("lay-data", layData);

        } catch (Exception e) {
            LogUtil.err(e);
        }
    }


    @Override
    public int doEndTag() throws JspException {
        text=null;
        type=null;
        this.cfg=null;
        this.width=null;
        this.fixed=null;

        return super.doEndTag();
    }
}
