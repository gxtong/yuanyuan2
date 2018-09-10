
package com.jhx.common.util.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jhx.common.util.AppPropUtil;
import com.jhx.common.util.NumberUtils;
import com.jhx.common.util.upload.UploadUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.JspServlet;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class WebAppConfig extends WebMvcConfigurerAdapter {

    //没有这个Bean，则controller中无法注入request session
    @Bean
    public RequestContextListener requestContextListener() {
        // service 层取 session
        return new RequestContextListener();
    }

    //全局异常处理（比如模型绑定异常）
    @Bean
    public WebExceptionHandler webExceptionHandler() {
        return new WebExceptionHandler();
    }

    //LocalTime LocalDate LocalDateTime jackson json输出格式配置
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = converter.getObjectMapper();
        SimpleModule module = new SimpleModule();

        JsonSerializer<LocalDateTime> longSerializer = new JsonSerializer<LocalDateTime>() {
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeNumber(value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }
        };
        JsonSerializer<LocalDate> longSerializer2 = new JsonSerializer<LocalDate>() {
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeString(value.toString());
            }
        };
        module.addSerializer(LocalDate.class, longSerializer2);
        module.addSerializer(LocalDateTime.class, longSerializer);
        mapper.registerModule(module);
        converters.add(converter);
        converters.add(new StringHttpMessageConverter());
        super.configureMessageConverters(converters);
    }

    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer() {
        return (ConfigurableEmbeddedServletContainer container) -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers(
                        (connector) -> {
                            connector.setMaxPostSize(20 * 1024 * 1024);
                        }
                );

                //jsp对1.8 lambda支持
                JspServlet servlet = tomcat.getJspServlet();
                Map<String, String> jspServletInitParams = servlet.getInitParameters();
                jspServletInitParams.put("compilerSourceVM", "1.8");
                jspServletInitParams.put("compilerTargetVM", "1.8");
                servlet.setInitParameters(jspServletInitParams);
            }

            // SSL
            boolean useSsl = BooleanUtils.toBoolean(AppPropUtil.get("useSsl"));
            if(useSsl){
                Ssl ssl = new Ssl();
                ssl.setKeyStore(AppPropUtil.get("keyStore"));
                ssl.setKeyStorePassword(AppPropUtil.get("keyStorePassword"));
                ssl.setKeyPassword(AppPropUtil.get("keyPassword"));
                ssl.setKeyStoreType("JKS");
                container.setSsl(ssl);
            }

            ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/WEB-INF/jsp/error/404.jsp");
            ErrorPage errorPage503 = new ErrorPage(HttpStatus.SERVICE_UNAVAILABLE, "/WEB-INF/jsp/error/503.jsp");
            container.addErrorPages(errorPage404,errorPage503);
        };
    }

    @Bean
    public UploadUtil uploadUtil() {
        UploadUtil uploadUtil = new UploadUtil();
        uploadUtil.setAccessKeyId(AppPropUtil.get("aliyun.oss.accessKeyId"));
        uploadUtil.setSecretAccessKey(AppPropUtil.get("aliyun.oss.accessKeySecret"));
        uploadUtil.setEndpoint(AppPropUtil.get("aliyun.oss.endpoint"));
        uploadUtil.setBucketName(AppPropUtil.get("aliyun.oss.bucketname"));
        return uploadUtil;
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {

        //String类型 trim
        registry.addConverter(new Converter<String, String>() {
            @Override
            public String convert(String source) {
                return StringUtils.trim(source);
            }
        });

        // LocalDateTime
        registry.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                if (Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", source)) {
                    source += " 00:00:00";
                }
                return toLocalDateTime(source);
            }
        });

        // LocalDate
        registry.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return toLocalDate(source);
            }
        });

        // LocalTime
        registry.addConverter(new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                try {
                    return LocalTime.parse(source);
                } catch (Exception e) {
                    return null;
                }
            }
        });

        //BigDecimal
        registry.addConverter(new Converter<String, BigDecimal>() {
            @Override
            public BigDecimal convert(String source) {
                if (NumberUtils.isNumber(source)) {
                    return NumberUtils.parseBigDecimal(source);
                }
                return null;
            }
        });

        //Integer
        registry.addConverter(new Converter<String, Integer>() {
            @Override
            public Integer convert(String source) {
                if (NumberUtils.isNumber(source)) {
                    return NumberUtils.parseBigDecimal(source).intValue();
                }
                return null;
            }
        });

        super.addFormatters(registry);
    }

    /**
     * 允许跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST")
                .maxAge(3600);
    }

    private String getDateFormatPattern(String source) {
        if (Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", source)) {
            return "yyyy-MM-dd";
        } else if (Pattern.matches("^\\d{4}年\\d{2}月\\d{2}日$", source)) {
            return "yyyy年MM月dd日";
        }
        return null;
    }

    private String getDateTimeFormatPattern(String source) {
        if (Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", source) ||
                Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", source)) {
            return "yyyy-MM-dd HH:mm:ss";
        }
        return null;
    }

    protected LocalDateTime toLocalDateTime(String source) {
        if (StringUtils.isNotBlank(source)) {
            source = StringUtils.trim(source);
            String pattern = getDateTimeFormatPattern(source);
            return pattern == null ? null : LocalDateTime.parse(source, DateTimeFormatter.ofPattern(pattern));
        }
        return null;
    }

    protected LocalDate toLocalDate(String source) {
        if (StringUtils.isNotBlank(source)) {
            source = StringUtils.trim(source);
            String pattern = getDateFormatPattern(source);
            return pattern == null ? null : LocalDate.parse(source, DateTimeFormatter.ofPattern(pattern));
        }
        return null;
    }
}
