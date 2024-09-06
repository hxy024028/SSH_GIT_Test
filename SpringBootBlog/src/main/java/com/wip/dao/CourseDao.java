/**
 * Created by IntelliJ IDEA.
 * User: Kyrie
 * DateTime: 2018/7/25 16:50
 **/
package com.wip.dao;

import com.wip.dto.cond.ContentCond;
import com.wip.dto.cond.CourseCond;
import com.wip.model.ContentDomain;
import com.wip.model.CourseDomain;
import com.wip.model.RelationShipDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章相关Dao接口
 */
@Mapper
public interface CourseDao {

    /**
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
     * 根据条件获取文章列表
     * @param contentCond
     * @return
     */
    List<CourseDomain> getCourseByCond(CourseCond courseCond);

    /**
     * 删除文章
     * @param cid
     */
    void deleteCourseById(Integer cid);

    /**
     * 获取文章总数
     * @return
     */
    Long getCourseCount();

    /**
     * 通过分类名获取文章
     * @param category
     * @return
     */
    List<CourseDomain> getCourseByCategory(@Param("category") String category);

    /**
     * 通过标签获取文章
     * @param cid
     * @return
     */
    List<CourseDomain> getCourseByTags(List<RelationShipDomain> cid);
}
