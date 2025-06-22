package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import com.sky.report.DishCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailMapper {

    void saveBatch(List<OrderDetail> listOrderDetail);

    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);


    List<DishCount> getTop10(LocalDateTime begin, LocalDateTime end);
}
