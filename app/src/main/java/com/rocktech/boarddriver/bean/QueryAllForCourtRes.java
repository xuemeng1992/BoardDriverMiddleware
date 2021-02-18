package com.rocktech.boarddriver.bean;

import java.util.List;

public class QueryAllForCourtRes  {


    /**
     * result_code : 4240
     * error_msg : success
     * cells_info : [{"result_code":4112,"error_msg":"success","cell_index":1,"lock_status":1,"goods_status":1},{"result_code":4112,"error_msg":"success","cell_index":2,"lock_status":2,"goods_status":1}]
     * lock_count : 2
     */

    private int result_code;
    private String error_msg;
    private int lock_count;
    private List<CellsInfoBean> cells_info;

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

    public int getLock_count() {
        return lock_count;
    }

    public void setLock_count(int lock_count) {
        this.lock_count = lock_count;
    }

    public List<CellsInfoBean> getCells_info() {
        return cells_info;
    }

    public void setCells_info(List<CellsInfoBean> cells_info) {
        this.cells_info = cells_info;
    }

    public static class CellsInfoBean {
        /**
         * result_code : 4112
         * error_msg : success
         * cell_index : 1
         * lock_status : 1
         * goods_status : 1
         */

        private int result_code;
        private String error_msg;
        private int cell_index;
        private int lock_status;
        private int goods_status;

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

        public int getCell_index() {
            return cell_index;
        }

        public void setCell_index(int cell_index) {
            this.cell_index = cell_index;
        }

        public int getLock_status() {
            return lock_status;
        }

        public void setLock_status(int lock_status) {
            this.lock_status = lock_status;
        }

        public int getGoods_status() {
            return goods_status;
        }

        public void setGoods_status(int goods_status) {
            this.goods_status = goods_status;
        }
    }
}
