package com.automation.framework.model.excel;

import lombok.Data;

import java.util.List;

public @Data
class ExcelRow {

    public int index;

    public String key;

    public List<ExcelColumn> cells;

    @Override
    public String toString() {
        return "ExcelRow{" +
                "index=" + index +
                ", cells=" + cells +
                '}';
    }
}
