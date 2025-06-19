package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "套餐相关接口")
@RequestMapping("/user/setmeal")
@RestController("userSetmealController")
public class SetmealController {



    @Autowired
    private SetmealService setmealService;



    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    @Cacheable(cacheNames = "setmeal",key = "#categoryId")
    public Result<List<Setmeal>> getByCategoryId(Long categoryId){

        List<Setmeal> list = setmealService.getByCategoryId(categoryId);

        return Result.success(list);
    }


    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询菜品")
    public Result<List<DishItemVO>> getBySetmealId(@PathVariable Long id){

        List<DishItemVO> list = setmealService.getBySetMealIdWithDishInfo(id);

        return Result.success(list);

    }



}
