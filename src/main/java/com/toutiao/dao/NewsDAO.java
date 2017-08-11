package com.toutiao.dao;

import com.toutiao.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //添加news
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    //获取指定的一部分news
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);

    //获取指定ID的news
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{newsId}"})
    News selectNewsById(int newsId);

    //更新评论数
    @Update({"update",TABLE_NAME,"set comment_count=#{commentCount} where id=#{newsId}"})
    void updateCommentCount(@Param("commentCount") int commentCount,@Param("newsId") int newsId);

    //更新点赞数
    @Update({"update",TABLE_NAME,"set like_count=#{likeCount} where id=#{newsId}"})
    void updateLikeCount(@Param("likeCount") int likeCount,@Param("newsId") int newsId);





}
