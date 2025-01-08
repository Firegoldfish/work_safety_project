package com.hjy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjy.reggie.common.CustomException;
import com.hjy.reggie.common.R;
import com.hjy.reggie.dto.SetmealDto;
import com.hjy.reggie.entity.Dish;
import com.hjy.reggie.entity.Setmeal;
import com.hjy.reggie.entity.SetmealDish;
import com.hjy.reggie.mapper.SetmealMapper;
import com.hjy.reggie.service.SetmealDishService;
import com.hjy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;

    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }
    @Transactional
    public void removeWithDish(List<Long> ids){
        LambdaQueryWrapper<Setmeal> wrapper = Wrappers.lambdaQuery();
        wrapper.in(Setmeal::getId,ids);
        wrapper.eq(Setmeal::getStatus,1);
        int count = this.count(wrapper);
        if(count>0){
            throw new CustomException("套餐正在售卖，无法删除");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> wrapperDish = Wrappers.lambdaQuery();
        wrapperDish.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(wrapperDish);
    }
}
