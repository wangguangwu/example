package com.wangguangwu.okhttpexample.controller;

import com.wangguangwu.okhttpexample.component.OkHttpCli;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangguangwu
 */
@RequestMapping("hello")
@RestController
public class AnswerController {

    @Resource
    private OkHttpCli okHttpCli;

    @GetMapping("/show")
    public String show() {
        String url = "https://www.baidu.com";
        return okHttpCli.doGet(url);
    }
}
