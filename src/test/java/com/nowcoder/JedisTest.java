package com.nowcoder;


import com.toutiao.ToutiaoApplication;
import com.toutiao.model.User;
import com.toutiao.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)

public class JedisTest{

    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void testObject(){
        User user = new User();
        user.setHeadUrl("http://image.nowcoder.com/head/100t.png");
        user.setPassword("pwd");
        user.setName("user");
        user.setSalt("salt");

        jedisAdapter.setObject("userxxx",user);
        User u=jedisAdapter.getObject("userxxx",User.class);

        System.out.println(ToStringBuilder.reflectionToString(u));
    }


}
