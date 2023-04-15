package com.aearn.takeout.controller;

import com.aearn.takeout.common.BaseContext;
import com.aearn.takeout.common.R;
import com.aearn.takeout.entity.Employee;
import com.aearn.takeout.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //如果没有查询到则返回登录失败结果
        if (emp == null){
            return R.error("登录失败");
        }
        //密码对比，如果登录失败返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }

        //检查员工状态是否是正常可用
        if (emp.getStatus() == 0){
            return R.error("账号已禁用");
        }
        //登录成功,将信息存在session中返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @RequestMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清空登录成功时填入的session
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        //设置初始密码123456，需要进行MD5进行加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        Employee emp = (Employee)request.getSession().getAttribute("employee");
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /*
        员工信息分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null){
            queryWrapper.like(Employee::getName,name);
        }
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        Page pages = employeeService.page(pageInfo, queryWrapper);
        return R.success(pages);
    }
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){
        //根据id修改
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> select(@PathVariable Long id){
        //根据id查询数据库信息
        Employee emp = employeeService.getById(id);
        return R.success(emp);
    }
}
