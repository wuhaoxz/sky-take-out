package com.sky.service;

import com.sky.vo.SalesTop10ReportVO;

import java.time.LocalDate;

public interface ReportService {
    SalesTop10ReportVO getTop10(LocalDate begin,LocalDate end);
}
