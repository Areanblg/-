package com.aearn.takeout.service.impl;

import com.aearn.takeout.entity.Dish;
import com.aearn.takeout.entity.Setmeal;
import com.aearn.takeout.entity.SetmealDish;
import com.aearn.takeout.mapper.SetmealDishMapper;
import com.aearn.takeout.service.DishService;
import com.aearn.takeout.service.SetmealDishService;
import com.aearn.takeout.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

}
