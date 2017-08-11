package com.toutiao.async.handler;

import com.toutiao.async.EventHandler;
import com.toutiao.async.EventModel;
import com.toutiao.async.EventType;
import com.toutiao.model.Message;
import com.toutiao.service.MessageService;
import com.toutiao.util.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class LoginExceptionHandler  implements EventHandler{

    @Autowired
    MessageService messageService;
    @Autowired
    MailSender mailSender;

    private static final Logger logger= LoggerFactory.getLogger(LoginExceptionHandler.class);

    @Override
    public void doHandler(EventModel model) {


        //发送站内信报告异常
        Message message=new Message();
        int toId=model.getActorId();
        message.setFromId(25);
        message.setToId(toId);
        message.setCreatedDate(new Date());
        message.setConversationId(25 > toId ? String.format("%d_%d", toId, 25) :
                String.format("%d_%d", 25, toId));
        message.setContent("您上次登陆IP异常");
        messageService.addMessage(message);

        //发送邮件报告异常
        Map<String,Object> map=new HashMap<>();
        map.put("username",model.getExts("username"));
        mailSender.sendWithHTMLTemplate(model.getExts("email"),"登陆异常",
                "mails/welcome.html",map);
        logger.info("已发送异常报告邮件");
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}
