package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 产品
 */
@Controller
@RequestMapping(value ="/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    /**
     * 产品的保存或者修改 如果传入id就是修改 不传就是添加
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "productSave.json")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆");
        }
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            return  productService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    /**
     * 产品的上下架
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "setSaleStatus.json")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆");
        }
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            return  productService.setSaleStatus(productId,status);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    /**
     * 获取产品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "setSaleStatus.json")
    @ResponseBody
    public ServerResponse productDetail(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆");
        }
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            return  productService.manageProductDetail(productId);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    @RequestMapping(value = "list.json")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue ="1" ) int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆");
        }
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            return productService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    @RequestMapping(value = "productSerach.json")
    @ResponseBody
    public ServerResponse productSerach(HttpSession session, @RequestParam(value = "pageNum",defaultValue ="1" ) int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆");
        }
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            return productService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

}
