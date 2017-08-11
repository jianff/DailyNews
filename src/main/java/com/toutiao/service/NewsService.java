package com.toutiao.service;

import com.toutiao.dao.NewsDAO;
import com.toutiao.model.News;
import com.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    //添加资讯
    public void addNews(News news){
        newsDAO.addNews(news);
    }

    //获取最新资讯
    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    //获取指定ID的资讯
    public News getNewsById(int newsId){
        return newsDAO.selectNewsById(newsId);
    }

    //图片上传
    public String saveImage(MultipartFile file) throws IOException{
        int dotPos=file.getOriginalFilename().lastIndexOf(".");
        if (dotPos<0) {
            return null;
        }
        String fileExtd=file.getOriginalFilename().substring(dotPos+1).toLowerCase();
        if (!ToutiaoUtil.isFileAollowed(fileExtd)){
            return null;
        }

        String fileName= UUID.randomUUID().toString().replace("-","")+"."+fileExtd;
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        //return ToutiaoUtil.TIOUTIAO_DOMAIN+"image?name="+fileName;
        return fileName;

    }

    //更新评论数
    public void updateCommentCount(int commentCount,int newsId){
        newsDAO.updateCommentCount(commentCount,newsId);
    }

    //更新点赞数
    public void updateLikeCount(int likeCount,int newsId){
        newsDAO.updateLikeCount(likeCount,newsId);
    }
}
