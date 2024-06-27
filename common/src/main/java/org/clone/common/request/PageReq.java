package org.clone.common.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public class PageReq {

    @NotNull(message = "[page] cannot be null")
    private Integer page;

    @NotNull(message = "[the number of data on every page] cannot be null")
    @Max(value = 100, message = "[the number of data on every page] cannot be larger than 100")
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
