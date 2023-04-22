package com.aearn.takeout.controller;


import com.aearn.takeout.common.BaseContext;
import com.aearn.takeout.common.R;
import com.aearn.takeout.entity.Employee;
import com.aearn.takeout.entity.Orders;
import com.aearn.takeout.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 查询用户订单分页
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,Long number){
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        if (number != null){
            queryWrapper.like(Orders::getNumber,number);
        }
        queryWrapper.orderByDesc(Orders::getId);
        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /*
        修改订单状态
     */
    @PutMapping
    public R<String> status(@RequestBody Orders orders){
        //根据订单id修改状态码
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(orders != null,Orders::getId,orders.getId());
        wrapper.set(Orders::getStatus,orders.getStatus());
        return R.success("ok");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(Integer page,Integer pageSize){
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getId);
        queryWrapper.eq(Orders::getUserId,userId);
        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
}