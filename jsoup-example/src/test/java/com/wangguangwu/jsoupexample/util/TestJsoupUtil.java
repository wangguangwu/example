package com.wangguangwu.jsoupexample.util;

import com.wangguangwu.jsoupexample.utils.JsoupUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangguangwu
 */
public class TestJsoupUtil {

    private Document document;

    @Before
    public void accessWangguangwu() {
        document = JsoupUtil.jsoupUrl("http://www.wangguangwu.com");
        Assert.assertNotNull(document);
    }

    @Test
    public void testHtml() {
        // 获取 html 文件
        String html = document.html();
        Assert.assertNotNull(html);
        System.out.println(html);
    }

    @Test
    public void testTitle() {
        // 获取 html 的标题
        String title = document.title();
        Assert.assertNotNull(title);
        System.out.println(title);
    }

    @Test
    public void testOuterHtml() {
        // 获取 html 文件
        String html = document.outerHtml();
        Assert.assertNotNull(html);
        System.out.println(html);
    }

    @Test
    public void testLink() {
        // 使用 selector 方法获取 link 开头的标签
        Elements elements = document.select("link");
        Assert.assertNotNull(elements);
        elements.forEach(System.out::println);
    }

    @Test
    public void testFirstLink() {
        // 使用 first 方法获取第一个 link 开头的标签
        Element element = document.select("link").first();
        Assert.assertNotNull(element);
        System.out.println(element);
        // 通过 attributes 拿到所有的标签
        System.out.println(element.attributes());
        // 通过 attr 方法拿到指定的标签
        System.out.println(element.attr("href"));
    }


    @Test
    public void testAllElements() {
        // 使用 getAllElements 方法获取所有元素
        Elements elements = document.getAllElements();
        Assert.assertNotNull(elements);
        elements.forEach(System.out::println);
    }

    @Test
    public void testBody() {
        // 使用 body 方法获取 body
        Element body = document.body();
        Assert.assertNotNull(body);
        System.out.println(body);
    }

    @Test
    public void testGetName() {
        // 获取我的博客中的文章名称
       Optional.of(document.select("div")).orElse(null)
                .stream()
                .filter(element -> "content".equals(element.attr("class")))
               .forEach(element -> System.out.println(element.childNode(0).toString().trim()));
    }

    @Test
    public void testGetWangguangwu() {
        Set<Element> elements = Optional.of(document.select("p")).orElse(null)
                .stream()
                .filter(element -> element.attr("class").startsWith("is"))
                .collect(Collectors.toSet());
        // 使用 text 方法拿到文本
        elements.forEach(element -> System.out.println(element.text()));
    }

}
