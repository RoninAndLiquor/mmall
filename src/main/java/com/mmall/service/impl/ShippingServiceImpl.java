package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse<Map<String,Object>> add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int insert = shippingMapper.insert(shipping);
        if(insert>0){
            Map<String,Object> map = Maps.newHashMap();
            map.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新增地址成功",map);
        }
        return ServerResponse.createByErrorMsg("新增地址失败");
    }

    public ServerResponse<String> delete(Integer userId,Integer shippingId){
        int count = shippingMapper.deleteByShippingIdAndUserId(shippingId, userId);
        if(count >0){
            return ServerResponse.createBySuccess("删除成功");
        }
        return ServerResponse.createByErrorMsg("删除失败");
    }

    public ServerResponse update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int insert = shippingMapper.updateByShipping(shipping);
        if(insert>0){

            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMsg("新增地址失败");
    }

    public ServerResponse<Shipping> select(Integer userId,Integer shippingId){
        Shipping shipping = shippingMapper.selectByShipingIdAndUserId(shippingId, userId);
        if(shipping==null){
            return ServerResponse.createByErrorMsg("无法查询到当前收货地址");
        }
        return ServerResponse.createBySuccess("查询成功",shipping);
    }
    public ServerResponse<PageInfo> list(Integer userId,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
