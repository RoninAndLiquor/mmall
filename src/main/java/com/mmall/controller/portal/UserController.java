package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户 Controller
 */
@Controller
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 登陆
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse response, HttpServletRequest request){
        ServerResponse<User> serverResponse = userService.Login(username, password);
        if(serverResponse.isSuccess()){
            //session.setAttribute(Const.CURRENT_USER,response.getData());
            CookieUtil.writeLoginToken(response,session.getId());
            CookieUtil.readLoginToken(request);
            RedisPoolUtil.setEx(session.getId(),Const.RedisCacheExtime.REDIS_SESSION_EXPIRE, JsonUtil.obj2String(serverResponse.getData()));
        }
        return serverResponse;
    }
    @RequestMapping(value = "logout.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        //session.removeAttribute(Const.CURRENT_USER);
        RedisPoolUtil.del(session.getId());
        return ServerResponse.createBySuccess();
    }
    @RequestMapping(value = "register.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        ServerResponse<String> register = userService.register(user);
        return register;
    }
    @RequestMapping(value = "checkValid.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return userService.checkValid(str,type);
    }
    @RequestMapping(value = "getUserInfo.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(session.getId()), User.class);
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createBySuccessMsg("用户未登录，尚无法获取用户信息");
    }
    @RequestMapping(value = "forgetGetQuestion.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return userService.selectQuestion(username);
    }
    @RequestMapping(value = "forgetCheckAnswer.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return userService.checkAnswer(username,question,answer);
    }
    @RequestMapping(value = "forgetResetPassword.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String password,String forgetToken){
        return userService.forgetResetPassword(username,password,forgetToken);
    }
    @RequestMapping(value = "resetPassword.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(session.getId()), User.class);
        if(user == null){
            return ServerResponse.createBySuccessMsg("登陆信息过期，请重新登陆");
        }
        return userService.resetPassword(passwordOld,passwordNew,user);
    }
    @RequestMapping(value = "updateInformation.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session,User user){
        //User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        User currentUser = JsonUtil.string2Obj(RedisPoolUtil.get(session.getId()), User.class);
        if(currentUser == null){
            return ServerResponse.createBySuccessMsg("登陆信息过期，请重新登陆");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = userService.updateInformation(user);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    @RequestMapping(value = "getInformation.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpServletRequest request,HttpSession session){
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        User user = JsonUtil.string2Obj(RedisPoolUtil.get(loginToken), User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登陆status=10");
        }
        return userService.getInformation(user.getId());
    }
}
