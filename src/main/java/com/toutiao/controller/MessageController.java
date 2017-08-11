package com.toutiao.controller;

import com.toutiao.model.HostHolder;
import com.toutiao.model.Message;
import com.toutiao.model.User;
import com.toutiao.model.ViewObject;
import com.toutiao.service.MessageService;
import com.toutiao.service.UserService;
import com.toutiao.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MessageController {

    private static final Logger logger= LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;


    //添加消息
    @RequestMapping(path={"/msg/addMessage"},method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toId") int toId,
                             @RequestParam("fromId") int fromId,
                             @RequestParam("content") String content){
        try {
            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setConversationId(fromId > toId ? String.format("%d_%d", toId, fromId) : String.format("%d_%d", fromId, toId));
            message.setContent(content);
            message.setCreatedDate(new Date());

            messageService.addMessage(message);

            return ToutiaoUtil.getJSONString(0,"消息已提交");
        }catch (Exception e){
            logger.error("添加消息失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"添加消息失败");
        }
    }

    //对话框
    @RequestMapping(path={"/msg/detail"},method= RequestMethod.GET)
    public String conversationDetail(@Param("conversationId") String conversationId, Model model){
       try{
        List<Message> conversationList=messageService.getConversation(conversationId,0,10);
        List<ViewObject> messages=new LinkedList<>();

        for (Message msg:conversationList){
            ViewObject vo=new ViewObject();
            vo.set("message",msg);
            User user=userService.getUserById(msg.getFromId());
            if (user==null){
                continue;
            }
            vo.set("headUrl",user.getHeadUrl());
            vo.set("userId",user.getId());
            messages.add(vo);
        }
        model.addAttribute("messages",messages);
       }catch (Exception e){
           logger.error("获取对话失败"+e.getMessage());
           return ToutiaoUtil.getJSONString(1,"获取对话失败");
       }
       messageService.updateHasRead(conversationId);
       return "letterDetail";
    }

    //获取对话列表
    @RequestMapping(path = {"/msg/list"},method = RequestMethod.GET)
    public String getConversationList(Model model){
       try {
           int localUserId=hostHolder.getUser().getId();
           List<Message> conversations = messageService.getConversationList(localUserId,
                   0, 10);
           if (conversations==null){
               return ToutiaoUtil.getJSONString(1,"无消息");
           }
           List<ViewObject> vos = new ArrayList<>();

           for (Message msg : conversations) {
               ViewObject vo=new ViewObject();
               vo.set("conversation",msg);
                int targetId=msg.getFromId()==localUserId?msg.getToId():msg.getFromId();
                User user=userService.getUserById(targetId);
                vo.set("user",user);
                vo.set("unread",messageService.getConversationUnreadCount(localUserId,
                        msg.getConversationId()));
                vos.add(vo);
           }
          model.addAttribute("conversations",vos);
       }catch(Exception e){
           logger.error("获取对话列表失败"+e.getMessage());
           return ToutiaoUtil.getJSONString(1,"获取对话列表失败");
       }
        return "letter";
    }


}
