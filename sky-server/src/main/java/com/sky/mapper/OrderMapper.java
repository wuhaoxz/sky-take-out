package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.aspectj.apache.bcel.generic.IINC;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @Update("update orders set status = 6,rejection_reason =#{rejectionReason}," +
            "cancel_reason=concat('商家取消：',#{rejectionReason}),cancel_time= now() where id = #{id}")
    void rejection(Orders orders);

    @Update("update orders set status = 4 where id = #{id}")
    void delivery(Long id);

    @Update("update orders set status = 5,delivery_time=now() where id = #{id}")
    void complete(Long id);

    @Update("update orders set status = 6,cancel_time=now()," +
            "cancel_reason='超时未付款，自动取消'" +
            " where status = 1 and order_time<#{time}")
    void handlingOverdueOrders(LocalDateTime time);

    @Select("select * from orders where number= #{orderNumber}")
    Orders getOrderByNumber(String orderNumber);

    BigDecimal getAmountWithBeginAndEnd(LocalDateTime begin, LocalDateTime end);

    Integer getTotalOrderNumWithBeginAndEnd(LocalDateTime begin, LocalDateTime end);

    Integer getValidOrderNumWithBeginAndEnd(LocalDateTime begin, LocalDateTime end);

    @Select("select count(*) from orders where order_time >= #{begin} and order_time <= #{end}")
    Integer getTotalOrderCount(LocalDateTime begin, LocalDateTime end);

    @Select("select count(*) from orders where status = 5 and order_time >= #{begin} and order_time <= #{end}")
    Integer getValidOrderCount(LocalDateTime begin, LocalDateTime end);
}
