package com.aearn.takeout.service.impl;

import com.aearn.takeout.entity.Employee;
import com.aearn.takeout.mapper.EmployeeMapper;
import com.aearn.takeout.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
