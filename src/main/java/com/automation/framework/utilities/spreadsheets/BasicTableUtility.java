package com.automation.framework.utilities.spreadsheets;

import com.automation.framework.model.excel.ExcelColumn;
import com.automation.framework.model.excel.ExcelRow;
import com.automation.framework.model.excel.ExcelTable;

import java.util.List;
import java.util.stream.Collectors;

public class BasicTableUtility extends SpreadSheet {

    public static ExcelTable getSheetTable(String file, String sheetName) {
        List<ExcelRow> rows = getSheetData(file, sheetName);
        ExcelRow header = rows.get(0);
        ExcelTable table = new ExcelTable();
        table.setHeader(header);
        table.setRows(rows.subList(1, rows.size()));
        table.setTotalRows(rows);
        return table;
    }

    public static List<List<String>> getSheetContent(String file, String sheetName) {
        List<ExcelRow> rows = getSheetData(file, sheetName);
        return rows.stream().map(row -> row.getCells().stream().map(ExcelColumn::getValue)
                        .collect(Collectors.toList()))
                        .collect(Collectors.toList());
    }


}
