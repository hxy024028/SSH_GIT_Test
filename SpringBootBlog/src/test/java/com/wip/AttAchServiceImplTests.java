package com.wip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.booleanThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.PageInfo;
import com.wip.constant.ErrorConstant;
import com.wip.dto.AttAchDto;
import com.wip.exception.BusinessException;
import com.wip.model.AttAchDomain;
import com.wip.service.attach.AttAchService;
import com.wip.service.attach.impl.AttAchServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AttAchServiceImplTests {
	@Autowired
	private AttAchServiceImpl attAchService;
	
	/**
	 * 增加网站图片信息为空
	 */
	@Test
	public void tsetAddAttAchNull() {
		try {
			attAchService.addAttAch(null);;
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 增加网站图片信息
	 */
	@Test
	public void tsetAddAttAch() {
		AttAchDomain attAchDomain = new AttAchDomain();
		attAchDomain.setAuthorId(1);
		attAchDomain.setCreated(1);
		attAchDomain.setFkey("key");
		attAchDomain.setFname("name");
		attAchDomain.setFtype("techo");
		attAchDomain.setId(1);
		attAchService.addAttAch(attAchDomain);
	}
	
	/**
	 * 获取图片文件信息列表
	 */
	@Test
	public void testGetAtts() {
		int pageNum = 1;
        int pageSize = 2;
        PageInfo<AttAchDto> pageInfo = attAchService.getAtts(pageNum, pageSize);
        if (pageInfo != null) {
            System.out.println("testGetAtts passed: PageInfo is not null");
            System.out.println("Total: " + pageInfo.getTotal());
            System.out.println("List size: " + pageInfo.getList().size());
        } else {
            System.out.println("testGetAtts failed: PageInfo is null");
        }
        //测试分页查询数据是否有数据
        List<AttAchDto> atts = pageInfo.getList();
        boolean hasData = atts != null && !atts.isEmpty();
        if (hasData) {
            System.out.println("testGetAtts passed: PageInfo contains data");
        } else {
            System.out.println("testGetAtts failed: PageInfo does not contain data");
        }
	}
	
	/**
	 * 根据编号查找图片信息，编号为空
	 */
	@Test
	public void TestGetAttAchByIdNull() {
		try {
			attAchService.getAttAchById(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 根据编号查找图片信息
	 */
	@Test
	public void TestGetAttAchById() {
		//11是数据库存在的id值，如果输入不存在的测试会报错
		AttAchDto achDto = attAchService.getAttAchById(11);
		if(null != achDto) {
			System.out.println(achDto);
		}else {
			fail("Expected BusinessException to be thrown");
		}
	}
	
	/**
	 * 根据编号删除信息，编号为空
	 */
	@Test
	public void testDeleteAttAchNull() {
		try {
			attAchService.deleteAttAch(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 根据编号删除信息
	 */
	@Test
	public void testDeleteAttAch() {
		//8已经删除，需要手动输入数据库存在的另外id值测试，否则会报错
		attAchService.deleteAttAch(8);
		AttAchDto achDomain = attAchService.getAttAchById(8);
		if(null != achDomain) {
			fail("Expected BusinessException to be thrown");
		}else {
			System.out.println("expected data delete successfully");
		}
	}
}
