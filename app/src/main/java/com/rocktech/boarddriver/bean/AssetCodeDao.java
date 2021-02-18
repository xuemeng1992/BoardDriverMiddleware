package com.rocktech.boarddriver.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AssetCodeDao {

    private String assetCode;//201200034444444
    private String boxId;//D**
    private String assetCodeTitle;//R02
    private String productModel;

    @Generated(hash = 613725055)
    public AssetCodeDao(String assetCode, String boxId, String assetCodeTitle,
                        String productModel) {
        this.assetCode = assetCode;
        this.boxId = boxId;
        this.assetCodeTitle = assetCodeTitle;
        this.productModel = productModel;
    }

    @Generated(hash = 521476181)
    public AssetCodeDao() {
    }

    public String getAssetCode() {
        return this.assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getBoxId() {
        return this.boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getAssetCodeTitle() {
        return this.assetCodeTitle;
    }

    public void setAssetCodeTitle(String assetCodeTitle) {
        this.assetCodeTitle = assetCodeTitle;
    }

    public String getProductModel() {
        return this.productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }


}
