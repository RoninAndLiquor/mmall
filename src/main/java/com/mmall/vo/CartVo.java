package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {

    private List<CartProductVO> cartProductVOList;
    private BigDecimal cartTotlePrice;
    private boolean allChecked;
    private String imageHost;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVO> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }

    public BigDecimal getCartTotlePrice() {
        return cartTotlePrice;
    }

    public void setCartTotlePrice(BigDecimal cartTotlePrice) {
        this.cartTotlePrice = cartTotlePrice;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
