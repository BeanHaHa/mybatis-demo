package com.wy.insurance.box.backend.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页Model
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public class PageModel<T> implements Serializable {

    /** 当前第几页 */
    private Integer page;
    /** 每页大小 */
    private Integer pageSize;
    /** 共几页 */
    private Long totalPage;
    /** 共多少条数据 */
    private Long total;
    /** 分页数据 */
    private List<Serializable> rows;

    PageModel() {
        super();
    }

    public PageModel(Integer page, Integer pageSize, Long totalPage, Long total, List rows) {
        super();
        this.page = page;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.total = total;
        this.rows = rows;
    }

    /** 获取第几页 */
    public int getPage() {
        return page;
    }

    /** 设置第几页 */
    public void setPage(Integer page) {
        this.page = page;
    }

    /** 获取每页多少数据 */
    public int getPageSize() {
        return pageSize;
    }

    /** 设置每页多少数据 */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /** 获取总页数 */
    public Long getTotalPage() {
        return this.totalPage;
    }

    /** 设置总页数 */
    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    /** 获取总记录数 */
    public Long getTotal() {
        return total;
    }

    /** 设置总记录数 */
    public void setTotal(Long total) {
        this.total = total;
    }

    /** 获取分页数据 */
    public List getRows() {
        if (rows == null) {
            rows = new ArrayList();
        }
        return rows;
    }

    /** 设置分页数据 */
    public void setRows(List rows) {
        this.rows = rows;
    }

    /** 加入分页数据 */
    public void addRow(T element) {
        if (element != null) {
            getRows().add(element);
        }
    }
}
