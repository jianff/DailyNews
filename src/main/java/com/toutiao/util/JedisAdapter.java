package com.toutiao.util;


import com.alibaba.fastjson.JSON;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;


@Component
public class JedisAdapter implements InitializingBean{

    private static final org.slf4j.Logger logger= LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool=null;

    public static void print(int index,Object obj){
        System.out.print(String.format("%d,%s",index,obj.toString()));
    }

    public static void main(String[] argv){

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool=new JedisPool("localhost",6379);
    }

    private Jedis getJedis(){
        return pool.getResource();
    }


    public void set(String key,String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            jedis.set(key,value);

        }catch (Exception e){
            logger.error("jedis异常"+e.getMessage());
        }finally{
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    public String get(String key){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return  jedis.get(key);
        }catch (Exception e){
            logger.error("jedis异常"+e.getMessage());
            return null;
        }finally{
            if (jedis!=null){
                jedis.close();
            }
        }
    }

    //加入集合
    public long sadd(String key, String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.sadd(key,value);

        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 1;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }

    }

    //从集合删除
    public long srem(String key, String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.srem(key,value);

        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 1;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }

    }

    //判断是否为集合成员
    public boolean sismember(String key, String value){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.sismember(key,value);

        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }

    }

    //获取集合中元素个数
    public long scard(String key){
        Jedis jedis=null;
        try{
            jedis=pool.getResource();
            return jedis.scard(key);

        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 1;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }

    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key,Object obj){
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key,Class<T> clazz){
        String value=get(key);
        if (value!=null){
           return JSON.parseObject(value,clazz);
        }
        return null;
    }

}
