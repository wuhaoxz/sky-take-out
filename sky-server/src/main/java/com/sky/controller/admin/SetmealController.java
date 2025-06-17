package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;

import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "套餐相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {


    @Autowired
    private SetmealService setmealService;



    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){

        PageResult pageResult = setmealService.page(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    @ApiOperation("新增套餐")
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO){


        setmealService.save(setmealDTO);


        return Result.success();
    }




    @ApiOperation("根据id查询套餐")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }


    @ApiOperation("套餐起售停售")
    @PostMapping("status/{status}")
    public Result startOrStop(@PathVariable Integer status,Long id){

            setmealService.startOrStop(status,id);

        return Result.success();
    }






}
