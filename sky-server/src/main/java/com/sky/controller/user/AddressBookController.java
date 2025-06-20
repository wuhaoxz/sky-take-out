package com.sky.controller.user;


import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user/addressBook")
@RestController
@Slf4j
@Api(tags = "地址相关接口")
public class AddressBookController {


    @Autowired
    private AddressBookService addressBookService;


    @ApiOperation("查询当前登录用户的所有地址")
    @GetMapping("/list")
    public Result<List<AddressBook>> getAddressBookListByUserId(){

        List<AddressBook> list = addressBookService.getAddressBookListByUserId();

        return Result.success(list);

    }

    @ApiOperation("新增地址")
    @PostMapping
    public Result save(@RequestBody AddressBook addressBook){

        addressBookService.save(addressBook);

        return Result.success();
    }


    @ApiOperation("设置默认地址")
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook addressBook){

        addressBookService.setDefault(addressBook);
        return Result.success();
    }



}
