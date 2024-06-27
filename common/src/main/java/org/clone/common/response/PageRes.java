package org.clone.common.response;

import java.io.Serializable;
import java.util.List;

public class PageRes<T> implements Serializable {

    //总条数
    private Long total;

    //当前页列表
    private List<T> list;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
