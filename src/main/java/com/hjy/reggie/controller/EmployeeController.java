package com.hjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hjy.reggie.common.R;
import com.hjy.reggie.eneity.Employee;
import com.hjy.reggie.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")  //员工登录
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //md5加密密码
        String password = employee.getPassword();
        password = DigestUtils.md5Hex(password);
        //查询用户数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //判断查到
        if(emp == null){
            return R.error("无此用户");
        }
        //密码比对
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //查看员工状态
        if(emp.getStatus() == 0){
            return R.error("账号已被禁用");
        }
        //成功登录
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }
}
