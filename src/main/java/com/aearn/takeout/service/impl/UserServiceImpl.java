package com.aearn.takeout.service.impl;

import com.aearn.takeout.entity.User;
import com.aearn.takeout.mapper.UserMapper;
import com.aearn.takeout.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
