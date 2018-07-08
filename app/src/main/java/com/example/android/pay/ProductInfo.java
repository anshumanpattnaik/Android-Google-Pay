package com.example.android.pay;

public class ProductInfo {
    private final String mProductName;
    private final int mProductImgRes;

    private final long mProductPrice;

    public ProductInfo(String productName, long productPrice, int productImgRes) {
        this.mProductName = productName;
        this.mProductPrice = productPrice;
        this.mProductImgRes = productImgRes;
    }

    public String getProductName() {
        return mProductName;
    }

    public int getProductImgRes() {
        return mProductImgRes;
    }

    public long getProductPrice() {
        return mProductPrice;
    }
}