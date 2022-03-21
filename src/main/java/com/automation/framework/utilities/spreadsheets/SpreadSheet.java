package com.automation.framework.utilities.spreadsheets;

import com.automation.framework.utilities.DateUtilities;
import com.automation.framework.utilities.RandomUtils;
import com.automation.framework.model.excel.ExcelColumn;
import com.automation.framework.model.excel.ExcelRow;
import com.automation.framework.model.excel.ExcelTable;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpreadSheet {

    private static final Logger log = LoggerFactory.getLogger(MultiTableUtility.class);
    private static FormulaEvaluator evaluator;
    public static boolean parsePatterns = true;

    public static Workbook getWorkbook(String filepath) {
        Workbook book = null;
        try {
            FileInputStream stream = new FileInputStream(filepath);
            if (filepath.endsWith(".xls")) {
                book = new HSSFWorkbook(stream);
            } else if (filepath.endsWith(".xlsx")) {
                book = new XSSFWorkbook(stream);
            } else {
                throw new IllegalArgumentException("Invalid file format to access through excel utilities : "
                        + filepath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert book != null;
        evaluator = book.getCreationHelper().createFormulaEvaluator();
        return book;
    }

    public static List<ExcelRow> getSheetData(Sheet sheet) {
        return getRowsData(sheet);
    }

    public static List<ExcelRow> getSheetData(String filepath, String sheetName) {
        Sheet sheet = getWorkbook(filepath).getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("unable to find the sheet : " + sheetName + " in the file : " + filepath);
        }
        return getRowsData(sheet);
    }

    public static List<ExcelRow> getRowsData(Sheet sheet) {
        List<ExcelRow> rows = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            rows.add(getRow(rowIterator.next()));
        }
        return rows;
    }

    public static ExcelRow getRow(Row row) {
        ExcelRow excelRow = new ExcelRow();
        excelRow.setIndex(row.getRowNum());
        Iterator<Cell> cells = row.cellIterator();
        List<ExcelColumn> columnList = new ArrayList<>();
        while (cells.hasNext()) {
            Cell cell = cells.next();
            String cell_value = getCellData(cell);
            cell_value = convertValue(cell_value);
            ExcelColumn column = new ExcelColumn();
            column.setCell(cell);
            column.setIndex(cell.getColumnIndex());
            column.setValue(cell_value);
            columnList.add(column);
        }
        excelRow.setCells(columnList);
        return excelRow;
    }

    public static String getCellData(Cell cell) {
        DataFormatter fmt = new DataFormatter();
        switch (cell.getCellType()) {
            case FORMULA:
                return fmt.formatCellValue(cell, evaluator);
            case STRING:
                return cell.getStringCellValue();
            case BLANK:
                return "";
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case NUMERIC:
                return fmt.formatCellValue(cell);
            default:
                int rowIndex = cell.getRowIndex();
                int colIndex = cell.getColumnIndex();
                String sheet_name = cell.getSheet().getSheetName();
                String cell_location = "(" + rowIndex + "," + colIndex + ")";
                log.warn("unable to parse the cell value at location : " + cell_location + " in the sheet " + sheet_name);
                return null;
        }
    }

    /**
     * convert the cell data if pattern matches {{date/random : options}}
     *
     * @param value patterned text
     * @return returns resolved value if text matches pattern
     */
    public static String convertValue(String value) {
        Pattern pattern = Pattern.compile("[{][{]([^{}]*)[}][}]");
        if (parsePatterns) {
            if (checkStringContainsPattern(value, pattern)) {
                String identifier = getValueIfMatched(value, pattern, 2, 2);
                if (identifier.startsWith("random")) {
                    /*String[] values = StringUtils.split(identifier, ":", 2);*/
                    value = RandomUtils.getValue(identifier.strip());
                } else if (identifier.startsWith("date")) {
                    String[] values = StringUtils.split(identifier, ":", 2);
                    value = DateUtilities.evalDate(values[1].strip());
                }
            }
        }
        return value;
    }

    public static List<ExcelTable> getSheetTables(String file, String sheetName) {
        Sheet sheet = getWorkbook(file).getSheet(sheetName);
        return getSheetTables(sheet);
    }

    public static List<ExcelTable> getSheetTables(Sheet sheet) {
        StopWatch watch = new StopWatch();
        watch.start();
        List<ExcelTable> tables = new ArrayList<>();
        List<ExcelRow> rows = getSheetData(sheet);
        ExcelTable table = null;
        List<ExcelRow> tableRows = new ArrayList<>();
        List<ExcelRow> totalTableRows = new ArrayList<>();
        for (ExcelRow row : rows) {
            if (row.getCells().size() == 1) {
                String label = row.getCells().get(0).getValue();
                if (label.equalsIgnoreCase("end")) {
                    assert table != null;
                    table.setRows(tableRows);
                    table.setTotalRows(totalTableRows);
                    tables.add(table);
                } else if (label.endsWith("_table")) {
                    table = new ExcelTable();
                    tableRows = new ArrayList<>();
                    totalTableRows = new ArrayList<>();
                    table.setName(row.getCells().get(0).getValue());
                }
            } else if (row.getCells().size() > 1) {
                String index_0 = row.getCells().get(0).getValue();
                if (index_0.equalsIgnoreCase("header")) {
                    assert table != null;
                    table.setHeader(row);
                    totalTableRows.add(row);
                } else {
                    tableRows.add(row);
                    totalTableRows.add(row);
                }
            }
        }
        watch.stop();
        log.debug("sheet to tables conversion : " + watch.getTime(TimeUnit.MILLISECONDS));
        return tables;
    }

    public static ExcelRow getTableRowByHeader(ExcelTable table, String key) {
        List<ExcelRow> rows = table.getRows();
        ExcelRow expected_row = null;
        for (ExcelRow row : rows) {
            String index_0 = row.getCells().get(0).getValue().trim();
            if (index_0.equals(key)) {
                expected_row = row;
                break;
            }
        }
        if (expected_row == null) {
            throw new IllegalArgumentException("unable to find key : " + key + " in the table : " + table.getName());
        }
        return expected_row;
    }

    public static String getValueIfMatched(String value, Pattern pattern, int lead, int trail) {
        Matcher matcher = pattern.matcher(value);
        String result = null;
        while (matcher.find()) {
            int start_index = matcher.start();
            int end_index = matcher.end();
            result = value.substring(start_index + lead, end_index - trail);
        }
        return result;
    }

    public static boolean checkStringContainsPattern(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count > 0;
    }

    public static ValueOptions getValueOptions(String pattern) {
        String[] options = pattern.split(";");
        ValueOptions valueOptions = new ValueOptions();
        valueOptions.setValue(pattern);
        for (String option : options) {
            String[] args = option.split("=");
            switch (args[0].toLowerCase()) {
                case "sheet":
                    valueOptions.setSheet(args[1].trim());
                    break;
                case "table":
                    valueOptions.setTable(args[1].trim());
                    break;
                case "key":
                    valueOptions.setKey(Arrays.stream(args[1].split(",")).
                            map(String::strip).collect(Collectors.toList()));
                    break;
            }
        }
        return valueOptions;
    }

    @Data
    @ToString
    public static class ValueOptions {
        String value;
        String sheet;
        String table;
        List<String> key;
    }

}
