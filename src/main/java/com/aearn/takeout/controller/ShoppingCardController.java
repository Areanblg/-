package com.aearn.takeout.controller;

import com.aearn.takeout.common.BaseContext;
import com.aearn.takeout.common.R;
import com.aearn.takeout.entity.ShoppingCart;
import com.aearn.takeout.service.ShoppingCardService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCardController {
    @Autowired
    private ShoppingCardService shoppingCardService;

    //添加购物车
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("shopingcart{}",shoppingCart);
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中存在
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId != null){
            //表示添加的是菜品
            wrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            //表示添加的是套餐
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCardService.getOne(wrapper);
        if (one != null){
            //如果已经存在，就在原来的数量基础上加一
            Integer number = one.getNumber();
            one.setNumber(number+1);
            shoppingCardService.updateById(one);
        }else{
            //如果不存在，则添加到购物车，数量默认就是1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCardService.save(shoppingCart);
            one = shoppingCart;
        }

        return R.success(one);
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车..");
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        wrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCardService.list(wrapper);
        return R.success(list);
    }
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        //查询数据库中相应的数据信息
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        if (shoppingCart.getDishId() != null){
            //如果是菜品，就查询菜品数据
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else{
            //否则就是套餐
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCardService.getOne(wrapper);
        //
        if (one.getNumber() == 1){
            //如果数量等于1，直接删除数据
            shoppingCardService.removeById(one);
        }else{
            //否则number-1
            one.setNumber(one.getNumber()-1);
            shoppingCardService.updateById(one);
        }
        return R.success(one);
    }

    @DeleteMapping("clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCardService.remove(wrapper);
        return R.success("成功");
    }
}
