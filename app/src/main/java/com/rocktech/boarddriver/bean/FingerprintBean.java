package com.rocktech.boarddriver.bean;

public class FingerprintBean {

    private String id;
    private String fingerprintInfo;

    public FingerprintBean() {
    }

    public FingerprintBean(String id, String fingerprintInfo) {
        this.id = id;
        this.fingerprintInfo = fingerprintInfo;
    }

    public String getId() {
        return id.trim();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFingerprintInfo() {
        return fingerprintInfo.trim();
    }

    public void setFingerprintInfo(String fingerprintInfo) {
        this.fingerprintInfo = fingerprintInfo;
    }

    @Override
    public String toString() {
        return "FingerprintBean{" +
                "id='" + id + '\'' +
                ", fingerprintInfo='" + fingerprintInfo + '\'' +
                '}';
    }
}
