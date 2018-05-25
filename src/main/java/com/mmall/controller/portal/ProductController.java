package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/product/")
public class ProductController {

    @Autowired
    private IProductService productService;

    @RequestMapping("detail.json")
    @ResponseBody
    public ServerResponse<ProductDetailVO> detail(Integer productId){
        return productService.getProductDetail(productId);
    }
    /**
      * @Author:蒋帅锋
      * @Description: 
      * @params:[keyword, categoryId, pageNum, pageSize, orderBy]
      * @return: com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo> 
      * @Date: 2018/5/24 14:14
      */ 
    @RequestMapping("list.json")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false) String keyword,
                                         @RequestParam(value = "categoryId",required = false) Integer categoryId,
                                         @RequestParam(value ="pageNum",defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return productService.getProductByKeywordCategotyId(keyword,categoryId,pageNum,pageSize,orderBy);
    }

}
