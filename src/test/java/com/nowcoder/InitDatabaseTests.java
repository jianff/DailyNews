package com.nowcoder;

import com.toutiao.ToutiaoApplication;
import com.toutiao.dao.CommentDAO;
import com.toutiao.dao.NewsDAO;
import com.toutiao.dao.UserDAO;
import com.toutiao.model.Comment;
import com.toutiao.model.EntityType;
import com.toutiao.model.News;
import com.toutiao.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/database_test.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;
    @Autowired
    NewsDAO newsDAO;
    @Autowired
    CommentDAO commentDAO;

    @Test
    public void initData() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);


            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);

            for (int j=0;j<4;j++){
                Comment comment=new Comment();
                comment.setUserId(i+1);
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                comment.setStatus(0);
                comment.setContent("content"+String.valueOf(j));
                comment.setCreatedDate(new Date());

                commentDAO.addComment(comment);
            }

            user.setPassword("newpassword");
            userDAO.updatePassword(user);
        }
    }
}
