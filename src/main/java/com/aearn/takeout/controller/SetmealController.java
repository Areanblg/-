package com.aearn.takeout.controller;

import com.aearn.takeout.common.R;
import com.aearn.takeout.dto.SetmealDto;
import com.aearn.takeout.entity.Category;
import com.aearn.takeout.entity.Dish;
import com.aearn.takeout.entity.Setmeal;
import com.aearn.takeout.entity.SetmealDish;
import com.aearn.takeout.service.CategoryService;
import com.aearn.takeout.service.DishService;
import com.aearn.takeout.service.SetmealDishService;
import com.aearn.takeout.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;



    @PostMapping
    @CacheEvict(value="setmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }
    /*
        套餐分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);

        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据like进行模糊查询
        wrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件
        wrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,wrapper);

        //进行对象的拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> DtoList = null;
        DtoList = records.stream().map((item) ->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //分类ID
            Long categoryId = item.getCategoryId();
            //根据分类ID查询分类名称
            Category category = categoryService.getById(categoryId);

            String categoryName = category.getName();

            setmealDto.setCategoryName(categoryName);

            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(DtoList);
        return R.success(dtoPage);
    }
    @DeleteMapping
    @CacheEvict(value="setmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    @PostMapping("/status/{statusNum}")
    public R<String> status(@PathVariable int statusNum,@RequestParam List<Long> ids){
        System.out.println(statusNum);
        System.out.println(ids);
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.in("id",ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(statusNum);
        setmealService.update(setmeal,wrapper);
        return R.success("修改成功");
    }

    /*
        根据条件查询套餐数据
     */
    @GetMapping("/list")
    @Cacheable(value="setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        wrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);
    }
}
