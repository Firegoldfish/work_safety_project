package com.hjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjy.reggie.common.R;
import com.hjy.reggie.dto.DishDto;
import com.hjy.reggie.dto.SetmealDto;
import com.hjy.reggie.entity.Category;
import com.hjy.reggie.entity.Dish;
import com.hjy.reggie.entity.Setmeal;
import com.hjy.reggie.entity.SetmealDish;
import com.hjy.reggie.service.CategoryService;
import com.hjy.reggie.service.DishService;
import com.hjy.reggie.service.SetmealDishService;
import com.hjy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private CategoryService categoryService;

    //新增套餐
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }
    @PostMapping("/status/1")
    public R<String> updateStatusStart(Long ids){
        Setmeal setmeal = setmealService.getById(ids);
        setmeal.setStatus(1);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getId, ids);
        setmealService.update(setmeal, queryWrapper);
        return R.success("修改成功");
    }
    @PostMapping("/status/0")
    public R<String> updateStatusStop(Long ids){
        Setmeal setmeal = setmealService.getById(ids);
        setmeal.setStatus(0);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getId, ids);
        setmealService.update(setmeal, queryWrapper);
        return R.success("修改成功");
    }
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam List<Long> ids){
        log.info("将要删除的套餐id{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }
}
