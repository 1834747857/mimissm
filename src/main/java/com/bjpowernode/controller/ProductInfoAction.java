package com.bjpowernode.controller;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    //每页显示的记录数
    public static final int PAGE_SIZE = 5;
    String saveFileName="";
    @Resource
    ProductInfoService productInfoService;
    //显示所有的数据不分页
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list",list);
        return "product";
    }

    //显示第一页的五条记录
    @RequestMapping("/split")
    public String split(HttpServletRequest request){
        //得到第一页数据
        PageInfo info = productInfoService.splitPage(1, PAGE_SIZE);
        request.setAttribute("info",info);
        return "product";
    }

    //ajax分页翻页处理
    @ResponseBody
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(int page, HttpSession session){
        PageInfo info = productInfoService.splitPage(page,PAGE_SIZE);
        session.setAttribute("info",info);
    }

    //异步AJAX文件上传
    @ResponseBody
    @RequestMapping("ajaxImg")
    public Object ajaxImg(MultipartFile pimage,HttpServletRequest request){
        //提取并生成文件名UUID+上传图片得后缀.jpg
        saveFileName = FileNameUtil.getUUIDFileName()+FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片存储得路径
        String path = request.getServletContext().getRealPath("/image_big");
        //转存
        try {
            pimage.transferTo(new File(path+File.separator+saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回客户端得json对象，
        JSONObject object = new JSONObject();
        object.put("imgurl",saveFileName);
        return object.toString();
    }

    //添加商品得功能
    @RequestMapping("/save")
    public String save(ProductInfo productInfo,HttpServletRequest request){
        //前端页面封装了from表单，所以这里只需要保存图片得名称和商品创建得日期
        productInfo.setpImage(saveFileName);
        //保存创建得日期
        productInfo.setpDate(new Date());
        //调用service业务层保存数据（除了以上两个信息还有from表单提交上来得五个商品信息）
        int num = -1;
        try {
            num = productInfoService.save(productInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将创建得结果回显到客户端
        if (num > 0){
            request.setAttribute("msg","添加成功");
        }else {
            request.setAttribute("msg","添加失败");
        }
        saveFileName = "";
        return "forward:/prod/split.action";
    }

    //根据主键查询商品信息
    @RequestMapping("/one")
    public String one(int id,Model model){
        ProductInfo info = productInfoService.selectById(id);
        model.addAttribute("prod",info);
        return "update";
    }

    //更新商品
    @RequestMapping("/update")
    public String update(ProductInfo productInfo,HttpServletRequest request){
        //因为ajax得异步图片上传，如果有上传过 则saveFileName里有上传过得图片名称，
        // 如果没有使用ajax上传过图片，则saveFileName为空,实体类使用隐藏表单域注入给新的实体类
        if (!(saveFileName.equals(""))){
            productInfo.setpImage(saveFileName);
        }
        //完成更新
        int nums = -1;
        try {
            nums = productInfoService.update(productInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (nums > 0){
            request.setAttribute("msg","更新成功");
        }else {
            request.setAttribute("msg","更新失败");
        }
        //处理完毕之后需要将saveFileName置空
        saveFileName = "";
        //重定向回到分页页面
        return "forward:/prod/split.action";
    }

    //单个商品删除的界面开发
    @RequestMapping("/delete")
    public String delete(int pid,HttpServletRequest request){
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            request.setAttribute("msg","删除成功");
        }else {
            request.setAttribute("msg","删除失败");
        }
        //删除结束后跳转到分页显示
        return "forward:/prod/deleteAjaxSplit.action";
    }
    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit",produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request){
        //取得第一页得数据
        PageInfo info = productInfoService.splitPage(1,PAGE_SIZE);
        request.getSession().setAttribute("info",info);
        return request.getAttribute("msg");
    }

    //实现批量删除商品
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids,HttpServletRequest request){
        //将上穿过来得字符串劈开，形成商品id得字符数组
        String []ps = pids.split(",");
        int num = -1;
        try {
            num = productInfoService.deleteBatch(ps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            request.setAttribute("msg","批量删除成功");
        }else {
            request.setAttribute("msg","批量删除失败");
        }
        //删除结束后跳转到分页显示
        return "forward:/prod/deleteAjaxSplit.action";
    }

    //多条件查询的界面实现
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo,HttpSession session){
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list",list);
    }
}
