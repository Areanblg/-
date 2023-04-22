package com.aearn.takeout.interceptor;

import com.aearn.takeout.common.BaseContext;
import com.aearn.takeout.common.R;
import com.aearn.takeout.entity.User;
import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录拦截
 */
public class UserLoginInterception implements HandlerInterceptor {
    private StringRedisTemplate redisTemplate = null;

    public UserLoginInterception(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求头中的token
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)){
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return false;
        }
        Map<Object, Object> userMap = redisTemplate.opsForHash().entries("login:token:" + token);
        if (!userMap.isEmpty()){
            //将查询到的hash数据转化为UserDto对象
            User user = JSON.parseObject(JSON.toJSONString(userMap), User.class);
            BaseContext.setCurrentId(user.getId());
            //7.刷新token有效期
            redisTemplate.expire("login:token:" + token,30, TimeUnit.MINUTES);
            return true;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return false;
    }
}
