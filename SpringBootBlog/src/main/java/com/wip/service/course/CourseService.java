/**
 * Created by IntelliJ IDEA.
 * User: Kyrie
 * DateTime: 2018/7/25 16:48
 **/
package com.wip.service.course;

import com.github.pagehelper.PageInfo;
import com.wip.dto.cond.ContentCond;
import com.wip.dto.cond.CourseCond;
import com.wip.model.ContentDomain;
import com.wip.model.CourseDomain;
import com.wip.model.MetaDomain;

import java.util.List;

/**
 * 文章相关Service接口
 */
public interface CourseService {

    /***
     * 添加文章
     * @param contentDomain
     */
    void addCourse(CourseDomain courseDomain);

    /**
     * 根据编号获取文章
     * @param cid
     * @return
     */
    CourseDomain getCourseById(Integer cid);

    /**
     * 更新文章
     * @param contentDomain
     */
    void updateCourseById(CourseDomain courseDomain);

    /**
     * 根据条件获取教程列表
     * @param contentCond
     * @param page
     * @param limit
     * @return
     */
    PageInfo<CourseDomain> getCoursesByCond(CourseCond courseCond, int page, int limit);

    /**
     * 删除文章
     * @param cid
     */
    void deleteCourseById(Integer cid);

    /**
     * 添加文章点击量
     * @param content
     */
    void updateCourseByCid(CourseDomain course);

    /**
     * 通过分类获取文章
     * @param category
     * @return
     */
    List<CourseDomain> getCourseByCategory(String category);

    /**
     * 通过标签获取文章
     * @param tags
     * @return
     */
    List<CourseDomain> getCourseByTags(MetaDomain tags);
}
