package com.wangguangwu.mybatisplusexample;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.wangguangwu.mybatisplusexample.properties.MysqlProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author wangguangwu
 */
@SpringBootTest
class ApiTest {

    @Resource
    private MysqlProperties properties;

    @Test
    void testCodeCodeGenerator() {
        FastAutoGenerator.create(properties.getUrl(), properties.getUsername(), properties.getPassword())
                .globalConfig(builder -> {
                    builder
                            // 设置作者
                            .author("wangguangwu")
                            // 覆盖已生成文件
                            .fileOverride()
                            // 指定输出目录
                            .outputDir("/Users/wangguangwu/workSpace/wangguangwu/example/mybatisplus-example/src/main/java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.wangguangwu") // 设置父包名
                            .moduleName("mybatisplusexample") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user") // 设置需要生成的表名
                            .addTablePrefix(""); // 设置过滤表前缀
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

}
