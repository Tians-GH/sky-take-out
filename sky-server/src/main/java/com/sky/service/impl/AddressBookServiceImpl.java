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
}
