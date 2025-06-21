package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {
    void save(Orders order);


    void payment(OrdersPaymentDTO ordersPaymentDTO);


    Page<Orders> page(Orders orders);

    @Select("select * from orders where id = #{id}")
    Orders getOrderById(Long id);

    @Update("update orders set status=#{status},pay_status=#{payStatus},cancel_time = #{cancelTime}," +
            "cancel_reason=#{cancelReason} where id = #{id}")
    void cancel(Orders order);
}
