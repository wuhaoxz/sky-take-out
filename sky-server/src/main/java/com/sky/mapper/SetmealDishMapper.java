package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealDishMapper {



    void saveBatch(List<SetmealDish> setmealDishes);




    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetmealId(Long id);


    void deleteBySetmealIds(List<Integer> ids);

    Integer countByDishIds(List<Long> ids);


    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetMealId(Long id);


    @Select("select dish.name name, dish.description description, dish.image iamge,setmeal_dish.copies  copies" +
            " from setmeal_dish join dish on setmeal_dish.dish_id = dish.id " +
            "where setmeal_dish.setmeal_id = #{setmealId}")
    List<DishItemVO> getBySetMealIdWithDishInfo(Long setmealId);
}
