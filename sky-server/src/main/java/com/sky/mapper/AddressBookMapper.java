package com.sky.mapper;


import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    /**
     * 新增地址
     *
     * @param addressBook
     */

    void insert(AddressBook addressBook);

    /**
     * 查找地址
     *
     * @param id
     * @return
     */
    @Select("select * from address_book where user_id = #{id}")
    List<AddressBook> selectList(Long id);

    /**
     * 查找默认地址
     *
     * @param id
     * @return
     */
    @Select("select * from address_book where user_id = #{id} and is_default = 1")
    AddressBook selectDefaultAddress(Long id);
}
