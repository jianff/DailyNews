package com.toutiao.service;

import com.toutiao.dao.LoginTicketDAO;
import com.toutiao.dao.UserDAO;
import com.toutiao.model.LoginTicket;
import com.toutiao.model.User;
import com.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.toutiao.util.ToutiaoUtil.MD5;

/**
 * Created by jianf on 2017/6/29.
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    public User getUserById(int id){
        return userDAO.selectById(id);
    }


    public User getUserByName(String name){
        return userDAO.selectByName(name);
    }

    //添加用户
    public Map<String,Object> register(String username, String password,String ip){

        Map<String,Object> map=new HashMap<>();
        //验证用户名和密码是否符合规范
        if(StringUtils.isEmpty(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }

        if(StringUtils.isEmpty(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }

        User user=userDAO.selectByName(username);
        if (user!=null){
            map.put("msgname","用户名已存在");
            return map;
        }
        //插入用户
        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,6));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        user.setPassword(MD5(password+user.getSalt()));
        user.setIp(ip);

        userDAO.addUser(user);

        return map;
    }

    public  Map<String,Object> login(String username,String password){
        Map<String,Object> map=new HashMap<>();
        //验证用户名和密码是否符合规范
        if(StringUtils.isEmpty(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }

        if(StringUtils.isEmpty(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }

        User user= userDAO.selectByName(username);
        if (user==null){
           map.put("msgname","用户名不存在");
           return map;
        }

        //判断密码是否正确
        if (!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码错误");
            return map;
        }

        //登陆-下发ticket
        String ticket= addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    //产生新的ticket
    private String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addLoginTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
