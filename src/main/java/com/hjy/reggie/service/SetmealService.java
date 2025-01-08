package com.hjy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjy.reggie.dto.SetmealDto;
import com.hjy.reggie.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public void removeWithDish(List<Long> ids);
}
