package com.wip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.intThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wip.constant.ErrorConstant;
import com.wip.exception.BusinessException;
import com.wip.model.UserDomain;
import com.wip.service.user.impl.UserServiceImpl;
import com.wip.utils.TaleUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTests {
	@Autowired
	private UserServiceImpl userService;
	
	/**
	 * 登录未输入用户名与密码
	 */
	@Test
	public void testLoginNull() {
		try {
			userService.login(null, null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Auth.USERNAME_PASSWORD_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 登录成功
	 */
	@Test
	public void testLoginSuccess() {
		UserDomain userDomain = userService.login("admin", "123456");
		if(null != userDomain) {
			System.out.println(userDomain);
		}else {
			fail("login fail");
		}
	}
	
	/**
	 * 测试md5加密
	 */
	@Test
	public void testLoginMd5() {
	    String username = "admin";
	    String password = "123456";
	    //计算用户名和密码的加密值
	    String expectedMd5 = TaleUtils.MD5encode(username + password);
	    System.out.println("Expected MD5: " + expectedMd5);
	    UserDomain user = userService.login(username, password);
	    //成功登录后获取登录的用户名与密码的加密值
	    System.out.println("Returned User: " + user.getUsername());
	    String actualMd5 = TaleUtils.MD5encode(username + password);
	    //将俩加密值对比是否相同，验证md5加密是否存在问题
	    System.out.println("Actual MD5: " + actualMd5);
	}
	
	/**
	 * 根据用户编号查找用户信息
	 */
	@Test 
	public void testGetUserInfoById() {
		int uid = 1;
		UserDomain userDomain = userService.getUserInfoById(uid);
		if(null != userDomain) {
			System.out.println(userDomain);
		}else {
			fail("Expected user was not found");
		}
	}
	
}
