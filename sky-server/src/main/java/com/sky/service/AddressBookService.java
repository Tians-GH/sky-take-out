package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 新增地址
     *
     * @param addressBook
     */
    void save(AddressBook addressBook);

    /**
     * 查找地址
     *
     * @return
     */
    List<AddressBook> selectList();

    /**
     * 查找默认地址
     *
     * @return
     */
    AddressBook selectDefaultAddress();

    /**
     * 修改默认地址
     *
     * @param addressBook
     */
    void updateAddr(AddressBook addressBook);

    /**
     * 根据id删除地址
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    AddressBook selectById(Long id);

    /**
     * 修改默认地址
     *
     * @param addressBook
     */
    void setDefaultAdd(AddressBook addressBook);
}
