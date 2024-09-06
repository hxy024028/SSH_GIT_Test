package com.wip;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.intThat;
import static org.mockito.Matchers.isNull;

import java.util.List;

import javax.sound.midi.MidiChannel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.wip.constant.ErrorConstant;
import com.wip.dao.RelationShipDao;
import com.wip.dto.MetaDto;
import com.wip.dto.cond.MetaCond;
import com.wip.exception.BusinessException;
import com.wip.model.MetaDomain;
import com.wip.service.meta.MetaService;
import com.wip.service.meta.impl.MetaServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
/**
 * 测试时最好把不需要测试的方法注释掉，不然更新新增啥的容易报错
 * @author hql
 * @date 2024年8月30日
 * @project_name SpringBootBlog
 * @package_name com.wip	
 * @file_name MetaServiceImplTests.java
 * @classname MetaServiceImplTests
 * @version 2024年8月30日下午1:26:35
 */
public class MetaServiceImplTests {
	@Autowired
	private MetaServiceImpl metaService;
	
	@Autowired
    private RelationShipDao relationShipDao;
	
	/**
	 * 更新数据，不存在相同的项目名、类型，存在mid更新数据
	 */
	@Test
	public void tsetSaveMetaUpdate() {
	    String type = "111";
	    String name = "categoryUpdate111";
	    int mid = 77;
	    metaService.saveMeta(type, name, mid);
	}
	
	/**
	 * 新增数据，不传入mid新增数据
	 */
	@Test
	public void tsetSaveMetaInsert() {
	    String type = "insert";
	    String name = "categoryInsert";
	    metaService.saveMeta(type, name, null);
	}
	
