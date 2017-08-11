package com.toutiao.service;

import com.toutiao.dao.CommentDAO;
import com.toutiao.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jianf on 2017/7/18.
 */
@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    public int addComment(Comment comment){
    return commentDAO.addComment(comment);
    }

    public List<Comment> getCommentsByEntity(int entityId,int entityType){
        return commentDAO.getCommentByEntity(entityId,entityType);
    }

    public int getCommentsCount(int entityId,int entityType){
        return commentDAO.getCommentCountByEntity(entityId,entityType);
    }


}
