package com.aearn.takeout.common;

/*
    基于threadLocal封装的工具类,用于保存和获取当前登录用户的id
    作用范围是一个线程之内
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
    public static void remove(){
        threadLocal.remove();
    }
}
