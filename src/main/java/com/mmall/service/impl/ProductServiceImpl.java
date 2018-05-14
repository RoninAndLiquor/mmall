package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVO;
import com.mmall.vo.ProductListVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product != null){
            if(StringUtils.isNotBlank(product.getSubImages())) {
                String[] images = product.getSubImages().split(",");
                if(images.length>0){
                    product.setMainImage(images[0]);
                }
            }
            if(product.getId() !=null){
                int count = productMapper.updateByPrimaryKeySelective(product);
                if(count>0){
                    return ServerResponse.createBySuccess("更新产品信息成功");
                }
                return ServerResponse.createByErrorMsg("更新产品信息失败");
            }else{
                int count = productMapper.insert(product);
                if(count>0){
                    return ServerResponse.createBySuccess("新增产品成功");
                }
                return ServerResponse.createByErrorMsg("新增产品失败");
            }
        }
        return  ServerResponse.createByErrorMsg("新增或者更新产品参数有误");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if(productId == null || status == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int count = productMapper.updateByPrimaryKeySelective(product);
        if(count>0){
            return ServerResponse.createBySuccess("修改产品形式状态成功");
        }
        return ServerResponse.createByErrorMsg("修改产品形式状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVO> manageProductDetail(Integer productId) {
        if(productId == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return  ServerResponse.createByErrorMsg("产品已下架或删除");
        }
        //VO对象 Value Object
        //pojo -> bo(business Object) -> vo(view Object)

        ProductDetailVO productDetailVO = assembleProduceDetailVO(product);
        return ServerResponse.createBySuccess(productDetailVO);
    }

    private ProductDetailVO assembleProduceDetailVO(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setName(product.getName());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubTitle(product.getSubtitle());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.jsf.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVO.setParentCategoryId(0);
        }else{
            productDetailVO.setParentCategoryId(category.getParentId());
        }
        productDetailVO.setCreateTime(DateTimeUtil.dateToString(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateTimeUtil.dateToString(product.getUpdateTime()));
        return productDetailVO;

    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        //1.startPage start
        PageHelper.startPage(pageNum,pageSize);
        //2.填充自己的SQL查询逻辑
        final List<Product> products = productMapper.selectList();
        List<ProductListVO> productListVOs = Lists.newArrayList();
        for(Product product:products){
            ProductListVO productListVO = assembleProductListVO(product);
            productListVOs.add(productListVO);
        }
        //3.pageHelper 的收尾
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVOs);
        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setName(product.getName());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.jsf.com/"));
        return productListVO;
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pateSize) {
        PageHelper.startPage(pageNum,pateSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> products = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVO> productListVOs = Lists.newArrayList();
        for(Product product:products){
            ProductListVO productListVO = assembleProductListVO(product);
            productListVOs.add(productListVO);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVOs);
        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<ProductDetailVO> getProductDetail(Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null) {
            return ServerResponse.createByErrorMsg("产品已下架或删除");
        }
        if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServerResponse.createByErrorMsg("产品已下架或删除");
        }
        ProductDetailVO productDetailVO = assembleProduceDetailVO(product);
        return ServerResponse.createBySuccess(productDetailVO);
    }
    public ServerResponse<PageInfo> getProductByKeywordCategotyId(String keyword,Integer categoryId,Integer pageNum,Integer pageSize,String orderBy){
        if(StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categotyIdList = Lists.newArrayList();
        if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)){
                PageHelper.startPage(pageNum,pageSize);
                List<ProductDetailVO> productDetailVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productDetailVOList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categotyIdList = categoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] split = orderBy.split("_");
                PageHelper.orderBy(split[0]+" "+split[1]);
            }
        }
        List<Product> products = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword, categotyIdList.size() == 0 ? null : categotyIdList);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        for(Product product:products){
            ProductListVO productListVO = assembleProductListVO(product);
            productListVOList.add(productListVO);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
