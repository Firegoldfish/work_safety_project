package com.hjy.reggie.dto;


import com.hjy.reggie.entity.Setmeal;
import com.hjy.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
