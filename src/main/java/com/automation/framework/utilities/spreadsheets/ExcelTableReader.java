package com.automation.framework.utilities.spreadsheets;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelTableReader {

    public static XSSFTable getXSSFTableUsingName(String filePath, String sheet, String tableName) {
        Workbook book = SpreadSheet.getWorkbook(filePath);
        List<XSSFTable> tables = ((XSSFSheet) book.getSheet(sheet)).getTables();
        return tables.stream().filter(table -> table.getDisplayName().equals(tableName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unable to find table : " +
                        tableName + " in sheet : " + sheet));
    }

    public static XSSFTable getXSSFTableUsingIndex(String filePath, String sheet,int index) {
        Workbook book = SpreadSheet.getWorkbook(filePath);
        List<XSSFTable> tables = ((XSSFSheet) book.getSheet(sheet)).getTables();
        if(!(tables.size() >= index)){
            throw new IllegalStateException("no of tables present in the sheet : "+tables.size());
        }
        return tables.get(index);
    }

    public static List<List<String>> getTable(String filePath, String sheetName, String tableName) {
        Workbook book = SpreadSheet.getWorkbook(filePath);
        XSSFTable xssfTable = getXSSFTableUsingName(filePath,sheetName,tableName);
        XSSFSheet sheet = (XSSFSheet) book.getSheet(sheetName);
        return getXSSFTableContent(xssfTable,sheet);
    }

    public static List<List<String>> getTable(String filePath, String sheetName, int index) {
        Workbook book = SpreadSheet.getWorkbook(filePath);
        XSSFTable xssfTable = getXSSFTableUsingIndex(filePath,sheetName,index);
        XSSFSheet sheet = (XSSFSheet) book.getSheet(sheetName);
        return getXSSFTableContent(xssfTable,sheet);
    }

    public static List<List<String>> getXSSFTableContent(XSSFTable xssfTable,XSSFSheet sheet){
        int startRow = xssfTable.getStartCellReference().getRow();
        int endRow = xssfTable.getEndCellReference().getRow();
        int startColumn = xssfTable.getStartCellReference().getCol();
        int endColumn = xssfTable.getEndCellReference().getCol();
        List<List<String>> result = new ArrayList<>();
        for (int i = startRow; i <= endRow; i++) {
            List<String> rowData = new ArrayList<>();
            for (int j = startColumn; j <= endColumn; j++) {
                XSSFCell cell = sheet.getRow(i).getCell(j);
                String cellVal = "";
                if (cell != null) {
                    cellVal = SpreadSheet.getCellData(cell);
                }
                rowData.add(cellVal);
            }
            result.add(rowData);
        }
        return result;
    }

    public static List<List<String>> getTableDataUsingKey(String filePath, String sheet, String table, List<String> keys) {
        List<List<String>> tableData = getTable(filePath,sheet,table);
        List<List<String>> result = new ArrayList<>();
        result.add(tableData.get(0));
        result.addAll(tableData.stream().filter(row-> keys.contains(row.get(0))).collect(Collectors.toList()));
        if(result.size() == 1){
            throw new IllegalStateException("unable to find rows with keys : "+keys+" in the table : "+table);
        }
        return result;
    }

    public static List<List<String>> getTableDataUsingIndex(String filePath, String sheet, String table,int index) {
        List<List<String>> tableData = getTable(filePath,sheet,table);
        List<List<String>> result = new ArrayList<>();
        result.add(tableData.get(0));
        result.add(tableData.get(index));
        return result;
    }

    public static List<List<String>> getTableDataUsingRange(String filePath, String sheet, String table,int start,int end) {
        List<List<String>> tableData = getTable(filePath,sheet,table);
        List<List<String>> result = new ArrayList<>();
        result.add(tableData.get(0));
        for(int i=start;i<=end;i++){
            result.add(tableData.get(i));
        }
        return result;
    }

    public static List<List<String>> filterByIndex(List<List<String>> content,List<Integer> indexList){
        List<List<String>> result = new ArrayList<>();
        result.add(content.get(0));
        indexList.forEach(index->{
            result.add(content.get(index));
        });
        return result;
    }

    public static List<List<String>> filterByRange(List<List<String>> content,int start,int end){
        List<List<String>> result = new ArrayList<>();
        result.add(content.get(0));
        for(int i=start;i<=end;i++){
            result.add(content.get(i));
        }
        return result;
    }

    public static List<List<String>> getTableDataFromSheet(String filePath,String sheet){
        return getTable(filePath,sheet,0);
    }


}