	/**
	 * 新增数据，如果存在相同的则抛出异常
	 */
	@Test
	public void testSaveMetaSameMeta() {
	    try {
		    String type = "tag";
		    String name = "tag22";
		    int mid = 78;
		    metaService.saveMeta(type, name, mid);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Meta.META_IS_EXIST, e.getErrorCode());
        }
	}
	
	
	/**
	 * 查找项目信息，项目类型为空,返回列表为空
	 */
	@Test
	public void testGetMetaListTypeNull() {
		String type = null;
		String orderBy = "count desc, a.mid desc";
		int limit = 3;
		List<MetaDto> metaList = metaService.getMetaList(type, orderBy, limit);
		if(null == metaList) {
			System.out.println("test succcess");
		}else {
			fail("Expected BusinessException to be thrown");
		}
	}
	
	/**
	 * 查找项目信息，排序顺序为空，自动附上默认值
	 */
	@Test
	public void testGetMetaListOrderByNull() {
		String type = "tag";
		String orderBy = null;
		int limit = 3;
		List<MetaDto> metaList = metaService.getMetaList(type, orderBy, limit);
		if(null != metaList) {
			System.out.println(metaList);
			System.out.println("test succcess");
		}else {
			fail("Expected BusinessException to be thrown");
		}
	}
	
	/**
	 * 查找项目信息，返回数量为空，默认赋值10条
	 */
	@Test
	public void testGetMetaListLimitNull() {
	    String type = "tag";
	    String orderBy = "count desc, a.mid desc";
	    Integer limit = null;
	    // 传递 -1 表示没有设置 limit
	    List<MetaDto> metaList = metaService.getMetaList(type, orderBy, limit != null ? limit : -1);
	    if (metaList != null) {
	        System.out.println("Total number of items: " + metaList.size());
	        System.out.println("test success");
	    } else {
	        fail("Expected BusinessException to be thrown");
	    }
	}

	/**
	 * 查找项目信息，所有值正确传入
	 */
	@Test
	public void testGetMetaSuccess() {
	    String type = "tag";
	    String orderBy = "count desc, a.mid desc";
	    int limit = 1;
	    List<MetaDto> metaList = metaService.getMetaList(type, orderBy, limit);
	    if (metaList != null) {
	        System.out.println("Total number of items: " + metaList.size()+metaList);
	        System.out.println("test success");
	    } else {
	        fail("Expected BusinessException to be thrown");
	    }
	}
	
	/**
	 * 添加项目信息，cid为空
	 */
	@Test 
	public void testAddMetasCidNull() {
		try {
			Integer cid = null;
			String names = "add";
			String type = "category";
			metaService.addMetas(cid, names, type);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 添加项目信息成功
	 */
	@Test 
	public void testAddMetasCidSuccess() {
		Integer cid = 15;
		String names = "addMetas15";
		String type = "category";
		metaService.addMetas(cid, names, type);
	}
	
	/**
	 * 新增数据，对象为空抛出异常
	 */
	@Test
	public void testSaveOrUpdateWithMultipleResults() {
	    // Arrange
	    Integer cid = 13;
	    String name = "add";
	    String type = "add";
	    MetaDomain meta1 = new MetaDomain();
	    meta1.setMid(1);
	    meta1.setName(name);
	    meta1.setType(type);
	    MetaDomain meta2 = new MetaDomain();
	    meta2.setMid(2);
	    meta2.setName(name);
	    meta2.setType(type);
	    metaService.addMea(meta1);
	    metaService.addMea(meta2);
	    try {
	        metaService.saveOrUpdate(cid, name, type);
	        fail("Expected BusinessException to be thrown");
	    } catch (BusinessException e) {
	        assertEquals(ErrorConstant.Meta.NOT_ONE_RESULT, e.getErrorCode());
	    }
	}

	
	@Test
    public void testSaveOrUpdate() {
        Integer cid = 2;
        String name = "testTag";
        String type = "tag";
        // 调用 saveOrUpdate 方法
        metaService.saveOrUpdate(cid, name, type);
        MetaCond metaCond = new MetaCond();
        metaCond.setName(name);
        metaCond.setType(type);
        List<MetaDomain> metaList = metaService.getMetas(metaCond);
        System.out.println("MetaList Size: " + metaList.size());
        System.out.println("Meta Details: " + metaList);
        Long count = relationShipDao.getCountById(cid, metaList.get(0).getMid());
        System.out.println("Relationship Count: " + count);
        if (count > 0) {
            System.out.println("Test success: Relationship created between content ID " + cid + " and meta ID " + metaList.get(0).getMid());
        } else {
            System.out.println("Test failed: Relationship was not created.");
        }
    }
	
	/**
	 * 增加项目信息为空
	 */
	@Test 
	public void testAddMeaNull() {
		try {
			metaService.addMea(null);
          fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 增加项目信息成功
	 */
	@Test 
	public void testAddMeaSuccess() {
		MetaDomain meta = new MetaDomain();
		meta.setName("addMeaSuccess");
		meta.setType("tag");
		metaService.addMea(meta);
	}
	
	/**
	 * 更新项目信息为空
	 */
	@Test 
	public void testUpdateMetaNull() {
		try {
			metaService.updateMeta(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 更新项目信息成功
	 */
	@Test 
	public void testUpdateMetaSuccess() {
		MetaDomain meta = new MetaDomain();
		meta.setName("updateMeta");
		meta.setMid(15);
		meta.setType("updateMeta");
		metaService.updateMeta(meta);
	}
	
	/**
	 * 根据类型查找数据为空
	 */
	@Test 
	public void testGetMetasCountByTypeNull() {
		try {
			metaService.getMetasCountByType(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 根据类型查找数据
	 */
	@Test 
	public void testGetMetasCountByTypeSuccess() {
		Long longl = metaService.getMetasCountByType("tag");
		System.out.println(longl);
	}
	
	
	/**
	 *根据名字查找信息为空
	 */
	@Test 
	public void testGetMetaByNameNull() {
		try {
			metaService.getMetaByName(null, null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	  * 根据名字查找信息成功
	 */
	@Test 
	public void testGetMetaByName() {
		MetaDomain meta = metaService.getMetaByName("tag", "tag22");
		System.out.println(meta);
	}
	
	/**
	 *根据id删除项目信息，id为空
	 */
	@Test 
	public void testDeleteMetaByIdNull() {
		try {
			metaService.deleteMetaById(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 *根据id删除项目信息
	 */
	@Test 
	public void testDeleteMetaById() {
		metaService.deleteMetaById(675);
	}
	
	/**
	 * 获取所有项目
	 */
	@Test
	public void testGetMetas() {
		MetaCond metaCond = new MetaCond();
		metaCond.setType("tag");
		List<MetaDomain> metaList = metaService.getMetas(metaCond);
		System.out.println(metaList);
	}
}
