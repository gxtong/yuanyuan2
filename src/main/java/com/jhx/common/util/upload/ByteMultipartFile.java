package com.jhx.common.util.upload;

import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * byte数组转MultipartFile
 * 复制的spring-test里的MockMultipartFile
 * @author t.ch
 * @date 2018-03-16 14:10
 */
public class ByteMultipartFile implements MultipartFile {

    private final String name;

    private String originalFilename;

    private String contentType;

    private final byte[] content;

    /**
     * 从格式为"data:image/png;base64,iVBORw0KG...UVORK5CYII="的字符串获取对象
     */
    public static ByteMultipartFile valueOf(String content){
        String[] split = content.split(";");
        String contentType = split[0].split(":")[1];
        String image = split[1].split(",")[1];
        String etc = contentType.split("/")[1];
        String fileName = "file."+etc;
        return new ByteMultipartFile("file", fileName, contentType, Base64Utils.decodeFromString(image));
    }

    /**
     * @param name 表单中的name
     * @param content 内容
     */
    public ByteMultipartFile(String name, byte[] content) {
        this(name, "", null, content);
    }

    /**
     *
     * @param name 表单中的name
     * @param contentStream 文件内容
     * @throws IOException 如果contentStream报异常的话
     */
    public ByteMultipartFile(String name, InputStream contentStream) throws IOException {
        this(name, "", null, FileCopyUtils.copyToByteArray(contentStream));
    }

    /**
     *
     * @param name 表单中的name
     * @param originalFilename 文件名
     * @param contentType 类型
     * @param content 内容
     */
    public ByteMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
        Assert.hasLength(name, "Name must not be null");
        this.name = name;
        this.originalFilename = (originalFilename != null ? originalFilename : "");
        this.contentType = contentType;
        this.content = (content != null ? content : new byte[0]);
    }

    /**
     *
     * @param name 表单中的name
     * @param originalFilename 文件名
     * @param contentType 类型
     * @param contentStream 内容
     * @throws IOException 如果contentStream报异常的话
     */
    public ByteMultipartFile(String name, String originalFilename, String contentType, InputStream contentStream)
            throws IOException {
        this(name, originalFilename, contentType, FileCopyUtils.copyToByteArray(contentStream));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return (this.content.length == 0);
    }

    @Override
    public long getSize() {
        return this.content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(this.content, dest);
    }

}
