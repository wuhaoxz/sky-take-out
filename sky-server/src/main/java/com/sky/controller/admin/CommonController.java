package com.sky.controller.admin;


import com.sky.exception.BaseException;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "公共接口")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        String name = file.getOriginalFilename();
        int index = name.lastIndexOf(".");
        String substring = name.substring(index);

        //文件名
        String objectName = UUID.randomUUID().toString() + substring;

        String url = null;
        try {
            url = aliOssUtil.upload(file.getBytes(), objectName);
        } catch (IOException e) {
            throw new BaseException("文件上传出现错误");
        }

        return Result.success(url);
    }

}
