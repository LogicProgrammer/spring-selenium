package com.automation.framework.utilities.spreadsheets;

import com.automation.framework.utilities.DateUtilities;
import com.automation.framework.utilities.RandomUtils;
import com.automation.framework.model.excel.ExcelRow;
import com.automation.framework.model.excel.ExcelTable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MultiTableUtility extends SpreadSheet {

    private static final Logger log = LoggerFactory.getLogger(MultiTableUtility.class);

    public static Map<String, List<ExcelTable>> getTablesFromAllSheets(String filepath) {
        Map<String, List<ExcelTable>> sheetMap = new HashMap<>();
        Workbook book = getWorkbook(filepath);
        int sheetCount = book.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = book.getSheetAt(i);
            sheetMap.put(sheet.getSheetName(), getSheetTables(sheet));
        }
        return sheetMap;
    }

    public static ExcelTable getTableFromSheet(String file,String sheet, String table) {
        ExcelTable required_table = getSheetTables(file,sheet).stream().
                filter(excelTable -> excelTable.getName().equalsIgnoreCase(table)).findAny().orElse(null);
        if (required_table == null) {
            throw new IllegalArgumentException("unable to find table with the name : " + table+" in sheet : "+sheet);
        }
        return required_table;
    }

    public static ExcelTable getTableDataForKey(String file, String sheet, String table, List<String> keys) {
        StopWatch watch = new StopWatch();
        watch.start();
        ExcelTable required_table = getTableFromSheet(file,sheet,table);
        ArrayList<ExcelRow> rows = new ArrayList<>();
        for(String key : keys){
            ExcelRow row = getTableRowByHeader(required_table, key);
            rows.add(row);
        }
        ExcelTable data = new ExcelTable();
        data.setHeader(required_table.getHeader());
        data.setRows(rows);
        data.setTotalRows(new ArrayList<>() {{
            add(data.getHeader());
            addAll(rows);
        }});
        watch.stop();
        log.debug("time taken to fetch the excel data : "+watch.getTime(TimeUnit.MILLISECONDS));
        return data;
    }

    public static List<Map<String, String>> getMapsFromExcelTable(ExcelTable table) {
        ExcelRow header = table.getHeader();
        List<ExcelRow> dataRow = table.getRows();
        List<Map<String,String>> maps = new ArrayList<>();
        dataRow.forEach(row -> {
            Map<String, String> data = new LinkedHashMap<>();
            for (int count = 0; count < header.cells.size(); count++) {
                data.put(header.cells.get(count).getValue(), row.cells.get(count).getValue());
            }
            maps.add(data);
        });
        return maps;
    }

    /**
     *  for fetching complex data structures using spreadSheet tables
     * @param sheet name of the sheet
     * @param table name of the table
     * @param key key value to identify
     * @return Map object
     */
    public static List<Map<String, Object>> getMapFromExcelTable(String file,String sheet, String table, List<String> key) {
        Pattern pattern = Pattern.compile("[{][{]([^{}]*)[}][}]");
        ExcelTable excelTable = getTableDataForKey(file,sheet, table, key);
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<Map<String, String>> list = getMapsFromExcelTable(excelTable);
        list.forEach(t->{
            Map<String,Object> data = new LinkedHashMap<>();
            t.forEach((x, y) -> {
                if (checkStringContainsPattern(y, pattern)) {
                    String identifier = getValueIfMatched(y, pattern, 2, 2);
                    if (identifier.contains(";")) {
                        SpreadSheet.ValueOptions options = getValueOptions(identifier);
                        List<Map<String, Object>> subMap = getMapFromExcelTable(file, options.getSheet(), options.getTable(),
                                options.getKey());
                        data.put(x, subMap);
                    } else {
                        if (identifier.startsWith("random")) {
                            String[] values = StringUtils.split(identifier, ":", 2);
                            data.put(x, RandomUtils.getValue(values[1].strip()));
                        } else if(identifier.startsWith("date")){
                            String[] values = StringUtils.split(identifier, ":", 2);
                            data.put(x, DateUtilities.evalDate(values[1].strip()));
                        }
                    }
                } else {
                    data.put(x, y);
                }
            });
            dataList.add(data);
        });
        return dataList;
    }


}
