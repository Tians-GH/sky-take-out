package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     *
     * @param addressBook
     */
    @Override
    public void save(AddressBook addressBook) {
        // 为谁存地址
        Long id = BaseContext.getCurrentId();
        addressBook.setUserId(id);
        // 默认不是默认地址
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    /**
     * 查找地址
     *
     * @return
     */
    @Override
    public List<AddressBook> selectList() {
        // 为谁查地址
        Long id = BaseContext.getCurrentId();
        List<AddressBook> list = addressBookMapper.selectList(id);
        return list;
    }

    /**
     * 查找默认地址
     *
     * @return
     */
    @Override
    public AddressBook selectDefaultAddress() {
        // 为谁查找地址
        Long id = BaseContext.getCurrentId();
        // 查找默认地址
        AddressBook addressBook = addressBookMapper.selectDefaultAddress(id);
        return addressBook;
    }

    /**
     * 修改默认地址
     *
     * @param addressBook
     */
    @Override
    public void updateAddr(AddressBook addressBook) {
        // Long id = BaseContext.getCurrentId();
        // addressBook.setUserId(id);
        // addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }

    /**
     * 根据id删除地址
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }

    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @Override
    public AddressBook selectById(Long id) {
        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook = addressBookMapper.selectById(id, userId);
        return addressBook;
    }

    /**
     * 修改默认地址
     *
     * @param addressBook
     */
    @Override
    public void setDefaultAdd(AddressBook addressBook) {
        // 只能有一个默认地址
        // 为指定id设置默认地址
        addressBookMapper.setDefaultAdd(addressBook);
        // 其他地址取消默认地址
        Long id = addressBook.getId();
        List<AddressBook> addressBooks = addressBookMapper.selectList(BaseContext.getCurrentId());
        for (AddressBook addressBook1 : addressBooks) {
            if (addressBook1.getId() != id) {
                addressBook1.setIsDefault(0);
                addressBookMapper.update(addressBook1);
            }
        }
        //
    }
}
