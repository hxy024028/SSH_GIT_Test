package com.wip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wip.constant.ErrorConstant;
import com.wip.exception.BusinessException;
import com.wip.model.OptionsDomain;
import com.wip.service.option.impl.OptionServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OptionServiceImplTests {
	@Autowired
	private OptionServiceImpl optionService;
	
	@Test
	public void testGetOptions() {
		List<OptionsDomain> optionsDomainList = optionService.getOptions();
		if(null != optionsDomainList) {
			System.out.println(optionsDomainList);
		}else {
			fail("Expected list is null");
		}
	}
	
	/**
	 * 批量处理，根据网站名字更新网站配置
	 */
	@Test
	public void testSaveOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("site_description", "value1");
        options.put("site_record", "value2");
        optionService.saveOptions(options);
        testGetOptions();
	}
	
	/**
	 * 单一处理，根据网站名字更新网站配置，名字为空
	 */
	@Test
	public void testUpdateOptionByNameNull() {
		try {
			optionService.updateOptionByName(null, "hhh");
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
        	assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 单一处理，根据网站名字更新网站配置
	 */
	@Test
	public void testUpdateOptionByName() {
		String name = "social_csdn";
		String value = "value3";
		optionService.updateOptionByName(name, value);
		testGetOptions();
	}
	
	/**
	 * 根据名字获取日志信息，名字为空
	 */
	@Test 
	public void testGetOptionByNameNull() {
		try {
			optionService.getOptionByName(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
        	assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 根据名字获取日志信息
	 */
	@Test 
	public void testGetOptionByName() {
		String name = "site_description";
		OptionsDomain optionsDomain = optionService.getOptionByName(name);
		if(null != optionsDomain) {
			System.out.println(optionsDomain );
		}else {
			fail("Expected option is null");
		}
	}
}
