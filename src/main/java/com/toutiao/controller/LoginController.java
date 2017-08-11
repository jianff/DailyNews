package com.toutiao.controller;

import com.toutiao.async.EventModel;
import com.toutiao.async.EventProducer;
import com.toutiao.async.EventType;
import com.toutiao.model.User;
import com.toutiao.service.UserService;
import com.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by jianf on 2017/7/4.
 */
@Controller
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;

    //注册
    @RequestMapping(path={"/reg/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String register( @RequestParam("username") String username,
                           @RequestParam("password") String password,
                            HttpServletRequest request){
       try {
           //记录IP
           String hostIp=ToutiaoUtil.getIpAddr(request);
           Map<String,Object>map=userService.register(username, password,hostIp) ;

           return ToutiaoUtil.getJSONString(0, "注册成功");

       }catch (Exception e){
           return ToutiaoUtil.getJSONString(1,"注册异常");
       }
    }




    //登陆
    @RequestMapping(path={"/login/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("rember") int remberme,
                        HttpServletResponse response,
                        HttpServletRequest request){
        try {
            Map<String,Object>map=userService.login(username, password) ;
            User hostUser = userService.getUserByName(username);

            if (map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if (remberme>0){
                    cookie.setMaxAge(3600*24*3);
                }
                response.addCookie(cookie);

                //判断登陆IP是否异常
                String hostIp=ToutiaoUtil.getIpAddr(request);
                if (!hostIp.equals(hostUser.getIp())){
                //产生异常登录事件
                eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId(hostUser.getId())
                .setExts("username",username).setExts("email","zhangjianfeng0418@outlook.com"));
                }

                return ToutiaoUtil.getJSONString(0,"登陆成功");
            }
            return ToutiaoUtil.getJSONString(1,map);

        }catch (Exception e){
            return ToutiaoUtil.getJSONString(1,"登陆异常");
        }
    }

    //登出
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}
