package com.sky.controller.admin;


import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("订单相关接口")
@Slf4j
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){

        PageResult result = orderService.conditionSearch(ordersPageQueryDTO);

        return Result.success(result);

    }


    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics(){

        OrderStatisticsVO orderStatisticsVO = orderService.statistics();

        return Result.success(orderStatisticsVO);

    }

    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody Orders orders){
        orderService.confirm(orders.getId());
        return Result.success();
    }


    @PutMapping("/cancel")
    @ApiOperation("管理端取消订单")
    public Result cancel(@RequestBody Orders orders){
        orderService.cancelByAdmin(orders);
        return Result.success();
    }


    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody Orders orders){
        orderService.rejection(orders);
        return Result.success();
    }


    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable Long id){
        orderService.delivery(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("派送订单")
    public Result complete(@PathVariable Long id){
        orderService.complete(id);
        return Result.success();
    }



    @GetMapping("/details/{id}")
    @ApiOperation("根据订单id查询订单详情")
    public Result<OrderVO> getOrderById(@PathVariable Long id){

        OrderVO orderVO = orderService.getOrderById(id);

        return Result.success(orderVO);

    }





}
