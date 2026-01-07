package com.erply.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityListResponse<T> {
    private List<T> items;
    private Integer totalCount;

    public List<T> getItems() { return items; }
    public void setItems(List<T> items) { this.items = items; }

    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
}
