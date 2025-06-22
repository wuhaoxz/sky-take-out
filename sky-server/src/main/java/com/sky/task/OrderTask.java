package com.sky.task;

import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 自定义定时任务类
 */
@Service
@Slf4j
public class OrderTask {


    {
        log.info("创建OrderTask定时任务类对象...");
    }


    @Autowired
    private OrderMapper orderMapper;

    /**
     * 定时任务处理15分钟内未支付订单：每隔一分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void handlingOverdueOrders(){
        log.info("处理15分钟内未支付订单...");

        LocalDateTime time = LocalDateTime.now();
        time = time.minusMinutes(15);
        orderMapper.handlingOverdueOrders(time);

    }
}
