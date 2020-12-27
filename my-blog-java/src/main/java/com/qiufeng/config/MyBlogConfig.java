package com.qiufeng.config;

import lombok.Data;
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
    private static String profile;

    /**
     * 获取博客图片路劲
     * @return 博客图片路劲
     */
    public static String getBlogImagePath() {
        return profile + "/blogImage";
    }

    /**
     * 获取头像上传路劲
     * @return 头像上传路劲
     */
    public static String getAvatarPath() {
        return profile + "/avatar";
    }

    /**
     * 获取下载路劲
     * @return 下载路劲
     */
    public static String getDownLoadPath() {
        return profile + "/download/";
    }

    /**
     * 获取文件上传路劲
     * @return 文件上传路劲
     */
    public static String getUploadPath() {
        return profile + "/upload";
    }

}
