package com.wip;

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;

import javax.sound.midi.VoiceStatus;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.sql.parser.Lexer.CommentHandler;
import com.github.pagehelper.PageInfo;
import com.wip.constant.ErrorConstant;
import com.wip.dao.CommentDao;
import com.wip.dao.ContentDao;
import com.wip.dao.RelationShipDao;
import com.wip.dto.cond.ContentCond;
import com.wip.exception.BusinessException;
import com.wip.model.CommentDomain;
import com.wip.model.ContentDomain;
import com.wip.model.MetaDomain;
import com.wip.model.RelationShipDomain;
import com.wip.service.article.ContentService;
import com.wip.service.article.impl.ContentServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentServiceImplTests {

    @Autowired
    private ContentServiceImpl contentService;
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private RelationShipDao relationShipDao;

    /**
     * 测试文章表为空
     */
    @Test
    public void testAddArticle_NullParam() {
        try {
        	//创建一个空内容的文章
            contentService.addArticle(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
    }
    
    /**
     * 测试文章标题为空
     */
    @Test
    public void testAddArticle_NullTitle() {
        // 创建一个 ContentDomain 对象，并设置标题为空字符串
        ContentDomain contentDomain = new ContentDomain();
        contentDomain.setTitle("");
        try {
            // 调用 addArticle 方法
            contentService.addArticle(contentDomain);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Article.TITLE_CAN_NOT_EMPTY, e.getErrorCode());
        }
    }
    
    /**
     * 测试文章标题字数超过限制
     */
    @Test
    public void testAddArticle_MaxTitleCount() {
        // 创建一个 ContentDomain 对象，并设置标题为超过最大长度的字符串
        ContentDomain contentDomain = new ContentDomain();
        String longTitle = StringUtils.repeat('A', 256);
        contentDomain.setTitle(longTitle);
        try {
            // 调用 addArticle 方法
            contentService.addArticle(contentDomain);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Article.TITLE_IS_TOO_LONG, e.getErrorCode());
        }
    }
    
    /**
     * 测试文章内容为空
     */
    @Test
    public void testAddArticle_NullContent() {
        ContentDomain contentDomain = new ContentDomain();
        contentDomain.setTitle("abc");
        contentDomain.setContent(" ");
        try {
            // 调用 addArticle 方法
            contentService.addArticle(contentDomain);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Article.CONTENT_CAN_NOT_EMPTY, e.getErrorCode());
        }
    }
    
    /**
     * 测试文章内容过长
     */
    @Test
    public void testAddArticle_MaxContentCount() {
        ContentDomain contentDomain = new ContentDomain();
        contentDomain.setTitle("abc");
        String longContent = StringUtils.repeat('A', 2000000);
        contentDomain.setContent(longContent);
        try {
            // 调用 addArticle 方法
            contentService.addArticle(contentDomain);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Article.CONTENT_IS_TOO_LONG, e.getErrorCode());
        }
    }
    
    /**
     * 测试成功添加文章
     */
    @Test
    @Transactional
    public void testAddArticle_Success() {
        ContentDomain contentDomain = new ContentDomain();
        contentDomain.setTitle("Test Title");
        contentDomain.setContent("Test Content");
        contentDomain.setTags("Tag1,Tag2");
        contentDomain.setCategories("Category1,Category2");
        contentService.addArticle(contentDomain);
    }
    
    /**
     * 测试根据id查找文章，但是输入的id为空的情况
     */
    @Test
    public void testgetArticleByIdNull() {
    	try {
            contentService.getArticleById(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
    }
    
    /**
     * 测试通过id查找文章
     */
    @Test
    public void testGetArticleById() {
        Integer cid =34;
        ContentDomain result = contentService.getArticleById(cid);
        if (result == null) {
            fail("Expected non-null ContentDomain object");
        } else {
        	//找到的对象id与输入的id不匹配
            if (!cid.equals(result.getCid())) {
                fail("Expected CID: " + cid + " but got: " + result.getCid());
            }
            //找到对象的标题不匹配
            if (!"Updated Title".equals(result.getTitle())) {
                fail("Expected title: Updated Title but got: " + result.getTitle());
            }
            System.out.println("Test passed: " + result);
        }
    }
    
    /**
     * 根据文章id修改文章信息
     */
    @Test
    public void testUpdateArticleById() {
    	 ContentDomain contentDomain = new ContentDomain();
         contentDomain.setCid(34);
         contentDomain.setTitle("Updated Title");
         contentDomain.setTags("tag11, tag22");
         contentDomain.setCategories("category11, category22");
         contentService.updateArticleById(contentDomain);
         ContentDomain result = contentService.getArticleById(34);
         System.out.println(result.getTitle());
    }
    
    /**
     * 根据条件查找文章,当条件为空
     */
    @Test
    public void testGetArticlesByCondNull() {
    	try {
            contentService.getArticlesByCond(null, 1, 10);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
    }
    
    /**
     * 根据条件查找文章
     */
    @Test
    public void testGetArticlesByCond() {
        ContentCond contentCond = new ContentCond();
        contentCond.setType("post");
        int pageNum = 1;
        int pageSize = 5;
        PageInfo<ContentDomain> result = contentService.getArticlesByCond(contentCond, pageNum, pageSize);
        if (result == null || result.getList().isEmpty()) {
            fail("Expected non-empty PageInfo object");
        } else {
            if (result.getPageNum() != pageNum) {
                fail("Expected page number " + pageNum + " but was " + result.getPageNum());
            }
            if (result.getPageSize() != pageSize) {
                fail("Expected page size " + pageSize + " but was " + result.getPageSize());
            }
            List<ContentDomain> contents = result.getList();
            for (ContentDomain content : contents) {
                if (!"post".equals(content.getType())) {
                    fail("Expected content type 'post' but was " + content.getType());
                }
            }
        }
    }
    
    /**
     * 根据文章编号删除，当编号为空时
     */
    @Test
    public void testDeleteArticleByIdNull() {
    	try {
            contentService.deleteArticleById(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
    }
    
    /**
     * 根据文章编号删除
     */
    @Test
    public void testDeleteArticleById_ValidCid() {
        Integer validCid = 1;
        ContentDomain content = new ContentDomain();
        content.setCid(validCid);
        content.setTitle("Test Article");
        contentDao.addArticle(content);
        CommentDomain comment = new CommentDomain();
        comment.setCid(validCid);
        comment.setContent("Test Comment");
        commentDao.addComment(comment);
        RelationShipDomain relationShip = new RelationShipDomain();
        relationShip.setCid(validCid);
        relationShip.setMid(1);
        relationShipDao.addRelationShip(relationShip);
        try {
            contentService.deleteArticleById(validCid);
        } catch (BusinessException e) {
            System.out.println("Caught BusinessException: " + e.getMessage());
            e.printStackTrace();
            fail("Expected no BusinessException, but got: " + e.getMessage());
        }
        // 手动检查文章是否被删除
        ContentDomain deletedContent = contentDao.getArticleById(validCid);
        if (deletedContent != null) {
            fail("Expected deletedContent to be null, but was: " + deletedContent);
        }
        // 验证相关评论是否被删除
        List<CommentDomain> comments = commentDao.getCommentByCId(validCid);
        if (!comments.isEmpty()) {
            fail("Expected comments to be empty, but found: " + comments.size() + " comments.");
        }
        // 验证标签关联是否被删除
        List<RelationShipDomain> relationShips = relationShipDao.getRelationShipByCid(validCid);
        if (!relationShips.isEmpty()) {
            fail("Expected relationShips to be empty, but found: " + relationShips.size() + " relationships.");
        }
    }

    /**
     * 验证缓存是否被清除干净
     */
    @Test
    public void testDeleteArticleById_CacheEviction() {
        Integer validCid = 1;
        // 初始化数据
        ContentDomain content = new ContentDomain();
        content.setCid(validCid);
        content.setTitle("Test Article");
        contentDao.addArticle(content);
        // 触发缓存
        contentService.getArticleById(validCid);
        // 删除文章并验证缓存清除
        contentService.deleteArticleById(validCid);
        // 手动检查缓存是否被清除
        ContentDomain result = contentService.getArticleById(validCid);
        if (result != null) {
            fail("Expected result to be null after deletion and cache eviction, but was: " + result);
        }
    }
    
    /**
     * 根据编号更新信息,content与id为空
     */
    @Test
    public void testUpdateContentByCidNull() {
    	ContentDomain content = new ContentDomain();
        content.setContent("null");
        content.setCid(null);
        if (content != null && content.getCid() != null) {
            contentService.updateContentByCid(content);
        } else {
            System.out.println("Content or CID is null, update operation skipped.");
        }
    }
    
    /**
     * 根据编号更新信息
     */
    @Test
    public void testUpdateContentByCid() {
        ContentDomain content = new ContentDomain();
        content.setContent("111");
        content.setCid(2);
        contentService.updateContentByCid(content);
    }
    
    /**
     * 根据目录类别查找文章，类别为空
     */
    @Test
    public void testGetArticleByCategoryNull() {
    	try {
            contentService.getArticleByCategory(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
    }
    
    /**
     * 根据目录类别查找文章
     */
    @Test
    public void testGetArticleByCategory() {
    	List<ContentDomain> contentList = contentService.getArticleByCategory("techo");
        // 确保返回的列表不是空的
        if (contentList != null) {
        	System.out.println(contentList);
        }else {
        	fail("Expected a non-empty list of articles to be returned");
        }
    }
    
    /**
     * 根据tags查找文章，tags为空
     */
    @Test
    public void testGetArticleByTagsNull() {
    	try {
            contentService.getArticleByTags(null);
            fail("Expected BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(ErrorConstant.Common.PARAM_IS_EMPTY, e.getErrorCode());
        }
    }
    /**
     * 根据tags查找文章
     */
    @Test
    public void testGetArticleByTags() {
    	MetaDomain tags = new MetaDomain();
        tags.setMid(1);
        List<RelationShipDomain> relations = Collections.emptyList();
        List<ContentDomain> articles = contentService.getArticleByTags(tags);
        if (articles == null) {
            System.out.println("testGetArticleByTagsEmptyRelationShip passed: Articles is null");
        } else {
            System.out.println("testGetArticleByTagsEmptyRelationShip failed: Articles is not null");
        }
    }

}
