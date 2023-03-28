package com.aearn.takeout.controller;

import com.aearn.takeout.common.R;
import com.aearn.takeout.entity.User;
import com.aearn.takeout.service.UserService;
import com.aearn.takeout.utils.SMSUtils;
import com.aearn.takeout.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /*
        发送手机短信验证码
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        String code = ValidateCodeUtils.generateValidateCode4String(4);
        log.info(code+"------------------------");
        System.out.println(phone+"---------------------");
        if (StringUtils.isNotEmpty(phone)){
            //生成一个四位的验证码
            //把验证码发送到手机号短信上
//            SMSUtils.sendMessage("aearn","SMS_274365085",phone,code);
            //需要将生成的验证码保存到session
            session.setAttribute(phone,code);
            return R.success("手机验证码短信发送成功");

        }
        return R.error("验证码发送失败");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        //获取手机号
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        Object codeInSession = session.getAttribute(phone);

        if (codeInSession != null && codeInSession.equals(code)){
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone,phone);
            User user = userService.getOne(wrapper);
            if (user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/loginout")
    public R<String> loginOut(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }


}
