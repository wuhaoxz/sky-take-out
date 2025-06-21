package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.aspectj.apache.bcel.generic.IINC;

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


    Page<Orders> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);


    @Select("select count(*) from orders where status=#{status}")
    Integer countByStatus(Integer status);


    @Update("update orders set status = 3 where id = #{id}")
    void confirm(Long id);
}
