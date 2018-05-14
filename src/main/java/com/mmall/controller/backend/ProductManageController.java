package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private IFileService fileService;

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
    @RequestMapping(value = "productDetail.json")
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

    @RequestMapping(value = "productSearch.json")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName,Integer productId,@RequestParam(value = "pageNum",defaultValue ="1" ) int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆");
        }
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            return productService.searchProduct(productName,productId,pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }
    @RequestMapping(value = "upload.json")
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value="uploadFile",required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆");
        }
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String dir = "img";
            String targetFileName = fileService.upload(file,path,dir);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map<String,Object> fileMap = new HashMap<String,Object>();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    @RequestMapping(value = "richtextImgUpload.json")
    @ResponseBody
    public Map<String,Object> richtextImgUpload(HttpSession session, @RequestParam(value="uploadFile",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> result = new  HashMap<String,Object>();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            result.put("success",false);
            result.put("msg","用户未登陆，请登陆");
            return result;
        }
        ServerResponse serverResponse = userService.checkAdinRole(user);
        if(serverResponse.isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String dir = "img";
            String targetFileName = fileService.upload(file,path,dir);
            if(StringUtils.isBlank(targetFileName)){
                result.put("success",false);
                result.put("msg","上傳失敗");
                return result;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            result.put("success",false);
            result.put("msg","上傳成功");
            result.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return result;
        }else{
            result.put("success",false);
            result.put("msg","沒有權限操作");
            return result;
        }
    }
}