package com.aearn.takeout.service.impl;

import com.aearn.takeout.common.JWT;
import com.aearn.takeout.common.R;
import com.aearn.takeout.dto.UserDto;
import com.aearn.takeout.entity.User;
import com.aearn.takeout.mapper.UserMapper;
import com.aearn.takeout.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private HttpSession httpSession;
    @Override
    public R<String> login(String phone, String code) {

        //1.判断此手机号在缓存中是否存在+判断手机号和验证码是否一致
        String codeCache = redisTemplate.opsForValue().get("login:code:" + phone);
        if (StringUtils.isEmpty(codeCache) || !code.equals(codeCache)){
            return R.error("验证码不正确或已过期");
        }
        //生成Token信息
        String token = JWT.getToken(phone);
        //获取sessionId， 用于判断是否同个浏览器
        String sessionId = httpSession.getId();
        //判断用户是否已经登录
        //1.判断token是否在缓存中存在
        if (token != null){
            String cachePhone = (String)redisTemplate.opsForHash().get("login:token:" + token, "phone");
            String cacheSessionId = (String)redisTemplate.opsForHash().get("login:token:" + token, "sessionId");
            if (phone.equals(cachePhone) && !sessionId.equals(cacheSessionId)){
                //用户已经登录
                return R.error("用户已经在别的地方登录了");
            }
        }
        //2.存在，查看数据库中是否有该用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,phone);
        //2.1如果不存在，注册一个，保存到数据库
        User user = this.getOne(wrapper);
        if (user == null){
            user.setPhone(phone);
            user.setStatus(1);
            save(user);
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setPhone(user.getPhone());
        userDto.setSessionId(sessionId);
        Map<String, String> userMap = null;
        try {
             userMap = BeanUtils.describe(userDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //4.将User信息转为HashMap存储
        String tokenKey = "login:token:"+token;
        assert userMap != null;
        redisTemplate.opsForHash().putAll(tokenKey,userMap);
        //5.设置token有效期
        redisTemplate.expire(tokenKey,30, TimeUnit.MINUTES);

        //5.返回Token
        return R.success(token);
    }
}
