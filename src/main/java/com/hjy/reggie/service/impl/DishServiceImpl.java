package com.hjy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjy.reggie.entity.Dish;
import com.hjy.reggie.entity.DishFlavor;
import com.hjy.reggie.mapper.DishMapper;
import com.hjy.reggie.service.DishFlavorService;
import com.hjy.reggie.service.DishService;
import dto.DishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) { //多表
        //保存菜品基本信息到菜表
        this.save(dishDto);
        //保存口味表
        Long dishDtoId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
