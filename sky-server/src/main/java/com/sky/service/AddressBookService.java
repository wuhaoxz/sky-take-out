package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    List<AddressBook> getAddressBookListByUserId();

    void save(AddressBook addressBook);
}
