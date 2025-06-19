package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "菜品相关接口")
@RequestMapping("/user/dish")
@RestController("userDishController")
public class DishController {

    @Autowired
    private DishService dishService;


    @ApiOperation("根据分类id查询菜品信息")
    @GetMapping("/list")
    public Result<List<DishVO>> getByCategoryId(Long categoryId){

        List<DishVO> list = dishService.getByCategoryIdWithFlavor(categoryId);

        return Result.success(list);
    }

}
