package com.rocktech.boarddriver.bean;

public class MessageEvent {
    private Object object;
    private int type;

    public MessageEvent() {
    }

    public MessageEvent(Object object, int type) {
        this.object = object;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
