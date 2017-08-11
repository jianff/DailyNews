package com.toutiao.controller;

import com.toutiao.async.EventModel;
import com.toutiao.async.EventProducer;
import com.toutiao.async.EventType;
import com.toutiao.model.EntityType;
import com.toutiao.model.HostHolder;
import com.toutiao.model.News;
import com.toutiao.service.LikeService;
import com.toutiao.service.NewsService;
import com.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;
    @Autowired
    EventProducer eventProducer;

    //点赞
    @RequestMapping(path={"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId){
        News news = newsService.getNewsById(newsId);
        int userId=hostHolder.getUser().getId();

        if (hostHolder.getUser()==null){
            return ToutiaoUtil.getJSONString(1,"请先登陆");
        }

        long likeCount=likeService.like(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount((int)likeCount,newsId);

        //产生点赞事件
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(userId).setEntityId(newsId)
                .setEntityOwnerId(news.getUserId()).setEntityType(EntityType.ENTITY_NEWS));

        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

    //点踩
    @RequestMapping(path={"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("newsId") int newsId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.disLike(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount((int)likeCount,newsId);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }


}
