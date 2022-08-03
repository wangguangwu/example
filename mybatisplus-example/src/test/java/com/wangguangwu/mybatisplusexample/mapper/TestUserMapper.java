package com.wangguangwu.mybatisplusexample.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangguangwu.mybatisplusexample.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangguangwu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserMapper {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        List<User> users = userMapper.selectList(null);
        Assert.assertNotNull(users);
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectByLike() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(new User());
        // 查询名字中包含 "O" 的用户
        queryWrapper.like("name", "o");

        List<User> users1 = userMapper.selectList(queryWrapper);
        Assert.assertNotNull(users1);
        users1.forEach(System.out::println);

        queryWrapper.clear();
        System.out.println("============================");
        queryWrapper.like("name", "%o%");
        List<User> users2 = userMapper.selectList(queryWrapper);
        users2.forEach(System.out::println);
    }

    @Test
    public void testSelectByLe() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(new User());
        // 查询年龄小于等于 20  的用户
        queryWrapper.le("age", 20);

        List<User> users = userMapper.selectList(queryWrapper);
        Assert.assertNotNull(users);
        users.forEach(System.out::println);
    }

    @Test
    public void testSave() {
        User user = new User();
        user.setAge(21);
        user.setEmail("w@wangguangwu.com");
        user.setName("wangguangwu");
        int count = userMapper.insert(user);
        Assert.assertTrue(count > 0);
        System.out.println("insert successful");
    }

    @Test
    public void testDelete() {
        int count = userMapper.deleteById(6);
        Assert.assertTrue(count > 0);
        System.out.println("delete successful");
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(5L);
        user.setEmail("w@wangguangwu.com");
        user.setName("wangguangwu");
        int count = userMapper.updateById(user);
        Assert.assertTrue(count > 0);
        System.out.println("insert successful");
    }

    @Test
    public void testSelectPage() {
        Page<User> page = new Page<>(1, 2);
        Page<User> userPage = userMapper.selectPage(page, null);
        System.out.println("总条数 ------> " + userPage.getTotal());
        System.out.println("当前页数 ------> " + userPage.getCurrent());
        System.out.println("当前每页显示数 ------> " + userPage.getSize());
        List<User> records = userPage.getRecords();
        Assert.assertNotNull(records);
        records.forEach(System.out::println);
    }

}
