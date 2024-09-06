package com.wip;

import com.wip.dto.StatisticsDto;
import com.wip.model.CommentDomain;
import com.wip.model.ContentDomain;
import com.wip.service.site.impl.SiteServiceImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SiteServiceImplTests {

    @Autowired
    private SiteServiceImpl siteService;

    /**
     * 获取评论列表
     */
    @Test
    public void testGetComments() {
        List<CommentDomain> comments = siteService.getComments(5);
        System.out.println("Number of comments: " + comments.size());
        if (comments.size() == 1) {
            System.out.println("Comment content: " + comments.get(0).getContent());
        } else {
            System.out.println("Test failed: Expected 1 comment, but got " + comments.size());
        }
    }

    /**
     * 获取文章列表
     */
    @Test
    public void testGetNewArticles() {
        List<ContentDomain> articles = siteService.getNewArticles(5);
        System.out.println("Number of articles: " + articles.size());
        if (articles.size() == 1) {
            System.out.println("Article title: " + articles.get(0).getTitle());
        } else {
            System.out.println("Test failed: Expected 1 article, but got " + articles.size());
        }
    }

    /**
     * 获取后台统计数
     */
    @Test
    public void testGetStatistics() {
        StatisticsDto statistics = siteService.getStatistics();
        System.out.println("Articles count: " + statistics.getArticles());
        System.out.println("Comments count: " + statistics.getComments());
        System.out.println("Links count: " + statistics.getLinks());
        System.out.println("Attachments count: " + statistics.getAttachs());
        if (statistics.getArticles() == 10L) {
            System.out.println("Articles count test passed.");
        } else {
            System.out.println("Test failed: Expected 10 articles, but got " + statistics.getArticles());
        }
        if (statistics.getComments() == 5L) {
            System.out.println("Comments count test passed.");
        } else {
            System.out.println("Test failed: Expected 5 comments, but got " + statistics.getComments());
        }
        if (statistics.getLinks() == 3L) {
            System.out.println("Links count test passed.");
        } else {
            System.out.println("Test failed: Expected 3 links, but got " + statistics.getLinks());
        }
        if (statistics.getAttachs() == 8L) {
            System.out.println("Attachments count test passed.");
        } else {
            System.out.println("Test failed: Expected 8 attachments, but got " + statistics.getAttachs());
        }
    }
}
