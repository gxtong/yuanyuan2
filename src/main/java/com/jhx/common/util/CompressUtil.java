package com.jhx.common.util;

import org.springframework.util.Base64Utils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩相关工具类
 * author 钱智慧
 * date 2017/12/10 下午3:43
 */

public class CompressUtil {
    public static byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();

            return baos.toByteArray();
        } catch (Exception e) {
            LogUtil.err(e);
        }
        return null;
    }

    public static Object deserialize(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object object=ois.readObject();
            ois.close();
            bais.close();
            return object;
        } catch (Exception e) {
            LogUtil.err(CompressUtil.class,e);
        }
        return null;
    }

    /**
     * desc 将对象压缩成base64字符串，{@link #unGzip(String)}
     * author 钱智慧
     * date 2018/1/3 20:48
     **/
    public static String gzipObj(Object obj){
        return gzip(serialize(obj));
    }

    public static String gzip(byte[] bytes){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip=null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
        } catch (IOException e) {
            LogUtil.err(CompressUtil.class,e);
        }finally{
            if(gzip!=null){
                try {
                    gzip.close();
                } catch (IOException e) {
                    LogUtil.err(CompressUtil.class,e);
                }
            }
        }
        return Base64Utils.encodeToString(out.toByteArray());
    }

    /**
     * desc 将{@link #gzipObj(Object)}方法压缩的结果解压缩为原始对象
     * author 钱智慧
     * date 2018/1/3 20:53
     **/
    public static Object unGzipObj(String compressedBase64Str){
        if(compressedBase64Str==null){
            return null;
        }

        ByteArrayOutputStream out= new ByteArrayOutputStream();
        ByteArrayInputStream in=null;
        GZIPInputStream ginzip=null;
        byte[] compressed=null;
        byte[] decompressed = null;
        try {
            compressed = Base64Utils.decodeFromString(compressedBase64Str);
            in=new ByteArrayInputStream(compressed);
            ginzip=new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed=out.toByteArray();
            Object obj = deserialize(decompressed);
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    /**
     * desc 将字符串采用gzip压缩然后转换成base64字符串，采用UTF-8编码
     * author 钱智慧
     * date 2017/12/10 下午3:45
     **/
    public static String gzip(String srcStr) {
        if (srcStr == null || srcStr.length() == 0) {
            return srcStr;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPOutputStream gzip=null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(srcStr.getBytes("UTF-8"));
        } catch (IOException e) {
            LogUtil.err(CompressUtil.class,e);
        }finally{
            if(gzip!=null){
                try {
                    gzip.close();
                } catch (IOException e) {
                    LogUtil.err(CompressUtil.class,e);
                }
            }
        }


        return Base64Utils.encodeToString(out.toByteArray());
    }

    /**
     * desc 将通过本类gzip方法压缩后的结果转成原始字符串，使用的编码是UTF-8
     * author 钱智慧
     * date 2017/12/10 下午3:47
     **/
    public static String unGzip(String compressedBase64Str){
        if(compressedBase64Str==null){
            return null;
        }

        ByteArrayOutputStream out= new ByteArrayOutputStream();
        ByteArrayInputStream in=null;
        GZIPInputStream ginzip=null;
        byte[] compressed=null;
        String decompressed = null;
        try {
            compressed = Base64Utils.decodeFromString(compressedBase64Str);
            in=new ByteArrayInputStream(compressed);
            ginzip=new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed=out.toString("UTF-8");
        } catch (IOException e) {
            LogUtil.err(CompressUtil.class,e);
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                    LogUtil.err(CompressUtil.class,e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.err(CompressUtil.class,e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LogUtil.err(CompressUtil.class,e);
                }
            }
        }

        return decompressed;
    }
}
