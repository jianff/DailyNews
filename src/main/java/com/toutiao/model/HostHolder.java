package com.toutiao.model;

import org.springframework.stereotype.Component;

/**
 * Created by jianf on 2017/7/10.
 */
@Component
public class HostHolder {

    private static ThreadLocal<User> users=new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }

    boolean a=true;
    boolean b=true;

    public void test() {
        if (a & b) {

        }
    }
}
