package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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

    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation("根据分类id查询菜品及口味")
    @GetMapping("/list")
    public Result<List<DishVO>> getByCategoryId(Long categoryId){

        //缓存优化
        //查询MySQL数据库前，判断redis中是否存在数据
        ValueOperations ops = redisTemplate.opsForValue();

        // 在使用默认的 JdkSerializationRedisSerializer 作为value的序列化器时，
        // 它会使用 Java 的原生序列化机制（ObjectOutputStream / ObjectInputStream）
        // 所以可以强转，只要你知道写入的是哪种类型
        List<DishVO> dishVOList = (List<DishVO>) ops.get("dish_" + categoryId);
        if(dishVOList!=null && dishVOList.size()>0){
            //如果有缓存数据，直接返回
            return Result.success(dishVOList);
        }

        List<DishVO> list = dishService.getByCategoryIdWithFlavor(categoryId);

        //将MySQL数据库查询到的数据存入Redis
        ops.set("dish_"+categoryId,list);

        return Result.success(list);
    }

}
