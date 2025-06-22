package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.SalesTop10ReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/report")
@Api(tags ="图形报表相关接口" )
public class ReportController {


    @Autowired
    private ReportService reportService;

    @GetMapping("top10")
    @ApiOperation("查询销量排名top10接口")
    public Result getTop10(){

        SalesTop10ReportVO top10vo = reportService.getTop10();

        return Result.success(top10vo);
    }



}
