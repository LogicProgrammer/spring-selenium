package com.automation.framework.model.excel;

import com.google.gson.JsonElement;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;

public @Data
class ExcelColumn {

    private Cell cell;

    private String value;

    private int index;

    private String type;

    private JsonElement element;

    @Override
    public String toString() {
        return "ExcelColumn{" +
                "cell=" + cell +
                ", value='" + value + '\'' +
                ", index=" + index +
                '}';
    }
}
