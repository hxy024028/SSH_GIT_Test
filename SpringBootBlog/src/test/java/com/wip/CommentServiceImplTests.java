package com.wip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wip.constant.ErrorConstant;
import com.wip.dao.CommentDao;
import com.wip.dto.cond.CommentCond;
import com.wip.exception.BusinessException;
import com.wip.model.CommentDomain;
import com.wip.model.ContentDomain;
import com.wip.service.article.ContentService;
import com.wip.service.comment.impl.CommentServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceImplTests {
	@Autowired
	private CommentServiceImpl commentService;
	@Autowired
    private CommentDao commentDao;
    @Autowired
    private ContentService contentService;
	
    @Test
    public void testAddCommentSuccess() {
        CommentDomain comment = new CommentDomain();
        comment.setCid(34);
        comment.setCreated(1);
        comment.setAuthor("Test Author");
        comment.setAuthorId("2");
        comment.setOwnerId(1);
        comment.setContent("Test Comment Content");
        comment.setEmail("test@example.com");
        comment.setUrl("111");
        comment.setIp("333");
        comment.setAgent("23");
        comment.setType("approved");
        comment.setStatus("approved");
        comment.setParent(22);
        commentService.addComment(comment);
    }

	
	/**
	 * 根据文章id获取评论，编号为空
	 */
	@Test
	public void testGetCommentsByCIdNull() {
		try {
			commentService.getCommentsByCId(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 根据文章id获取评论，评论的状态必须为approved才能被查询到
	 */
	@Test
	public void testGetCommentsByCId() {
		List<CommentDomain> commentList = commentService.getCommentsByCId(1);
		 if(null != commentList) {
			 System.out.println(commentList);
		 }else {
			 fail("Expected Comments cannot be found!");
		 }
	}
	
	/**
	 * 根据条件获取评论列表，条件为空
	 */
	@Test
	public void tsetGetCommentsByCondNull() {
		try {
			commentService.getCommentsByCond(null, 1, 2);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 根据条件获取评论列表,采用分页的形式
	 */
	@Test
	public void tsetGetCommentsByCond() {
		int pageNum = 3;
		int pageSize = 3;
		CommentCond commentCond = new CommentCond();
		commentCond.setStatus("approved");
		PageHelper.startPage(pageNum,pageSize);
		PageInfo<CommentDomain> comments = commentService.getCommentsByCond(commentCond, pageNum, pageSize);
		if (comments != null) {
            System.out.println("testGetAtts passed: PageInfo is not null");
            System.out.println("Total: " + comments.getTotal());
            System.out.println("List size: " + comments.getList().size());
        } else {
            System.out.println("testGetAtts failed: PageInfo is null");
        }
		List<CommentDomain> atts = comments.getList();
        boolean hasData = atts != null && !atts.isEmpty();
        if (hasData) {
            System.out.println("testGetAtts passed: PageInfo contains data");
        } else {
            System.out.println("testGetAtts failed: PageInfo does not contain data");
        }
	}
	
	/**
	 * 根据编号查看评论，编号为空
	 */
	@Test
	public void testGetCommentByIdNull() {
		try {
			commentService.getCommentById(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 根据编号查看评论
	 */
	@Test
	public void testGetCommentById() {
		int coid = 4;
		CommentDomain commentDomain = new CommentDomain();
		commentDomain = commentService.getCommentById(coid);
		if(null != commentDomain) {
			System.out.println(commentDomain.getStatus());
		}else {
			 fail("Expected Comment can not be found!");
		}
	}
	
	/**
	 * 根据评论编号，更新评论状态，编号为空
	 */
	@Test 
	public void testUpdateCommentStatusNull() {
		try {
			commentService.updateCommentStatus(null, "approved");;
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 根据评论编号，更新评论状态
	 */
	@Test 
	public void testUpdateCommentStatus() {
		commentService.updateCommentStatus(4,"no");
		testGetCommentById();
	}
	
	/**
	 * 根据评论编号删除评论，编号为空
	 */
	@Test 
	public void testDeleteCommentNull() {
		try {
			commentService.deleteComment(null);;
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
	}
	
	/**
	 * 根据评论编号删除评论,测试过一次就需要换一个编号，否则会报错
	 */
	@Test 
	public void testDeleteComment() {
		commentService.deleteComment(18);
		testGetCommentsByCId();
	}
}
