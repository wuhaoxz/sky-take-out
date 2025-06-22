package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController("userOrderController")
@Api("订单相关接口")
@Slf4j
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("提交订单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        OrderSubmitVO vo = orderService.submit(ordersSubmitDTO);
        return Result.success(vo);
    }


    @PutMapping("/payment")
    @ApiOperation("模拟支付")
    public Result payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO){
        orderService.payment(ordersPaymentDTO);
        return Result.success();
    }


    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO){

        PageResult result = orderService.page(ordersPageQueryDTO);

        return Result.success(result);
    }


    @GetMapping("/orderDetail/{id}")
    @ApiOperation("根据订单id查询订单详情")
    public Result<OrderVO> getOrderById(@PathVariable Long id){

        OrderVO orderVO = orderService.getOrderById(id);

        return Result.success(orderVO);

    }


    /**
     * 加入购物车即可
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetition(@PathVariable Long id){

        orderService.repetition(id);

        return Result.success();
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable Long id){

        orderService.cancel(id);

        return Result.success();
    }


    @GetMapping("/reminder/{id}")
    @ApiOperation("用户催单")
    public Result reminder(@PathVariable Long id){
        orderService.reminder(id);
        return Result.success();
    }


}
