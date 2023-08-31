package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 根据实体类插入员工表
     * @param employee
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user,status) values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime}," +
            "#{updateTime},#{createUser},#{updateUser},#{status})")
    void save(Employee employee);

    /**
     * 根据name动态查询员工分页数据
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> selectByPage(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据参数更新用户
     * @param employee
     */
    void updata(Employee employee);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
    Employee selectById(Integer id);
}
