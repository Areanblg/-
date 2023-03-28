package com.aearn.takeout.service;

import com.aearn.takeout.dto.SetmealDto;
import com.aearn.takeout.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.awt.*;
import java.util.List;

public interface SetmealService extends IService<Setmeal> {


    public void saveWithDish(SetmealDto setmealDto);

    /*
        删除套餐，同时删除套餐和菜品的关联数据
     */
    public void removeWithDish(List<Long> ids);
}
