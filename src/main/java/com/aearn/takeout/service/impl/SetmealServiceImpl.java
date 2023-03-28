package com.aearn.takeout.service.impl;

import com.aearn.takeout.common.CustomException;
import com.aearn.takeout.dto.SetmealDto;
import com.aearn.takeout.entity.Setmeal;
import com.aearn.takeout.entity.SetmealDish;
import com.aearn.takeout.mapper.SetmealMapper;
import com.aearn.takeout.service.SetmealDishService;
import com.aearn.takeout.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal,执行insert操作
        this.save(setmealDto);

        //保存菜单和菜品的关联信息，操作setmeal,执行insert操作

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
           item.setSetmealId(setmealDto.getCategoryId());
           return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，如果状态为启用就无法删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        //抛出异常
        if (count > 0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //如果可以删除，删除表中的数据
        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.removeByIds(ids);
        //删除关系表中的数据
        setmealDishService.remove(wrapper);
    }



}
