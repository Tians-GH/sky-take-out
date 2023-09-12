package com.sky.mapper;


import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper {
    /**
     * 新增地址
     *
     * @param addressBook
     */

    void insert(AddressBook addressBook);
}
