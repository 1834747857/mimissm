package com.bjpowernode.service;

import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductInfoService {
    //显示全部数据，不分页
    List<ProductInfo> getAll();

    //分页功能的实现
    PageInfo splitPage(int pageNO, int pageSize);

    //添加功能得实现
    int save(ProductInfo productInfo);

    //根据主键id查询商品
    ProductInfo selectById(int id);

    //跟新商品
    int update(ProductInfo productInfo);

    //单个商品删除得功能
    int delete(int pId);

    //批量删除商品
    int deleteBatch(String []ids);

    //多条件的查询
    List<ProductInfo> selectCondition(ProductInfoVo vo);
}
