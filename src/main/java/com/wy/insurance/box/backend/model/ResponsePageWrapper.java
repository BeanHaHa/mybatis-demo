package com.wy.insurance.box.backend.model;

/**
 * 接口返回分页数据结果
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public class ResponsePageWrapper {

    private Long total;
    private Object rows;

    public ResponsePageWrapper(Long total, Object rows) {
        this.total = total;
        this.rows = rows;
    }
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }
}
