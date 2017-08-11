package com.toutiao.service;

import com.toutiao.dao.MessageDAO;
import com.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    //添加消息
    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    //获取指定对话
    public List<Message> getConversation(String conversationId,int offset,int limit){
        return messageDAO.getMessageByConversationId(conversationId,offset,limit);
    }

    //获取指定用户的对话列表
    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }

    //获取未读消息数量
    public int getConversationUnreadCount(int userId,String conversationId){
        return messageDAO.getConversationUnreadCount(userId,conversationId);
    }

    //更改消息为已读状态
    public void updateHasRead(String conversationId){
        messageDAO.updateHasRead(conversationId);
    }

}
