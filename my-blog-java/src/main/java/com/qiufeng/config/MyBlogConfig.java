package com.qiufeng.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目配置对象
 */
@Component
@ConfigurationProperties(prefix = "my-blog-java")
@Data
public class MyBlogConfig {

    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 版本年份 */
    private String copyrightYear;

    /** 上传路劲 */
    private String profile;

}
