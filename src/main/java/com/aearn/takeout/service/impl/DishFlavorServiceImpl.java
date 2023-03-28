package com.aearn.takeout.service.impl;

import com.aearn.takeout.entity.DishFlavor;
import com.aearn.takeout.mapper.DishFlavorMapper;
import com.aearn.takeout.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
