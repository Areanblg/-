package com.aearn.takeout.controller;

import ch.qos.logback.core.db.dialect.DBUtil;
import com.aearn.takeout.common.R;
import com.aearn.takeout.dto.DishDto;
import com.aearn.takeout.entity.Category;
import com.aearn.takeout.entity.Dish;
import com.aearn.takeout.entity.DishFlavor;
import com.aearn.takeout.service.CategoryService;
import com.aearn.takeout.service.DishFlavorService;
import com.aearn.takeout.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishFlavorService dishFlavorService;
    /*
        新增菜品
     */
    @PostMapping
    public R<String> save(@RequestBody  DishDto dto){
        log.info(dto.toString());
        dishService.saveWithFlavor(dto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page(page,pageSize);

        Page<DishDto> pageDto = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);
        //获取list集合数据
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = null;
        list = records.stream().map((item)->{
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(item,dto);
            //获取分类id
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dto.setCategoryName(categoryName);
            return dto;
        }).collect(Collectors.toList());

        pageDto.setRecords(list);
        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        return R.success(pageDto);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dto = dishService.getByIdWithFlavor(id);
        return R.success(dto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dto){
        dishService.updateWithFlavor(dto);
        return R.success("修改成功");
    }
    @PostMapping("status/{flag}")
    public R<String> updateStatus(Long ids,@PathVariable int flag){
        dishService.updateStatus(ids,flag==1?true:false);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        Dish dish = new Dish();
        dish.setId(ids);
        Dish dish1 = dishService.getById(dish);
        if (dish1.getStatus() == 1){
            return R.error("该菜品处于起售状态，不能删除");
        }
        dishService.removeById(dish);
        return R.success("删除成功！");
    }


    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        wrapper.eq(Dish::getStatus, 1);

        //添加排序条件
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(wrapper);
        List<DishDto> dishDto = null;

        dishDto = list.stream().map((item)->{
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(item,dto);
            DishDto byIdWithFlavor = dishService.getByIdWithFlavor(item.getId());
            BeanUtils.copyProperties(byIdWithFlavor,dto);
            return dto;
        }).collect(Collectors.toList());

        return R.success(dishDto);
    }
}
