package com.aearn.takeout.service.impl;

import com.aearn.takeout.common.CustomException;
import com.aearn.takeout.common.R;
import com.aearn.takeout.entity.Category;
import com.aearn.takeout.entity.Dish;
import com.aearn.takeout.entity.Setmeal;
import com.aearn.takeout.mapper.CategoryMapper;
import com.aearn.takeout.service.CategoryService;
import com.aearn.takeout.service.DishService;
import com.aearn.takeout.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;
    /*
        根据Id删除分类，删除之前需要进行判断。
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个异常。
        LambdaQueryWrapper<Dish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Dish::getCategoryId,id);
        int count = dishService.count(wrapper1);
        if (count > 0){
            throw new CustomException("当前分类下关联了一个菜品，不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个异常。
        LambdaQueryWrapper<Setmeal> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(wrapper2);
        if (count2 > 0){
            throw new CustomException("当前分类下关联了一个菜品，不能删除");
        }
        //正常删除
        super.removeById(id);
    }

}
