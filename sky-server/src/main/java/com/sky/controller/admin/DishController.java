package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {


    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation("根据分类id查询菜品")
    @GetMapping("list")
    public Result<List<Dish>> getByCategoryId(Long categoryId){

        List<Dish> list =  dishService.getByCategoryId(categoryId);

        return Result.success(list);
    }



    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){

        //新增的时候，因为只会改变一个分类下的菜品信息
        // 所以只需要删一个分类下的菜品信息即可
        redisTemplate.delete("dish_"+dishDTO.getCategoryId());


        dishService.save(dishDTO);
        return Result.success();
    }


    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dto){
        PageResult pageResult = dishService.page(dto);
        return Result.success(pageResult);
    }



    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result deleteBatch(@RequestParam("ids") List<Long> ids){

        //因为可能存在批量删除，所以需要删除所有的分类
        //获得所有以dish_开头的key,然后全部删除
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);


        dishService.deleteBatch(ids);

        return Result.success();
    }

    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){

        DishVO dishVO = dishService.getById(id);

        return  Result.success(dishVO);
    }



    @ApiOperation("修改菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){

        //因为可能改动两个分类（由一个分类改成另一个分类），所以全部删
        //获得所有以dish_开头的key,然后全部删除
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);


        dishService.update(dishDTO);
        return Result.success();
    }


    @ApiOperation("起售停售菜品")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){


        //因为难以获得分类id，所以删除所有的分类
        //获得所有以dish_开头的key,然后全部删除
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        dishService.startOrStop(status,id);

        return Result.success();
    }







}
