package com.gdsc.toplearth_server.core.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class PageInfoDto {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private long totalItems;
    private int currentItems;

    public PageInfoDto(Page<?> page) {
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
        this.totalItems = page.getTotalElements();
        this.currentItems = page.getNumberOfElements();
    }
}