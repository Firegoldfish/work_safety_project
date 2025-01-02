package com.hjy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjy.reggie.entity.Dish;
import dto.DishDto;
import org.springframework.stereotype.Service;

@Service
public interface DishService extends IService<Dish> {
    //新增菜品，口味，对两张表操作
    public void saveWithFlavor(DishDto dishDto);
}