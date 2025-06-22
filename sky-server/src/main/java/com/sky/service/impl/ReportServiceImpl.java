package com.sky.service.impl;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.report.DishCount;
import com.sky.service.DishService;
import com.sky.service.ReportService;
import com.sky.vo.SalesTop10ReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public SalesTop10ReportVO getTop10() {

        SalesTop10ReportVO top10vo = new SalesTop10ReportVO();
        //得到销量最高的10个菜品的dish_id和每个菜品的销量
        List<DishCount> dishCountlist = orderDetailMapper.getTop10();

        //List<GoodsSalesDTO>：菜品的名字和每个菜品的销量
        List<GoodsSalesDTO> goodsSalesDTOlist = new ArrayList<>();
        for (DishCount dishCount : dishCountlist) {
            Long dishId = dishCount.getDishId();
            Dish dish = dishMapper.getById(dishId);

            GoodsSalesDTO goodsSalesDTO = new GoodsSalesDTO();

            goodsSalesDTO.setName(dish.getName());
            goodsSalesDTO.setNumber(dishCount.getDishCount());

            goodsSalesDTOlist.add(goodsSalesDTO);
        }

        List<String> nameList = goodsSalesDTOlist.stream().map(new Function<GoodsSalesDTO, String>() {
            @Override
            public String apply(GoodsSalesDTO goodsSalesDTO) {
                return goodsSalesDTO.getName();
            }
        }).collect(Collectors.toList());


        List<String> countList = goodsSalesDTOlist.stream().map(new Function<GoodsSalesDTO, String>() {
            @Override
            public String apply(GoodsSalesDTO goodsSalesDTO) {
                return goodsSalesDTO.getNumber()+"";
            }
        }).collect(Collectors.toList());


        String nameStr = String.join(",", nameList);
        String countStr = String.join(",", countList);

        top10vo.setNameList(nameStr);
        top10vo.setNumberList(countStr);

        return top10vo;
    }
}
