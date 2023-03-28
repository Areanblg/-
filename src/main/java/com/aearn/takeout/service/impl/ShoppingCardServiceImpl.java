package com.aearn.takeout.service.impl;

import com.aearn.takeout.entity.ShoppingCart;
import com.aearn.takeout.mapper.ShoppingCardMapper;
import com.aearn.takeout.service.ShoppingCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCardServiceImpl extends ServiceImpl<ShoppingCardMapper, ShoppingCart> implements ShoppingCardService {
}
