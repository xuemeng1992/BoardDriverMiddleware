package com.rocktech.boarddriver.bean;

import java.util.List;

public class ReadLockerRes {


    /**
     * result_code : 4192
     * error_msg : success
     * cols_info : [{"result_code":4193,"error_msg":"success","col_id":"110010000001","col_index":1},{"result_code":4193,"error_msg":"success","col_id":"210020000002","col_index":2}]
     * col_count : 2
     */

    private int result_code;
    private String error_msg;
    private int col_count;
    private List<ColsInfoBean> cols_info;

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public int getCol_count() {
        return col_count;
    }

    public void setCol_count(int col_count) {
        this.col_count = col_count;
    }

    public List<ColsInfoBean> getCols_info() {
        return cols_info;
    }

    public void setCols_info(List<ColsInfoBean> cols_info) {
        this.cols_info = cols_info;
    }

    public static class ColsInfoBean {
        /**
         * result_code : 4193
         * error_msg : success
         * col_id : 110010000001
         * col_index : 1
         */

        private int result_code;
        private String error_msg;
        private String col_id;
        private String col_index;
        private String col_type;

        public int getResult_code() {
            return result_code;
        }

        public void setResult_code(int result_code) {
            this.result_code = result_code;
        }

        public String getError_msg() {
            return error_msg;
        }

        public void setError_msg(String error_msg) {
            this.error_msg = error_msg;
        }

        public String getCol_id() {
            return col_id;
        }

        public void setCol_id(String col_id) {
            this.col_id = col_id;
        }

        public String getCol_index() {
            return col_index;
        }

        public void setCol_index(String col_index) {
            this.col_index = col_index;
        }

        public String getCol_type() {
            return col_type;
        }

        public void setCol_type(String col_type) {
            this.col_type = col_type;
        }
    }
}
