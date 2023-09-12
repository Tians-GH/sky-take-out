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
     * 查找地址
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

    /**
     * 查找默认地址
     *
     * @param
     * @return
     */
    @ApiOperation("查找默认地址")
    @GetMapping("/default")
    public Result<AddressBook> selectDefaultAddress() {

        AddressBook addressBook = addressBookService.selectDefaultAddress();
        return Result.success(addressBook);
    }

    /**
     * 修改默认地址
     *
     * @param
     * @return
     */
    @ApiOperation("修改默认地址")
    @PutMapping
    public Result updateAddr(@RequestBody AddressBook addressBook) {

        addressBookService.updateAddr(addressBook);
        return Result.success();
    }

    /**
     * 根据id删除地址
     *
     * @param
     * @return
     */
    @ApiOperation("根据id删除地址")
    @DeleteMapping
    public Result deleteById(@RequestParam Long id) {
        log.info("addr:{}", id);
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据id查询地址
     *
     * @param
     * @return
     */
    @ApiOperation("根据id查询地址")
    @GetMapping("/{id}")
    public Result<AddressBook> selectById(@PathVariable Long id) {
        log.info("id:{}", id);
        AddressBook addressBook = addressBookService.selectById(id);
        return Result.success(addressBook);
    }

    /**
     * 修改默认地址
     *
     * @param
     * @return
     */
    @ApiOperation("修改默认地址")
    @PutMapping("/default")
    public Result setDefaultAdd(@RequestBody AddressBook addressBook) {

        addressBookService.setDefaultAdd(addressBook);
        return Result.success();
    }
}
