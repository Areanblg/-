package com.aearn.takeout.service.impl;

import com.aearn.takeout.entity.OrderDetail;
import com.aearn.takeout.mapper.OrderDetailMapper;
import com.aearn.takeout.service.OrderDetailService;
import com.aearn.takeout.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
