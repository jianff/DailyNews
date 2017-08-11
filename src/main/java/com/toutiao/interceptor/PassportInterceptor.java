package com.toutiao.interceptor;

import com.toutiao.dao.LoginTicketDAO;
import com.toutiao.dao.UserDAO;
import com.toutiao.model.HostHolder;
import com.toutiao.model.LoginTicket;
import com.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by jianf on 2017/7/10.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{

    @Autowired
    private LoginTicketDAO loginTicketDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket=null;
        //是否有ticket
        if (httpServletRequest.getCookies()!=null){
            for (Cookie cookie:httpServletRequest.getCookies()){
               if (cookie.getName().equals("ticket")){
                   ticket=cookie.getValue();
                   break;
               }
            }
        }
        //ticket是否有效
        if (ticket!=null){
            LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
            if (loginTicket==null||loginTicket.getExpired().before(new Date())||
                    loginTicket.getStatus()!=0){
                return true;
            }

            //若有效，将用户加入线程变量
            User user=userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView!=null&&hostHolder!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
