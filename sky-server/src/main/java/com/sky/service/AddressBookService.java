package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    List<AddressBook> getAddressBookListByUserId();

    void save(AddressBook addressBook);

    void setDefault(AddressBook addressBook);



    AddressBook getDefaultByUserId();

    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    void deleteById(Long id);
}
