package com.toutiao.dao;

import com.toutiao.model.LoginTicket;
import com.toutiao.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by jianf on 2017/7/6.
 */
@Mapper
public interface LoginTicketDAO {
    
    
    String TABLE_NAME="login_ticket";
    String INSERT_FIELDS="user_id,expired,status,ticket";
    String SELECT_FIELDS="id,"+INSERT_FIELDS;

    @Insert({"insert into" ,TABLE_NAME,"(",INSERT_FIELDS,")" +
            "values(#{userId},#{expired},#{status},#{ticket})"})
    int addLoginTicket(LoginTicket ticket);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update",TABLE_NAME,"set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);
}
