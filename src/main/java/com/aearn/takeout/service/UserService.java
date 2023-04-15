package com.aearn.takeout.service;

import com.aearn.takeout.common.R;
import com.aearn.takeout.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {

    R<String> login(String phone, String code);
}
