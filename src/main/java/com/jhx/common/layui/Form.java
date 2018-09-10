package com.jhx.common.layui;

import com.jhx.common.util.LogUtil;
import lombok.Getter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * @author 钱智慧
 * date 6/26/18 11:10 AM
 */
@Getter
public class Form extends Base {
    protected static final String Post = "post";
    protected static final String Get = "get";
    protected static final String Cancel = "cancel";
    protected static final String Submit = "submit";
    protected static final String None = "none";

    protected String action;
    protected String method;
    private String btns;

    public void setBtns(String btns) {
        this.btns = btns;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        tagWriter.startTag("form");
        writeAttr("class", "layui-form");
        writeAttr("action", resolveAction());
        writeAttr("method", getHttpMethod(true));
        writeDynamicAttrs();
        tagWriter.forceBlock();

        tagWriter.startTag("div");
        tagWriter.writeAttribute("class", "jhx-dlg-content");
        tagWriter.forceBlock();


        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        action = null;
        method = null;
        return super.doEndTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        tagWriter.endTag();//结束jhx-dlg-conten div


        writeBtns();

        tagWriter.endTag();//结束form
        return SKIP_BODY;
    }

    private void writeBtns() throws JspException {
        if ("".equals(btns) || None.equals(btns)) {
            return;
        }


        tagWriter.startTag("div");
        tagWriter.writeAttribute("class", "jhx-dlg-btns");

        if (btns == null) {
            writeCancelBtn(false);
            writeSubmitBtn();
        } else if (btns.contains(Cancel)) {
            writeCancelBtn(btns.contains(Submit));
        } else if (btns.equals(Submit)) {
            writeSubmitBtn();
        }

        tagWriter.endTag();
    }

    private void writeSubmitBtn() throws JspException {
        tagWriter.startTag("button");
        tagWriter.writeAttribute("lay-submit", "");
        tagWriter.writeAttribute("class", "layui-btn layui-btn-sm");
        tagWriter.appendValue("提交");
        tagWriter.endTag();
    }

    private void writeCancelBtn(boolean onlyCancel) throws JspException {
        tagWriter.startTag("button");
        tagWriter.writeAttribute("class", "layui-btn layui-btn-sm");
        tagWriter.appendValue(onlyCancel ? "关闭" : "取消");
        tagWriter.endTag();
    }


    protected String resolveAction() throws JspException {
        String action = getAction();

        if (StringUtils.hasText(action)) {
            action = getDisplayString(evaluate("action", action));
            return processAction(action);
        }

        String requestUri = getRequestContext().getRequestUri();
        String encoding = this.pageContext.getResponse().getCharacterEncoding();
        try {
            requestUri = UriUtils.encodePath(requestUri, encoding);
        } catch (Exception e) {
            LogUtil.err(e);
        }

        if (StringUtils.hasText(requestUri)) {
            return processAction(requestUri);
        } else {
            throw new IllegalArgumentException("Attribute 'action' is required. " +
                    "Attempted to resolve against current request URI but request URI was null.");
        }
    }

    private String processAction(String action) {
        RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
        ServletRequest request = this.pageContext.getRequest();
        if (processor != null && request instanceof HttpServletRequest) {
            action = processor.processAction((HttpServletRequest) request, action, getHttpMethod(true));
        }
        return action;
    }

    protected boolean isMethodBrowserSupported(String method) {
        return (Get.equalsIgnoreCase(method) || Post.equalsIgnoreCase(method));
    }

    protected String getHttpMethod(boolean defaultPost) {
        return (isMethodBrowserSupported(getMethod()) ? getMethod() : (defaultPost ? Post : Get));
    }
}

