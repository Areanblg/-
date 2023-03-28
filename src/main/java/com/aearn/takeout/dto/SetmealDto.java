package com.aearn.takeout.dto;

import com.aearn.takeout.entity.Setmeal;
import com.aearn.takeout.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
