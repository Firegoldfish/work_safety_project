package com.hjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjy.reggie.common.R;
import com.hjy.reggie.entity.Category;
import com.hjy.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    //新增分类
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category={}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        Page<Category> pageInfo = new Page<>(page, pageSize); //分页构造器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>(); //条件构造器
        wrapper.orderByDesc(Category::getSort);
        categoryService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除id={}", ids);
        //categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }
}
