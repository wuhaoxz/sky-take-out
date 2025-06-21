package com.sky.mapper;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {
    void save(Orders order);


    void payment(OrdersPaymentDTO ordersPaymentDTO);
}
