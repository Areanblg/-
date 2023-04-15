package com.aearn.takeout.controller;


import com.aearn.takeout.common.R;
import com.aearn.takeout.entity.Category;
import com.aearn.takeout.entity.Dish;
import com.aearn.takeout.entity.Employee;
import com.aearn.takeout.entity.Setmeal;
import com.aearn.takeout.service.CategoryService;
import com.aearn.takeout.service.DishService;
import com.aearn.takeout.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.catalina.startup.Catalina;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody Category category, HttpServletRequest request){
        boolean save = categoryService.save(category);
        if (save){
            return R.success("添加成功");
        }
        return R.error("添加失败");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize) {
        //配置分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //配置排序
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        System.out.println("----------------category----------------"+category);
        boolean ret = categoryService.updateById(category);
        if (ret){
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }


    /*
        根据条件查询分类数据
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        //条件构造器
        if (category.getType() != null){
            wrapper.eq(Category::getType,category.getType());
        }
        //添加排序条件
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }



}
