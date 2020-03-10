package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.user.bean.UmsMember;
import com.atguigu.gmall.user.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.user.mapper.UserMapper;
import com.atguigu.gmall.user.mapper.UserMemberReceiveAddressMapper;
import com.atguigu.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserMemberReceiveAddressMapper addressMapper;

    @Override
    public List<UmsMember> getAllUser() {
//        List<UmsMember> umsMemberList = userMapper.selectAllUser();
//        return umsMemberList;
        return userMapper.selectAll();
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String id) {
//        UmsMemberReceiveAddress address = new UmsMemberReceiveAddress();
//        address.setMemberId(id);
//        return addressMapper.select(address);
        Example example = new Example(UmsMemberReceiveAddress.class);
        example.createCriteria().andEqualTo("memberId", id);
        return addressMapper.selectByExample(example);
    }
}
