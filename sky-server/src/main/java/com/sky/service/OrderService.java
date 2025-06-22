package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    void payment(OrdersPaymentDTO ordersPaymentDTO);

    PageResult page(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getOrderById(Long id);

    void repetition(Long id);

    void cancel(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void confirm(Long id);

    void rejection(Orders orders);

    void delivery(Long id);

    void complete(Long id);

    void cancelByAdmin(Orders orders);

    void reminder(Long id);
}
