package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.UserMapper;
import com.sky.report.DishCount;
import com.sky.service.ReportService;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.UserReportVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private DishMapper dishMapper;


    @Autowired
    private UserMapper userMapper;

    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {

        SalesTop10ReportVO top10vo = new SalesTop10ReportVO();
        //得到销量最高的10个菜品的dish_id和每个菜品的销量
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);// xxxx-xx-xx 00:00
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);//xxxx-xx-xx 23:59:59.999999999

        List<DishCount> dishCountlist = orderDetailMapper.getTop10(beginTime,endTime);

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

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {

        //1.获得从开始时间到结束时间的时间列表
        ArrayList<LocalDateTime> timeList = new ArrayList<>();
        int plusNnum = 0;
        while(begin.plusDays(plusNnum).isBefore(end)){
            LocalDateTime temp = LocalDateTime.of(begin.plusDays(plusNnum++), LocalTime.MIN);//.plusDays方法会返回新的对象
            timeList.add(temp);
        }
        timeList.add(LocalDateTime.of(end, LocalTime.MIN));//手动补上end
        // [2025-06-15T00:00, 2025-06-16T00:00, 2025-06-17T00:00, 2025-06-18T00:00, 2025-06-19T00:00, 2025-06-20T00:00, 2025-06-21T00:00]




        //2.查询数据库，计算每一天的总用户数量
        ArrayList<Integer> totalNumList = new ArrayList<>();
        for (int i = 0; i < timeList.size()-1; i++) {
            Integer total = userMapper.queryTotalUserNum(timeList.get(i+1));
            totalNumList.add(total);
        }
        //2025-06-21T00:00~2025-06-22T00:00
        Integer total = userMapper.queryTotalUserNum(timeList.get(timeList.size()-1).plusDays(1));
        totalNumList.add(total);//手动补上最后一天的总人数    [1,2,4,6,7,8,9]

        //3.计算得到一个时间段内的新增用户数量
        ArrayList<Integer> increaseList = new ArrayList<>();
        for (int i = 0; i < totalNumList.size()-1; i++) {
            increaseList.add(totalNumList.get(i+1)-totalNumList.get(i));
        }
        //2025-06-14T00:00~2025-06-15T00:00
        Integer beforeBefore = userMapper.queryTotalUserNum(timeList.get(0));
        increaseList.add(0,totalNumList.get(0)-beforeBefore);//手动补上第一天的增长量


        //4.封装返回数据
        UserReportVO userReportVO = new UserReportVO();

        List<String> collect1  = timeList.stream().map(new Function<LocalDateTime, String>() {
            @Override
            public String apply(LocalDateTime localDateTime) {

                return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }).collect(Collectors.toList());

        String join1 = String.join(",", collect1);
        userReportVO.setDateList(join1);


        List<String> collect2 = totalNumList.stream().map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return ""+integer;
            }
        }).collect(Collectors.toList());
        String join2 = String.join(",", collect2);
        userReportVO.setTotalUserList(join2);

        List<String> collect3 = increaseList.stream().map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return ""+integer;
            }
        }).collect(Collectors.toList());
        String join3 = String.join(",", collect3);
        userReportVO.setNewUserList(join3);


        return userReportVO;
    }
}
