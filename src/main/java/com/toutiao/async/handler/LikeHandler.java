package com.toutiao.async.handler;

import com.toutiao.async.EventHandler;
import com.toutiao.async.EventModel;
import com.toutiao.async.EventType;
import com.toutiao.model.Message;
import com.toutiao.model.News;
import com.toutiao.model.User;
import com.toutiao.service.MessageService;
import com.toutiao.service.NewsService;
import com.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler{

    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    NewsService newsService;

    @Override
    public void doHandler(EventModel model) {

        Message message=new Message();
        User actorUser=userService.getUserById(model.getActorId());
        User ownerUser=userService.getUserById(model.getEntityOwnerId());
        News news=newsService.getNewsById(model.getEntityId());
        int toId=ownerUser.getId();

        message.setToId(toId);
        message.setFromId(25);
        message.setContent(actorUser.getName()+"赞了你的资讯---"
                +"《"+news.getTitle()+"》");
        message.setCreatedDate(new Date());
        message.setConversationId(25 > toId ? String.format("%d_%d", toId, 25) : String.format("%d_%d", 25, toId));

        //检查消息是否已存在
        List<Message> messages= messageService.getConversation(message.getConversationId(),0,100000);
        for (Message msg:messages){
            if (msg.getContent().equals(message.getContent())){
                return;
            }
        }
        //自己赞自己不发消息
        if(actorUser.getId()==ownerUser.getId()){
            return;
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}
