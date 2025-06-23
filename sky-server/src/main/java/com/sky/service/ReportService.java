package com.sky.service;

import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    SalesTop10ReportVO getTop10(LocalDate begin,LocalDate end);

    UserReportVO userStatistics(LocalDate begin, LocalDate end);
}
