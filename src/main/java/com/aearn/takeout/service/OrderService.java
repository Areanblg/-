package com.aearn.takeout.service;

import com.aearn.takeout.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
