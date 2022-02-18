package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.AdminMapper;
import com.bjpowernode.pojo.Admin;
import com.bjpowernode.pojo.AdminExample;
import com.bjpowernode.service.AdminService;
import com.bjpowernode.utils.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    //在业务逻辑层中一定会有数据访问层对象
    @Resource
    AdminMapper adminMapper;
    @Override
    public Admin login(String name, String pwd) {
        //根据传入的用户名到数据库中查找相应的对象
        AdminExample adminExample = new AdminExample();
        adminExample.createCriteria().andANameEqualTo(name);
        List<Admin> list = adminMapper.selectByExample(adminExample);
        if (list.size() > 0){
            Admin admin = list.get(0);
            String miPwd = MD5Util.getMD5(pwd);
            //如果查询到用户对象，在进行密码比对
            if (miPwd.equals(admin.getaPass())){
                return admin;
            }
        }
        return null;
    }
}
