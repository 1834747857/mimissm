package com.bjpowernode.controller;

import com.bjpowernode.pojo.Admin;
import com.bjpowernode.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.applet.Main;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminAction {
    //在所有的界面层一定会有业务逻辑层的对象
    @Resource
    AdminService adminService;
    //实现登录判断。进行相应的跳转
    @RequestMapping("/login")
    public String login(String name , String pwd , HttpServletRequest request){
        Admin admin = adminService.login(name, pwd);
        if (admin != null){
            //登录成功
            request.setAttribute("admin",admin);
            return "main";
        }else {
            //登录失败
            request.setAttribute("errmsg","用户名或者密码不正确!!!!");
            return "login";
        }
    }
}
