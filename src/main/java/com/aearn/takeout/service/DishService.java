package com.aearn.takeout.service;

import com.aearn.takeout.dto.DishDto;
import com.aearn.takeout.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    //新增菜品,同时插入菜品对应的口味数据,需要操作两张表:dish、dish_flavor
    void saveWithFlavor(DishDto dto);

    //根据id查询菜品信息和对应的口味信息
    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dto);

    //修改菜品状态
    void updateStatus(Long id,Boolean flag);

    //根据id删除菜品信息和对应的口味信息
    void deleteDishAndFlavor(Long id);
}
