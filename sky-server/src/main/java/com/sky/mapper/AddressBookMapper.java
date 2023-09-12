package com.sky.mapper;


import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    /**
     * 修改默认地址
     *
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 根据id删除地址
     *
     * @param id
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据id查询地址
     *
     * @param id
     * @param userId
     * @return
     */
    @Select("select * from address_book where user_id = #{userId} and id = #{id}")
    AddressBook selectById(Long id, Long userId);

    /**
     * 修改默认地址
     *
     * @param addressBook
     */
    @Update("update address_book set is_default = 1 where id = #{id}")
    void setDefaultAdd(AddressBook addressBook);
}
