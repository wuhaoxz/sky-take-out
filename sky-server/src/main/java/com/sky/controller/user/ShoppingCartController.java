package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "购物车相关接口")
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {


    @Autowired
    private ShoppingCartService shoppingCartService;

    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result save(@RequestBody ShoppingCartDTO shoppingCartDTO){

        shoppingCartService.save(shoppingCartDTO);

        return Result.success();
    }


    @ApiOperation("查看购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){

        List<ShoppingCart> list = shoppingCartService.list();

        return Result.success(list);

    }

    @ApiOperation("删除购物车中的一个商品")
    @PostMapping("/sub")
    public Result deleteOne(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.deleteOne(shoppingCartDTO);
        return Result.success();
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result clean(){
        shoppingCartService.clean();
        return Result.success();
    }




}
