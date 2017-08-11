package com.toutiao.dao;

import com.toutiao.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {

    String TABLE_NAME="message";
    String INSERT_FIELDS="from_id,to_id,conversation_id,content,created_date,has_read";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

    //添加信息
    @Insert({"insert into ",TABLE_NAME," (",INSERT_FIELDS,")" +
            "values(#{fromId},#{toId},#{conversationId},#{content},#{createdDate},#{hasRead})"})
    int addMessage(Message message);

    //获取指定对话ID的消息列表
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where conversation_id=#{conversationId} " +
            "order by id desc limit #{offset},#{limit}"})
    List<Message> getMessageByConversationId(@Param("conversationId") String conversationId,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);

    //获取指定用户的对话列表
    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} " +
            "or to_id=#{userId} order by id desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    //查询指定对话的未读消息数量
    @Select({"select count(id) from",TABLE_NAME,"where to_id=#{userId} and " +
            "has_read=0 and conversation_id=#{conversationId}"})
int getConversationUnreadCount(@Param("userId") int userId,
                               @Param("conversationId") String conversationId);

    //更新hasread
    @Update({"update",TABLE_NAME,"set has_read=1 where conversation_id=#{conversationId}"})
    void updateHasRead(@Param("conversationId") String conversationId);


}
