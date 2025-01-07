package com.hjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hjy.reggie.common.R;
import com.hjy.reggie.dto.SetmealDto;
import com.hjy.reggie.entity.Dish;
import com.hjy.reggie.entity.Setmeal;
import com.hjy.reggie.entity.SetmealDish;
import com.hjy.reggie.service.DishService;
import com.hjy.reggie.service.SetmealDishService;
import com.hjy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private DishService dishService;

    @PutMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info(setmealDto.toString());
        // SetmealDishService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {  //根据条件查询套餐
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //排序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = DishService.list(queryWrapper);
        dishService.list = list
        return R.success(list);
    }
}
