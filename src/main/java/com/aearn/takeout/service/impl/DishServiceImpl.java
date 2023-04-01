package com.aearn.takeout.service.impl;

import com.aearn.takeout.dto.DishDto;
import com.aearn.takeout.entity.Dish;
import com.aearn.takeout.entity.DishFlavor;
import com.aearn.takeout.mapper.DishMapper;
import com.aearn.takeout.service.DishFlavorService;
import com.aearn.takeout.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;



    @Override
    @Transactional
    public void saveWithFlavor(DishDto dto){
        //保存菜品的基本信息到菜品表dish
        this.save(dto);
        Long id = dto.getId();
        List<DishFlavor> flavors = dto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());


        //保存菜品口味表dish_flavor
        dishFlavorService.saveBatch(dto.getFlavors());
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
       //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);
        //把基本的菜品信息拷贝过去
        DishDto dto = new DishDto();
        BeanUtils.copyProperties(dish,dto);
        //查询当前菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(wrapper);
        dto.setFlavors(list);
        return dto;
    }

    @Override
    public void updateWithFlavor(DishDto dto) {
        //首先把菜品的主要信息修改进去
        this.updateById(dto);
        //再去删除Flavor中的关于dto中的id的表所关联的数据。
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dto.getId());
        dishFlavorService.remove(wrapper);
        //添加新的数据到Flavor中
        List<DishFlavor> dishFlavors  = dto.getFlavors();
        dishFlavors = dishFlavors.stream().map((item)->{
            item.setDishId(dto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavors);
    }

    @Override
    public void updateStatus(Long id,Boolean flag) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(flag?1:0);
        this.updateById(dish);
    }

    @Override
    public void deleteDishAndFlavor(Long id) {
        //首先删除对应的菜品
        this.removeById(id);
        //再去删除对应菜品的口味信息
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DishFlavor::getDishId,id);
        dishFlavorService.remove(wrapper);
    }


}
