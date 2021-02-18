package com.rocktech.boarddriver.bean;

import java.util.List;

public class CupboardFunctionBean {


    /**
     * id : 00
     * name : 智能柜(原快递柜)
     * cupboardProductType : [{"id":"00","name":"智能柜I（原丰巢快递柜）"},{"id":"01","name":"智能柜II（原标准柜）"},{"id":"02","name":"智能柜III（原简易柜）"}]
     */

    private String id;
    private String name;
    private List<CupboardProductTypeBean> cupboardProductType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CupboardProductTypeBean> getCupboardProductType() {
        return cupboardProductType;
    }

    public void setCupboardProductType(List<CupboardProductTypeBean> cupboardProductType) {
        this.cupboardProductType = cupboardProductType;
    }

    public static class CupboardProductTypeBean {
        /**
         * id : 00
         * name : 智能柜I（原丰巢快递柜）
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
