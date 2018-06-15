package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService orderService;
    /**
      * @Author:蒋帅锋
      * @Description: 
      * @params:[session, shippingId] 
      * @return: com.mmall.common.ServerResponse 
      * @Date: 2018/5/24 10:51 
      */ 
    @RequestMapping(value = "create.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse create(HttpSession session,Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        return orderService.createOrder(user.getId(),shippingId);
    }

    /**
      * @Author:蒋帅锋
      * @Description: 
      * @params:[session, orderNo, request] 
      * @return: com.mmall.common.ServerResponse 
      * @Date: 2018/5/24 10:51 
      */ 
    @RequestMapping(value = "pay.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String realPath = request.getSession().getServletContext().getRealPath("upload");
        return orderService.pay(user.getId(),realPath,orderNo);
    }
    @RequestMapping(value = "alipayCallback.json",method = RequestMethod.POST)
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String,String> params = Maps.newHashMap();
        for(Map.Entry<String,String[]> map : parameterMap.entrySet()){
            String key = map.getKey();
            String[] values = map.getValue();
            String valueStr = "";
            for(int i=0;i<values.length;i++){
                valueStr = (i == values.length-1)?valueStr + values[i] : valueStr+values[i]+",";
            }
            params.put(key,valueStr);
        }
        LOG.info("支付宝回调：sign{},trade_status{},参数{}",params.get("sign"),params.get("trade_status"),params.toString());
        //验证回调的正确性 ， 确实是否使我们发出的  并且过滤重复通知
        params.remove("sign_type");
        try {
            boolean flag = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if(!flag){
                return ServerResponse.createByErrorMsg("非法请求，验证不通过");
            }

        } catch (AlipayApiException e) {
            LOG.error("支付宝回调异常"+e.getMessage());
        }
        ServerResponse serverResponse = orderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }
    /**
      * @Author:蒋帅锋
      * @Description: 
      * @params:[session, orderNo] 
      * @return: com.mmall.common.ServerResponse<java.lang.Boolean>
      * @Date: 2018/5/24 14:14
      */ 
    @RequestMapping(value = "queryOrderPayStatus.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = orderService.queryOrderPayStatus(user.getId(), orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

}
