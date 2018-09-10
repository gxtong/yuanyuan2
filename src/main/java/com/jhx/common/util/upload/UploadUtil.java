package com.jhx.common.util.upload;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.jhx.common.util.AppPropUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 上传工具类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadUtil {

	Logger log = LoggerFactory.getLogger(UploadUtil.class);
	
	private String endpoint;
	private String accessKeyId;
	private String secretAccessKey;
	private String bucketName;
	private static String endpointInternal = AppPropUtil.get("aliyun.oss.endpoint.internal");

	private static String joinStr = "-java-";

	public String getJoinStr(){
		return joinStr;
	}

	/**  */
	private String connectKey(String prefix, String fileName) {
		return String.format("%s%s%s", prefix,joinStr,fileName);
	}
	
	/**
	 * 拼接完整的url
	 * @return
	 */
	private String getFullUrl(String key){
		return String.format("//%s.%s/%s", this.bucketName,this.endpoint,key);
	}
	
	/**
	 * 上传文件,返回完整的url 
	 * 
	 * @param prefix
	 *            文件名前缀
	 * @param file
	 *            文件
	 * @return url 上传失败返回null;
	 */
	public String uploadReturnFullUrl(String prefix, MultipartFile file) {
		String key = upload(prefix, file);
		if (StringUtils.isNotEmpty(key)) {
			return getFullUrl(key);
		}
		return null;
	}
	
	
	/**
	 * 上传文件
	 * 
	 * @param prefix
	 *            文件名前缀
	 * @param file
	 *            文件
	 * @return 文件名(不带前缀) 上传失败返回null;
	 */
	private String upload(String prefix, MultipartFile file) {
		try{
			if(file.getSize() == 0){
				// 空文件不上传;
				log.error(prefix+" 上传图片为空");
				return null;
			}
			OSSClient client = new OSSClient(StringUtils.isBlank(endpointInternal) ? endpoint : endpointInternal, accessKeyId, secretAccessKey);
			String etc = null;
			String fileName = file.getOriginalFilename();
			if(StringUtils.isNotBlank(fileName)){
				etc = StringUtils.substringAfterLast(fileName, ".").toLowerCase();
			}
			String randName = UUID.randomUUID().toString() + "." + etc;
			String key = connectKey(prefix,randName).toLowerCase();
			
			InputStream input = file.getInputStream();
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());
			PutObjectRequest request = new PutObjectRequest(bucketName, key, input, metadata);
			PutObjectResult result = client.putObject(request);
			
			if (StringUtils.isNotEmpty(result.getETag())) {
				return key;
			}
		}catch (Exception e) {
			log.error("上传图片失败",e);
		}
		
		return null;
	}
	
	
	/**
	 * 删除图片 ，在用户执行删除操作时 ，如果涉及到图片，那么在删除时也要删除阿里云中对应的图片
	 * @author whc
	 * @param key(要删除的图片路径)
	 */
	public void deleteOSSPic(String key) {
	    try {
            key = key.replaceFirst(String.format("(?:http:)?//%s\\.%s/", this.bucketName, this.endpoint), "");
            OSSClient client = new OSSClient(endpoint, accessKeyId, secretAccessKey);
            client.deleteObject(bucketName, key);
        }catch (Exception e){
            log.error("删除图片失败",e);
        }
	}

    /**
     * 富文本内容删除OSS图片
     * @param content
     */
	public void deleteByContent(String content){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(content)){
            Pattern compile = Pattern.compile("<img src=\"([^\"]+)\"");
            Matcher matcher = compile.matcher(content);
            while (matcher.find()){
                String url = matcher.group(1);
                if(url.contains(String.format("//%s.%s/", this.bucketName,this.endpoint))){
                    deleteOSSPic(url);
                }
            }
        }
    }

    /**
     * 根据富文本内容上传OSS图片
     * @param content
     */
    public String uploadByContent(String content, String prefix){
        Pattern compile = Pattern.compile("src=\"(data:([^;]+);base64,([^\"]+))\"");
        Matcher matcher = compile.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()){
            String contentType = matcher.group(2);
            String base64Content = matcher.group(3);
            String fileName = contentType.replace("/",".");
            ByteMultipartFile file = new ByteMultipartFile(fileName,fileName ,contentType, Base64Utils.decodeFromString(base64Content));
            String url = uploadReturnFullUrl(prefix, file);
            if(org.apache.commons.lang3.StringUtils.isNotBlank(url)){
                matcher.appendReplacement(sb,"src=\""+url+"\"");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}

