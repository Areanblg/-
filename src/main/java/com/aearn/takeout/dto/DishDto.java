package com.aearn.takeout.dto;

import com.aearn.takeout.entity.Dish;
import com.aearn.takeout.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    //获取菜品的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
