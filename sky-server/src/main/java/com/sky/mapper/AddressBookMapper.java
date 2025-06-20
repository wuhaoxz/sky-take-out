package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    @Select("select * from address_book where user_id = #{currentId}")
    List<AddressBook> getAddressBookListByUserId(Long currentId);


    void save(AddressBook addressBook);

    @Update("update address_book set is_default = 0 where user_id = #{userId}")
    void setNotDefaultByUserId(Long userId);

    @Update("update address_book set is_default = 1 where id = #{id}")
    void setDefaultById(Long id);

    @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook getDefaultByUserId(Long userId);

    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);


    void update(AddressBook addressBook);

    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);
}
