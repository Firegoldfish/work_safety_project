package com.hjy.reggie.controller;

import com.hjy.reggie.common.R;
import com.hjy.reggie.service.DishFlavorService;
import com.hjy.reggie.service.DishService;
import com.hjy.reggie.dto.DishDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("save dish : {}", dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜成功");
    }
}
