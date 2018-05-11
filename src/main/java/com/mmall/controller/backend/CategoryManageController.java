package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/manage/category/")
public class CategoryManageController {


    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping(value = "addCategory.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请重新登陆");
        }
        //校验一下是否是管理员
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            return categoryService.addCategory(categoryName,parentId);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员");
        }
    }

    @RequestMapping(value = "setCategoryName.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请重新登陆");
        }
        //校验一下是否是管理员
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            return categoryService.updateCategoryName(categoryId,categoryName);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员");
        }
    }
    @RequestMapping(value = "getChildrenParallelCategory.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请重新登陆");
        }
        //校验一下是否是管理员
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            //查询当前节点的categoty信息  不递归 只查询平级
            return categoryService.getChildrenParallelCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员");
        }
    }
    @RequestMapping(value = "getCategoryAndDeepChildrenCategory.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请重新登陆");
        }
        //校验一下是否是管理员
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            //查询子节点的categoty信息  递归
            return categoryService.selectCategoryAndChildrenById(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("不是管理员");
        }
    }

}
