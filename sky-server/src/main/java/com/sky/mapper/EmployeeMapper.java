package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmployeeMapper {


    /**
     * 新增员工
     * @param employee
     */
    @Insert("insert into employee(name, username, password, phone, sex, id_number, " +
            "create_time, update_time, create_user, update_user) values " +
            "(#{name}, #{username},#{password}, #{phone}, #{sex}, #{idNumber}," +
            "#{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void save(Employee employee);


    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);


    /**
     * 启用禁用员工
     * @param status
     * @param id
     */
    @Update("update employee set status = #{status} where id = #{id}")
    void startOrStop(@Param("status") Integer status, @Param("id") Long id);
}
