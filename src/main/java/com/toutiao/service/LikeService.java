package com.toutiao.service;

import com.toutiao.util.JedisAdapter;
import com.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;
    
    //点赞返回1，点踩返回-1，未点返回0
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey= RedisKeyUtil.getLikeKey(entityType,entityId);
        String disLikeKey= RedisKeyUtil.getDisLikeKey(entityType,entityId);

        if (jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        if (jedisAdapter.sismember(disLikeKey,String.valueOf(userId))){
            return -1;
        }
        return 0;
    }

    //点赞
    public long like(int userId,int entityType,int entityId){
        //在指定实体的点赞集合里添加
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        //在指定实体的点踩集合里删除
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    //点踩
    public long disLike(int userId,int entityType,int entityId){
        //在指定实体的点踩集合里添加
        String disLikeKey=RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));
        //在指定实体的点赞集合里删除
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

}
