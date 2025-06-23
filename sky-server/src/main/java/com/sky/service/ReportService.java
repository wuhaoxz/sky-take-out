package com.sky.service;

import com.sky.vo.*;

import java.time.LocalDate;

public interface ReportService {
    SalesTop10ReportVO getTop10(LocalDate begin,LocalDate end);

    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);
}
