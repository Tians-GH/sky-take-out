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

/**
 * C端地址管理
 */
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "C端地址管理")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;

    /**
     * 新增地址
     *
     * @param addressBook
     * @return
     */
    @ApiOperation("新增地址")
    @PostMapping
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 新增地址
     *
     * @param
     * @return
     */
    @ApiOperation("查找地址")
    @GetMapping("/list")
    public Result<List<AddressBook>> selectList() {

        List<AddressBook> list = addressBookService.selectList();
        return Result.success(list);
    }
}
