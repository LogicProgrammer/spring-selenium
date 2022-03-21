package com.automation.framework.model.excel;


import lombok.Data;

import java.util.List;

public @Data
class ExcelTable {

    private ExcelRow header;

    private List<ExcelRow> rows;

    private List<ExcelRow> totalRows;

    private String name;

    @Override
    public String toString() {
        return "ExcelTable{" +
                "header=" + header +
                ", rows=" + rows +
                ", totalRows=" + totalRows +
                ", name='" + name + '\'' +
                '}';
    }
}