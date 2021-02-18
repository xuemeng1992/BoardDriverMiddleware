package com.rocktech.boarddriver.bean;

import java.util.List;

public class DoubleOpenConfig {


    /**
     * type : -1
     * doubleconfig : [{"boxid":1,"mappingid":"1,2"},{"boxid":2,"mappingid":"3,4"},{"boxid":3,
     * "mappingid":"5,6"}]
     */

    private int type;
    private List<DoubleconfigBean> doubleconfig;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<DoubleconfigBean> getDoubleconfig() {
        return doubleconfig;
    }

    public void setDoubleconfig(List<DoubleconfigBean> doubleconfig) {
        this.doubleconfig = doubleconfig;
    }

    public static class DoubleconfigBean {
        /**
         * boxid : 1
         * mappingid : 1,2
         */

        private int boxid;
        private String mappingid;

        public int getBoxid() {
            return boxid;
        }

        public void setBoxid(int boxid) {
            this.boxid = boxid;
        }

        public String getMappingid() {
            return mappingid;
        }

        public void setMappingid(String mappingid) {
            this.mappingid = mappingid;
        }
    }
}
