package com.hjy.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjy.reggie.common.R;
import com.hjy.reggie.eneity.Employee;
import com.hjy.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")  //员工登录
    public R<Employee> login(javax.servlet.http.HttpServletRequest request, @RequestBody Employee employee) {
        //md5加密密码
        String password = employee.getPassword();
        password = org.springframework.util.DigestUtils.md5DigestAsHex(password.getBytes());
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
    //员工退出
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理当前登录员工session
        request.getSession().removeAttribute("employee");
        return R.success("已退出");
    }
    //新增员工
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee) {
//        log.info("新增员工，员工信息{}", employee.toString());
//        //设置初始密码123456，加密存储
        employee.setPassword(org.springframework.util.DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId=(Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功！！！");
    }

    //员工信息分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page={}, pageSize={}, name={}",page,pageSize,name);
        //分页构造器
        Page pageInfo = new Page(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }
    //根据id修改status
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee) {
        log.info(employee.toString());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }
    //根据id查员工信息
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工={}", id);
        Employee employee= employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有根据id查到员工信息");
    }
}
