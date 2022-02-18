package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductInfoExample;
import com.bjpowernode.pojo.vo.ProductInfoVo;
import com.bjpowernode.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
@Service
public class ProductInfoServiceImpl  implements ProductInfoService {
    @Resource
    ProductInfoMapper productInfoMapper;

    /**
     * 查询全部商品信息，不分页
     * @return
     */
    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    /**
     * 分页显示所有商品信息
     * select * from product_info limit 起始记录数=（当前页-1）*每页显示的数据，每页显示几行数据
     * @param pageNO：从第几页开始显示
     * @param pageSize：每一页显示的数据
     * @return
     */
    @Override
    public PageInfo splitPage(int pageNO, int pageSize) {
        //先将数据进行分页设置
        PageHelper.startPage(pageNO,pageSize);
        //将分页好的数据封装到PageInfo中
        //进行有条件的查询时，必须要创建ProductInfoExample的对象
        ProductInfoExample example = new ProductInfoExample();
        //设置排序（降序）
        example.setOrderByClause("p_id desc");
        //设置完排序后，取集合
        List<ProductInfo> list = productInfoMapper.selectByExample(example);
        //将查询到的集合封装进PageInfo对象中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo productInfo) {
        return productInfoMapper.insert(productInfo);
    }

    //根据商品id查询商品信息

    @Override
    public ProductInfo selectById(int id) {
        return productInfoMapper.selectByPrimaryKey(id);
    }

    //跟新商品
    @Override
    public int update(ProductInfo productInfo) {
        return productInfoMapper.updateByPrimaryKey(productInfo);
    }

    //单个商品删除得功能

    @Override
    public int delete(int pId) {
        return productInfoMapper.deleteByPrimaryKey(pId);
    }

    //批量删除商品
    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

    //多条件查询

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }
}
