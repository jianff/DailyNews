package com.toutiao.controller;

import com.toutiao.model.*;
import com.toutiao.service.*;
import com.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jianf on 2017/7/12.
 */
@Controller
public class NewsController {

    @Autowired
    NewsService newsService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    LikeService likeService;

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    //上传图片
    @RequestMapping(value = "/uploadImage/",method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file ){
        try{
            String fileUrl=newsService.saveImage(file);   //上传到本地
            //String fileUrl=qiniuService.saveImage(file);    //上传到七牛云
            if (fileUrl==null){
                return ToutiaoUtil.getJSONString(1,"上传失败");
            }
            return ToutiaoUtil.getJSONString(0,fileUrl);
        }catch(Exception e){
            logger.error("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传失败");

        }
    }

    //展示图片
    @RequestMapping(value = "/image",method = RequestMethod.GET)
    @ResponseBody
    public void getImage(@RequestParam("name") String imagename, HttpServletResponse response){
        try{
            response.setContentType("image/jpg");
            StreamUtils.copy(new FileInputStream(
                    new File(ToutiaoUtil.IMAGE_DIR+imagename)),
                    response.getOutputStream());
        }catch (Exception e){
            logger.error("图片读取错误"+imagename+e.getMessage());
        }
    }

    //发布资讯
    @RequestMapping(path={"/user/addNews"},method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String addNews(@RequestParam("image") String imageurl,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){

        try {
            News news=new News();

            news.setImage(imageurl);
            news.setTitle(title);
            news.setLink(link);
            news.setCreatedDate(new Date());
            if (hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());
            }else {
                //设置匿名用户
                news.setUserId(14);
            }

            newsService.addNews(news);

        }catch (Exception e){
            logger.error("发布资讯错误",e.getMessage());
        }
        return ToutiaoUtil.getJSONString(0,"发布资讯成功");
    }

    //资讯详情
    @RequestMapping(value={"/news/{newsId}"}, method={RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news=newsService.getNewsById(newsId);
        if (news==null){
            return ToutiaoUtil.getJSONString(1,"该新闻不存在");
        }

        int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
        if (localUserId!=0) {
            int likeStatus = likeService.getLikeStatus(localUserId,EntityType.ENTITY_NEWS,newsId);
            model.addAttribute("like",likeStatus);
        }else{
            model.addAttribute("like",0);
        }
            //评论
        List<Comment> comments=commentService.getCommentsByEntity(newsId, EntityType.ENTITY_NEWS);
        List<ViewObject> commentVOs=new ArrayList<>();

        for (Comment comment:comments){
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUserById(comment.getUserId()));
            commentVOs.add(vo);
        }
        model.addAttribute("comments",commentVOs);
        model.addAttribute("news",news);
        model.addAttribute("owner",userService.getUserById(news.getUserId()));
        return "detail";
    }

    //添加评论
    @RequestMapping(value = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){

        Comment comment=new Comment();
        comment.setContent(content);
        comment.setEntityId(newsId);
        comment.setEntityType(EntityType.ENTITY_NEWS);
        comment.setCreatedDate(new Date());
        comment.setUserId(hostHolder.getUser().getId());

        commentService.addComment(comment);

        //更新评论数
        int count=commentService.getCommentsCount(newsId,EntityType.ENTITY_NEWS);
        newsService.updateCommentCount(count,newsId);

        return "redirect:/news/"+String.valueOf(newsId);
    }

}
