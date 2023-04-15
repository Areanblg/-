package com.aearn.takeout.common;

import io.jsonwebtoken.*;

public class JWT {
    private static String  signature = "phones";

    public static String getToken(String phone){
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                .setHeaderParam("typ","JWT")  //头信息 token类型
                .setHeaderParam("alg","HS256") //加密算法
                .claim("phone",phone)
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        return jwtToken;
    }
    public static String getPhone(String token){
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return (String)body.get("phone");
    }


}
