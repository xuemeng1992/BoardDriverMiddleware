package android_serialport_api.bean;

import java.util.Arrays;

public class Data {

    private byte[] data;
    private int tag;
    private int handle;

    public byte[] getData() {
        return data;
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Data [data=" + Arrays.toString(data) + ", tag=" + tag + "]";
    }

}
