package com.hjy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjy.reggie.eneity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
