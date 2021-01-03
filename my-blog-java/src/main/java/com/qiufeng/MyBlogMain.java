package com.qiufeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 主启动
 * 禁用自带数据源
 */
@SpringBootApplication( exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.qiufeng.mapper")
public class MyBlogMain {

    public static void main(String[] args) {
        SpringApplication.run(MyBlogMain.class,args);
        System.out.println(".————————————————————————————————.	\n" +
                "(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ	\n" +
                ".————————————————————————————————.	\n");
    }

}
