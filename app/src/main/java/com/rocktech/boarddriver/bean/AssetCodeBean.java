package com.rocktech.boarddriver.bean;

import java.io.Serializable;

public class AssetCodeBean implements Serializable {

    private String assetCode;//R02201200034444444
    private String productModel;
    private String originalAssetCode;//R02201200034444444NNN
    private String boxId;//D**
    private String realBoxId;

    public AssetCodeBean() {

    }

    public AssetCodeBean(String assetCode, String boxId) {
        this.assetCode = assetCode;
        this.boxId = boxId;
    }

    public AssetCodeBean(String assetCode, String boxId, String originalAssetCode) {
        this.assetCode = assetCode;
        this.boxId = boxId;
        this.originalAssetCode = originalAssetCode;
    }

    public AssetCodeBean(String assetCode, String boxId, String originalAssetCode, String productModel, String realBoxId) {
        this.assetCode = assetCode;
        this.productModel = productModel;
        this.originalAssetCode = originalAssetCode;
        this.boxId = boxId;
        this.realBoxId = realBoxId;
    }

    public String getRealBoxId() {
        return realBoxId;
    }

    public void setRealBoxId(String realBoxId) {
        this.realBoxId = realBoxId;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getOriginalAssetCode() {
        return originalAssetCode;
    }

    public void setOriginalAssetCode(String originalAssetCode) {
        this.originalAssetCode = originalAssetCode;
    }

    @Override
    public String toString() {
        return "AssetCodeBean{" +
                "assetCode='" + assetCode + '\'' +
                ", productModel='" + productModel + '\'' +
                ", originalAssetCode='" + originalAssetCode + '\'' +
                ", boxId='" + boxId + '\'' +
                ", realBoxId='" + realBoxId + '\'' +
                '}';
    }
}
