package com.rocktech.boarddriver.bean;

public class GetVersionBean extends BaseRes {
    private String model;
    private String version;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
