package com.aearn.takeout.controller;

import com.aearn.takeout.common.BaseContext;
import com.aearn.takeout.common.R;
import com.aearn.takeout.entity.User;
import com.aearn.takeout.service.UserService;
import com.aearn.takeout.utils.SMSUtils;
import com.aearn.takeout.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /*
        发送手机短信验证码
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        String code = ValidateCodeUtils.generateValidateCode4String(4);
        if (StringUtils.isNotEmpty(phone)){
            redisTemplate.opsForValue().set("login:code:"+phone,code,5, TimeUnit.MINUTES);
            return R.success("手机验证码短信发送成功");

        }
        return R.error("验证码发送失败");
    }
    @PostMapping("/login")
    public R<String> login(@RequestBody Map map,HttpServletRequest request){
        //获取手机号
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        return userService.login(phone,code);
    }

    @PostMapping("/loginout")
    public R<String> loginOut(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        redisTemplate.delete("login:token:"+token);
        BaseContext.remove();
        return R.success(token);
    }


}
