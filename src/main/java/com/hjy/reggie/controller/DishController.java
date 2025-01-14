package com.hjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjy.reggie.common.R;
import com.hjy.reggie.entity.Category;
import com.hjy.reggie.entity.Dish;
import com.hjy.reggie.service.CategoryService;
import com.hjy.reggie.service.DishFlavorService;
import com.hjy.reggie.service.DishService;
import com.hjy.reggie.dto.DishDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("save dish : {}", dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name ) {
        //分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //查询
        dishService.page(pageInfo, queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(dishDtoPage, dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(item,dto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dto.setCategoryName(categoryName);
            return dto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }
    @PostMapping("/status/1")
    public R<String> updateStatusStart(Long ids){
        Dish dish = dishService.getById(ids);
        dish.setStatus(1);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getId, ids);
        dishService.update(dish, queryWrapper);
        return R.success("修改成功");
    }
    @PostMapping("/status/0")
    public R<String> updateStatusStop(Long ids){
        Dish dish = dishService.getById(ids);
        dish.setStatus(0);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getId, ids);
        dishService.update(dish, queryWrapper);
        return R.success("修改成功");
    }
    @DeleteMapping
    public R<String> deleteDish(Long ids) {
        Dish dish = dishService.getById(ids);
        dish.setIsDeleted(1);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getId, ids);
        dishService.update(dish, queryWrapper);
        return R.success("删除成功");
    }
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {  //根据条件查询套餐
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);    //起售的菜品
        //排序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }
}
