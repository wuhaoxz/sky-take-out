package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public List<AddressBook> getAddressBookListByUserId() {

        List<AddressBook> list = addressBookMapper.getAddressBookListByUserId(BaseContext.getCurrentId());

        return list;
    }

    @Override
    public void save(AddressBook addressBook) {

        Long userId = BaseContext.getCurrentId();
        addressBook.setId(null);
        addressBook.setUserId(userId);

        //查询该用户是否有地址
        List<AddressBook> list = addressBookMapper.getAddressBookListByUserId(userId);
        //如果该用户之前没有地址，这是第一个，那么就设置isDefault=1
        //否则，设置isDefault=0
        if(list==null || list.size() == 0){
            addressBook.setIsDefault(1);
        }else{
            addressBook.setIsDefault(0);
        }



        addressBookMapper.save(addressBook);



    }

    @Override
    public void setDefault(AddressBook addressBook) {
        Long id = addressBook.getId();
        Long userId = BaseContext.getCurrentId();

        //将该用户所有的地址信息的isDefault设置为0
        addressBookMapper.setNotDefaultByUserId(userId);

        //将当前id的isDefault设置为1
        addressBookMapper.setDefaultById(id);



    }

    @Override
    public AddressBook getDefaultByUserId() {
        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook = addressBookMapper.getDefaultByUserId(userId);
        return addressBook;
    }

    @Override
    public AddressBook getById(Long id) {
        AddressBook addressBook = addressBookMapper.getById(id);
        return addressBook;
    }
}
