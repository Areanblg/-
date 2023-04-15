package com.aearn.takeout.common;

import com.aearn.takeout.common.JWT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JWTTest {
    @Test
    public void getToken(){
        String token = JWT.getToken("18739465575");
        System.out.println(token);
    }
    @Test
    public void getPhone(){
        String phone = JWT.getPhone("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IjE4NzM5NDY1NTc1In0.OtRV7ZEGJlm3PP06SbA-Rk9KIZAaovr-WYNXP6lMRGo");
        System.out.println(phone);
    }
}
