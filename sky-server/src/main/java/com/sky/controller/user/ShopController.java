package com.sky.controller.user;


import com.sky.entity.Dish;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@Api(tags = "店铺相关接口")
@Slf4j
@RequestMapping("/user/shop")
public class ShopController {

    //存入redis的key
    private static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;



    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getShptStatus(){
        ValueOperations ops = redisTemplate.opsForValue();
        Integer status = (Integer) ops.get(KEY);
        return Result.success(status);
    }





}
