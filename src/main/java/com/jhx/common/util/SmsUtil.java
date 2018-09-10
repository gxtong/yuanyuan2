package com.jhx.common.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.jhx.common.util.dto.Result;
import com.jhx.common.util.dto.SmsSendRequest;
import com.jhx.common.util.dto.SmsSendResponse;
import com.jhx.common.util.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信工具类
* @author 钱智慧
* @date 2017年9月8日 上午10:26:00
*/
public class SmsUtil {
	private static String smsType = AppPropUtil.get("smsType");

	public  static Result send(String content,String ... phoneNumbers){
	    //使用什么短信 发送
        // ali 阿里  ytx 253云通信 。。。
		Pattern phonePtn = Pattern.compile(Constant.PatternPhone);
		for (String phoneNumber : phoneNumbers) {
			if(!Pattern.matches(Constant.PatternPhone,phoneNumber)){
				return new Result(false,new StringBuilder("手机号").append(phoneNumber).append("非法").toString());
			}
		}



        if(StringUtils.isBlank(smsType)){
            return  aliSend(content,phoneNumbers);
        }
        switch (smsType){
            case "ytx":
                return ytxSend(content,phoneNumbers);
            default:
                return  aliSend(content,phoneNumbers);
        }
	}

	/**
	 * 阿里云短信发送
	 * @param content 要发送的内容，如验证码
	 * @param phoneNumbers 接收短信的手机号码，可以是多个
	 * @return
	 * @author 钱智慧
	 * @date 2017年9月8日 上午10:27:04
	 */
	private static Result aliSend(String content,String ... phoneNumbers) {
		Result ret=new Result();
		
		final String accessKeyId = AppPropUtil.get("AccessKeyId");
		final String accessKeySecret = AppPropUtil.get("AccessKeySecret");
		final String signName = AppPropUtil.get("SignName");
		final String templateCode = AppPropUtil.get("TemplateCode");
		final String templateContent = AppPropUtil.get("TemplateContent");
		//设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化ascClient需要的几个参数
		final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
				
		/*
		 *开发时要注意，短信模板是变化的(客户自己申请填写)，未必就是这种：“验证码${code}，您正在进行身份验证，打死不要告诉别人哦！”
		 *占位符可能有多个，甚至未必叫code，但形式都是${xxx}， 我们的处理方式是：只用第一个，后面的都用空字符代替，比如，如果短信模板是
		 *“验证码${code}，您正在进行身份验证，打死不要告诉${name}别人哦！”，发送的给短信网关时，${code}换成发送内容content，
		 *${name}换成空字符---要想达到这个效果，用正则最方便
		 */
		
		// 获取参数
		Builder<Object,Object> mapBuilder = ImmutableMap.builder();
		Matcher matcher = Pattern.compile("\\$\\{(\\w+)\\}").matcher(templateContent);
		if(matcher.find()) {
			mapBuilder.put(matcher.group(1), content); // 首个参数
		}
		while(matcher.find()) {
			mapBuilder.put(matcher.group(1), ""); // 其他参数
		}
		String paramStr = new Gson().toJson(mapBuilder.build());
		//初始化ascClient,暂时不支持多region
		IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKeyId,accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", product, domain);
		} catch (ClientException e1) {
			LogUtil.err(e1);
			ret.setMsg(e1.getErrMsg());
			return ret;
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);
		 //组装请求对象
		 SendSmsRequest request = new SendSmsRequest();
		 //使用post提交
		 request.setMethod(MethodType.POST);
		 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		 request.setPhoneNumbers(StringUtils.join(phoneNumbers,","));
		 //必填:短信签名-可在短信控制台中找到
		 request.setSignName(signName);
		 //必填:短信模板-可在短信控制台中找到
		 request.setTemplateCode(templateCode);
		 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		 request.setTemplateParam(paramStr);
		 //可选-上行短信扩展码(无特殊需求用户请忽略此字段)
		 //request.setSmsUpExtendCode("90997");
		 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		 //request.setOutId("yourOutId");
		 //请求失败这里会抛ClientException异常
		
		try {
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			ret.setOk("OK".equals(sendSmsResponse.getCode()));
			ret.setMsg(sendSmsResponse.getMessage());
		} catch (ClientException e) {
			LogUtil.err(e);
			ret.setMsg(e.getErrMsg());
		}
		return ret;
	}


    /**
     * 253云通讯 普通短信发送 如果出现ip错误需要进入253短信控制台，ip绑定
     * @param content 发送的内容，将会替换进短信模板
     * @param phoneNumbers 接收短信的手机号
     * @return
     */
    private static Result ytxSend(String content,String ... phoneNumbers){
        String charset = "utf-8";
		// 用户平台API账号(非登录账号,示例:N1234567)
		String account = AppPropUtil.get("YtxAccount");
		// 用户平台API密码(非登录密码)
		String pswd = AppPropUtil.get("Ytxpassword");
		String smsVariableRequestUrl = AppPropUtil.get("YtxRequestUrl");
		//短信签名，必须有 但需要申请
		String signName = AppPropUtil.get("YtxSignName");
		//短信内容
		String msg = signName + AppPropUtil.get("YtxMsgTemplateContent");
		//接收短信的手机号
		String phone = StringUtils.join(phoneNumbers);
		//是否状态报告
		String report= "true";

        /**
         * 短信模板里可能有多个${code} 但只取第一个
         */
        Builder<Object,Object> mapBuilder = ImmutableMap.builder();
        Matcher matcher = Pattern.compile("\\$\\{(\\w+)\\}").matcher(msg);
        if(matcher.find()) {
            msg = msg.replace("${" + matcher.group(1) + "}", content);
        }
        while(matcher.find()) {
            msg = msg.replace("${" + matcher.group(1) + "}", "");
        }
        Gson gson = new Gson();
        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone,report);
        String requestJson = gson.toJson(smsSingleRequest);
        String response = null;
		Result result = new Result(false,"短信发送失败！");

		try {
            //发送短信，获取发送状态信息
            response = HttpUtil.post(smsVariableRequestUrl,requestJson,charset);
        } catch (IOException e) {
            LogUtil.err(e);
			result.setMsg(e.getMessage());
        }

        SmsSendResponse smsSingleResponse = gson.fromJson(response,SmsSendResponse.class);

        if(smsSingleResponse!=null && org.springframework.util.StringUtils.isEmpty(smsSingleResponse.getErrorMsg())){
            //响应成功
            result.setOk(true).setMsg("发送成功！");
        }else{
            result.setOk(false).setMsg(smsSingleResponse.getErrorMsg()+" msgId:"+smsSingleResponse.getMsgId()+"  错误状态码:"+smsSingleResponse.getCode());
        }
        return result;
	}

	public static void main(String[] args) {
		// 测试
		//System.out.println(new Gson().toJson(SmsUtil.aliSend("测试6666", "17630025236")));
        //System.out.println(SmsUtil.ytxSend("ax35","17630025236","15565776613"));
	}
}
