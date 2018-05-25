package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.ShardedRedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户 Controller
 */
@RestController
@RequestMapping(value = "/user/session/")
public class UserSpringSessionController {

    @Autowired
    private IUserService userService;

    /**
     * 登陆
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.json",method = RequestMethod.GET)
    public ServerResponse<User> login(String username, String password,HttpSession session,HttpServletResponse response){
        ServerResponse<User> serverResponse = userService.Login(username, password);
        if(serverResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
            //CookieUtil.writeLoginToken(response,session.getId());
            //ShardedRedisPoolUtil.setEx(session.getId(),Const.RedisCacheExtime.REDIS_SESSION_EXPIRE, JsonUtil.obj2String(serverResponse.getData()));
        }
        return serverResponse;
    }
    @RequestMapping(value = "logout.json",method = RequestMethod.GET)
    public ServerResponse<String> logout(HttpSession session,HttpServletRequest request,HttpServletResponse response){
        session.removeAttribute(Const.CURRENT_USER);
        //String loginToken = CookieUtil.readLoginToken(request);
        //CookieUtil.delLoginToken(request,response);
        //ShardedRedisPoolUtil.del(loginToken);
        return ServerResponse.createBySuccess();
    }
    @RequestMapping(value = "getInformation.json",method = RequestMethod.GET)
    public ServerResponse<User> getInformation(HttpSession session,HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //String loginToken = CookieUtil.readLoginToken(request);
        //User user = JsonUtil.string2Obj(ShardedRedisPoolUtil.get(loginToken), User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登陆status=10");
        }
        return userService.getInformation(user.getId());
    }
}
