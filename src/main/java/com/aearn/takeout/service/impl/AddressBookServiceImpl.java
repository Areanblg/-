package com.aearn.takeout.service.impl;

import com.aearn.takeout.entity.AddressBook;
import com.aearn.takeout.entity.Category;
import com.aearn.takeout.mapper.AddressBookMapper;
import com.aearn.takeout.mapper.CategoryMapper;
import com.aearn.takeout.service.AddressBookService;
import com.aearn.takeout.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
