package com.automation.framework.utilities.msgraph;

import com.automation.framework.utilities.TableFormatter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.Workbook;
import com.microsoft.graph.models.WorkbookRange;
import com.microsoft.graph.models.WorkbookTable;
import com.microsoft.graph.models.WorkbookWorksheet;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.WorkbookTableCollectionPage;
import com.microsoft.graph.requests.WorkbookTableColumnCollectionPage;
import com.microsoft.graph.requests.WorkbookTableRowCollectionPage;
import okhttp3.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExcelClient {

    private final String START = "_table";
    private final String END = "end";
    private final String HEADER = "header";
    private TokenCredentialAuthProvider auth;
    private String userID;

    public ExcelClient(TokenCredentialAuthProvider auth, String userID) {
        this.auth = auth;
        this.userID = userID;
    }

    // method for creating graph client
    // this requires token credential auth provider for creating the client
    public GraphServiceClient<Request> getGraphClient() {
        return GraphServiceClient.builder().authenticationProvider(auth).buildClient();
    }

    public WorkbookRange getSheetRange(String workbookID, String sheet) {
        GraphServiceClient<Request> client = getGraphClient();
        return client.users(userID).drive().items(workbookID).workbook().worksheets(sheet)
                .usedRange()
                .buildRequest()
                .get();
    }

    // this method only works for tables in sheets
    // for raw tables please use workbook range
    public List<List<String>> getTables(String workbookID, String sheetName, String tableName) {
        GraphServiceClient<Request> client = getGraphClient();
        WorkbookTableRowCollectionPage rows = client.users(userID).drive().items(workbookID).workbook()
                .worksheets(sheetName).tables(tableName).rows()
                .buildRequest()
                .get();
        WorkbookTableColumnCollectionPage columns = client.users(userID).drive().items(workbookID).workbook()
                .worksheets(sheetName).tables(tableName).columns()
                .buildRequest()
                .get();
        List<List<String>> table = new ArrayList<>();
        List<String> header = new ArrayList<>();
        columns.getCurrentPage().forEach(col->{
            header.add(col.name);
        });
        table.add(header);
        rows.getCurrentPage().forEach(row->{
            List<String> rowData = new ArrayList<>();
            row.values.getAsJsonArray().forEach(element->{
                element.getAsJsonArray().forEach(ele->{
                    rowData.add(ele.getAsString());
                });
            });
            table.add(rowData);
        });
        return table;
    }

    public List<List<String>> getRawTableContentFromSheet(String workbookID, String sheet){
        WorkbookRange range = getSheetRange(workbookID, sheet);
        JsonArray array = range.values.getAsJsonArray();
        List<List<String>> table = new ArrayList<>();
        for (JsonElement values : array) {
            List<String> rowData = new ArrayList<>();
            JsonArray cells = values.getAsJsonArray();
            cells.forEach(cell->{
                rowData.add(cell.getAsString());
            });
            table.add(rowData);
        }
        return table;
    }

    public List<List<String>> getTableDataUsingKey(String workbookId, String sheetName, List<String> keys) {
        List<List<String>> tableData = getRawTableContentFromSheet(workbookId,sheetName);
        List<List<String>> result = new ArrayList<>();
        result.add(tableData.get(0));
        result.addAll(tableData.stream().filter(row-> keys.contains(row.get(0))).collect(Collectors.toList()));
        if(result.size() == 1){
            throw new IllegalStateException("unable to find rows with keys : "+keys+" in the sheet : "+sheetName);
        }
        return result;
    }

    public List<List<String>> getTableDataUsingKey(String workbookId, String sheetName, String tableName, List<String> keys) {
        List<List<String>> tableData = getTables(workbookId,sheetName,tableName);
        List<List<String>> result = new ArrayList<>();
        result.add(tableData.get(0));
        result.addAll(tableData.stream().filter(row-> keys.contains(row.get(0))).collect(Collectors.toList()));
        if(result.size() == 1){
            throw new IllegalStateException("unable to find rows with keys : "+keys+" in the table : "+tableName);
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

    /*public ExcelTable getExcelRows(String workbookID, String sheet, String tableName) {
        WorkbookRange range = getSheetRange(workbookID, sheet);
        assert range.values != null;
        JsonArray array = range.values.getAsJsonArray();
        List<ExcelTable> tables = new ArrayList<>();
        ExcelTable table = null;
        List<ExcelRow> rows = new ArrayList<>();
        int headerCellCount = 0;
        for (JsonElement values : array) {
            JsonArray cells = values.getAsJsonArray();
            ExcelRow row = new ExcelRow();
            String cell_index0 = cells.get(0).getAsString();
            if (!cell_index0.isEmpty()) {
                if (cell_index0.contains("_table")) {
                    table = new ExcelTable();
                    table.setName(cells.get(0).getAsString());
                } else if (cell_index0.equalsIgnoreCase("header")) {
                    List<ExcelColumn> columns = new ArrayList<>();
                    for (JsonElement cell : cells) {
                        ExcelColumn column = new ExcelColumn();
                        column.setValue(cell.getAsString());
                        columns.add(column);
                    }
                    row.setCells(columns);
                    table.setHeader(row);
                    headerCellCount = cells.size();
                } else if (cell_index0.equalsIgnoreCase("end")) {
                    table.setRows(rows);
                    rows.clear();
                    tables.add(table);
                    headerCellCount = 0;
                } else {
                    List<ExcelColumn> columns = new ArrayList<>();
                    for (int i = 0; i < headerCellCount; i++) {
                        ExcelColumn column = new ExcelColumn();
                        column.setValue(cells.get(i).getAsString());
                        columns.add(column);
                    }
                    row.setCells(columns);
                    rows.add(row);
                }
            }
        }
        System.out.println(tables);
        return tables.stream().filter(t -> t.getName().equalsIgnoreCase(tableName)).collect(Collectors.toList()).get(0);
    }

    public ExcelTable getExcelRows(String workbookID, String sheet, String tableName, String key) {
        WorkbookRange range = getSheetRange(workbookID, sheet);
        assert range.values != null;
        JsonArray array = range.values.getAsJsonArray();
        List<ExcelTable> tables = new ArrayList<>();
        ExcelTable table = null;
        List<ExcelRow> rows = new ArrayList<>();
        int headerCellCount = 0;
        for (JsonElement values : array) {
            JsonArray cells = values.getAsJsonArray();
            ExcelRow row = new ExcelRow();
            String cell_index0 = cells.get(0).getAsString();
            if (!cell_index0.isEmpty()) {
                if (cell_index0.contains("_table")) {
                    table = new ExcelTable();
                    table.setName(cells.get(0).getAsString());
                } else if (cell_index0.equalsIgnoreCase("header")) {
                    List<ExcelColumn> columns = new ArrayList<>();
                    for (JsonElement cell : cells) {
                        ExcelColumn column = new ExcelColumn();
                        column.setValue(cell.getAsString());
                        columns.add(column);
                    }
                    row.setCells(columns);
                    table.setHeader(row);
                    headerCellCount = cells.size();
                } else if (cell_index0.equalsIgnoreCase("end")) {
                    table.setRows(rows);
                    rows.clear();
                    tables.add(table);
                    headerCellCount = 0;
                } else {
                    List<ExcelColumn> columns = new ArrayList<>();
                    for (int i = 0; i < headerCellCount; i++) {
                        ExcelColumn column = new ExcelColumn();
                        column.setValue(cells.get(i).getAsString());
                        columns.add(column);
                    }
                    row.setKey(columns.get(0).getValue());
                    row.setCells(columns);
                    rows.add(row);
                }
            }
        }
        ExcelTable requiredTable = tables.stream().filter(t -> t.getName().equalsIgnoreCase(tableName)).collect(Collectors.toList()).get(0);
        List<ExcelRow> targetRows = requiredTable.getRows().stream().filter(r -> r.getKey().equalsIgnoreCase(key)).collect(Collectors.toList());
        ExcelTable target = new ExcelTable();
        target.setHeader(requiredTable.getHeader());
        target.setRows(targetRows);
        return target;
    }

    public void printAvailableTables(String workbookID, String sheet) {
        WorkbookRange range = getSheetRange(workbookID, sheet);
        assert range.values != null;
        JsonArray array = range.values.getAsJsonArray();
        List<ExcelTable> tables = getTables(array);
        System.out.println("available tables are : ");
        tables.forEach(t -> {
            System.out.println(t.getName());
        });
    }

    public void printSheetContent(String workbookID, String sheet) {
        WorkbookRange range = getSheetRange(workbookID, sheet);
        assert range.values != null;
        JsonArray array = range.values.getAsJsonArray();
        List<ExcelTable> tables = getTables(array);
        printTables(tables);
    }

    public ExcelTable getTable(String workbookID, String sheet, String tableName) {
        WorkbookRange range = getSheetRange(workbookID, sheet);
        assert range.values != null;
        JsonArray array = range.values.getAsJsonArray();
        List<ExcelTable> tables = getTables(array);
        return tables.stream().filter(t -> t.getName().equalsIgnoreCase(tableName)).collect(Collectors.toList()).get(0);
    }

    public ExcelTable getTable(String workbookID, String sheet, String tableName, List<String> keys) {
        ExcelTable table = getTable(workbookID, sheet, tableName);
        ExcelTable target = new ExcelTable();
        target.setName(table.getName());
        target.setHeader(table.getHeader());
        List<ExcelRow> finalRows = new ArrayList<>();
        keys.forEach(key->{
            finalRows.addAll(table.getRows().stream().filter(r -> r.getKey().equalsIgnoreCase(key)).toList());
        });
        target.setRows(finalRows);
        List<ExcelRow> totalRows = new ArrayList<>();
        totalRows.add(table.getHeader());
        totalRows.addAll(finalRows);
        target.setTotalRows(totalRows);
        return target;
    }

    *//**
     * constructing the tables
     *
     * @param array
     *//*
    public List<ExcelTable> getTables(JsonArray array) {
        List<ExcelTable> tables = new ArrayList<>();
        ExcelTable table = null;
        int columnsCount = 0;
        List<ExcelRow> rows = new ArrayList<>();
        ExcelRow headerRow = new ExcelRow();
        for (JsonElement row : array) {
            JsonArray columns = row.getAsJsonArray();
            String key = columns.get(0).getAsString();
            if (StringUtils.isNotBlank(key)) {
                if (key.contains(START)) {
                    table = new ExcelTable();
                    table.setName(key);
                    rows = new ArrayList<>();
                } else if (key.equalsIgnoreCase(HEADER)) {
                    columns = removeBlanksInHeader(columns);
                    columnsCount = columns.size();
                    assert table != null;
                    headerRow = getExcelRow(columns, columnsCount);
                } else if (key.equalsIgnoreCase(END)) {
                    assert table != null;
                    table.setHeader(headerRow);
                    table.setRows(rows);
                    ExcelRow finalHeaderRow = headerRow;
                    List<ExcelRow> finalRows = rows;
                    List<ExcelRow> totalRows = new ArrayList<ExcelRow>() {{
                        add(finalHeaderRow);
                        addAll(finalRows);
                    }};
                    table.setTotalRows(totalRows);
                    tables.add(table);
                    table = null;
                    columnsCount = 0;
                } else {
                    assert table != null;
                    rows.add(getExcelRow(columns, columnsCount));
                }
            }
        }
        return tables;
    }

    public ExcelRow getExcelRow(JsonArray columns, int limit) {
        ExcelRow row = new ExcelRow();
        row.setKey(columns.get(0).getAsString());
        List<ExcelColumn> columnsList = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            ExcelColumn column = new ExcelColumn();
            column.setElement(columns.get(i).deepCopy());
            column.setValue(columns.get(i).getAsString());
            column.setIndex(i);
            columnsList.add(column);
        }
        row.setCells(columnsList);
        return row;
    }

    public void printTables(List<ExcelTable> tables) {
        tables.forEach(table -> {
            printTable(table);
            System.out.println("\n");
        });
    }

    public void printTable(ExcelTable table) {
        System.out.println("printing table : " + table.getName());
        table.getTotalRows().forEach(r -> {
            System.out.print("| ");
            r.getCells().forEach(c -> {
                System.out.print(c.getValue() + " |");
            });
            System.out.print("\n");
        });
    }

    public JsonArray removeBlanksInHeader(JsonArray columns) {
        JsonArray finalArray = new JsonArray();
        for (JsonElement column : columns) {
            if (StringUtils.isNotBlank(column.getAsString())) {
                finalArray.add(column);
            }
        }
        return finalArray;
    }

    public List<Map<String, Object>> getMapsFromExcelTable(ExcelTable table) {
        List<Map<String, Object>> maps = new ArrayList<>();
        ExcelRow header = table.getHeader();
        List<ExcelColumn> headerColumns = header.getCells();
        table.getRows().forEach((r) -> {
            List<ExcelColumn> columns = r.getCells();
            Map<String, Object> map = new LinkedHashMap<>();
            for (int i = 0; i < header.getCells().size(); i++) {
                map.put(headerColumns.get(i).getValue(), columns.get(i).getElement());
            }
            map.remove(HEADER);
            maps.add(map);
        });
        return maps;
    }*/

}
