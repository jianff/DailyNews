package com.toutiao.dao;

import com.toutiao.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by jianf on 2017/7/18.
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME="comment";
    String INSERT_FIELDS="user_id,entity_id,entity_type,content,created_date,status";
    String SELECT_FIELDS="id," + INSERT_FIELDS;

    //添加评论
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{userId},#{entityId},#{entityType}," +
            "#{content},#{createdDate},#{status})"})
    int addComment(Comment comment);

    //获取指定entity的所有评论
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where entity_id=#{entityId} and " +
            "entity_type=#{entityType} order by id desc"})
    List<Comment> getCommentByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);

    //获取指定entity的评论数
    @Select({"select count(id)",SELECT_FIELDS,"from",TABLE_NAME,"where entity_id=#{entityId} and " +
            "entity_type=#{entityType}"})
    int getCommentCountByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);



}
