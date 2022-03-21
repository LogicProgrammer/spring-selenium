package com.automation.framework.service;

import com.automation.framework.model.query.Query;
import com.automation.framework.model.query.Range;
import com.automation.framework.model.query.Table;
import com.automation.framework.utilities.CSVUtilities;
import com.automation.framework.utilities.TableFormatter;
import com.automation.framework.utilities.spreadsheets.BasicTableUtility;
import com.automation.framework.utilities.spreadsheets.ExcelTableReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class QueryService {

    // this method will be used to fetch data from various file systems
    // and convert them into datatable string for the purpose of cucumber feature parser
    // data need to be inserted in the form of Query Object
    public String evaluateQuery(Query query){
        log.info("evaluating query : " + query.getName());
        String type = query.getType();
        if (StringUtils.isBlank(query.getType())) {
            throw new IllegalArgumentException("please provide type for the query : " + query.getName());
        }
        if (type.equals("table")||type.equals("raw-table")) {
            List<List<String>> content = getTableContent(query);
            return getDataTableString(content,query);
        } else if (type.equals("database")) {

        } else if (type.equals("json")) {

        } else if (type.equals("xml")) {

        } else {
            throw new IllegalArgumentException("given type : " + type + " is not supported");
        }
        return null;
    }

    public List<List<String>> getTableContent(Query query){
        String file = query.getFile();
        if (StringUtils.isBlank(file)) {
            throw new IllegalArgumentException("file is not provided in the query : " + query.getName() + " for the " +
                    "type : " + query.getType());
        }
        if (file.endsWith(".csv")) {
            return getContentFromCSV(query);
        }
        else if (file.endsWith(".xlsx") || file.endsWith(".xls")) {
            Table table = query.getTable();
            if (Objects.isNull(table)) {
                throw new IllegalArgumentException("file is not provided in the query : " + query.getName() + " for the " +
                        "type : " + query.getType());
            }
            if (query.getType().equalsIgnoreCase("raw-table")) {
                return getRawTableContentFromExcel(query);
            } else {
                return getTableContentFromExcel(query);
            }
        }
        else if (file.endsWith(".table")) {
            // TODO: 3/20/2022 need to create parser for table files
            return null;
        }else{
            throw new IllegalStateException("given file : "+query.getFile()+" is not supported!");
        }
    }

    public List<List<String>> getRawTableContentFromExcel(Query query) {
        String file = query.getFile();
        Table table = query.getTable();
        if (StringUtils.isBlank(table.getSheet())) {
            throw new IllegalStateException("sheet value is null or empty in query : " + query.getName());
        }
        String sheet = table.getSheet();
        List<List<String>> content = BasicTableUtility.getSheetContent(file, sheet);
        return filterUsingFetchAndIndex(content, query);
    }

    public List<List<String>> getTableContentFromExcel(Query query) {
        String file = query.getFile();
        Table table = query.getTable();
        if (StringUtils.isBlank(table.getSheet())) {
            throw new IllegalStateException("sheet value is null or empty in query : " + query.getName());
        }
        String sheet = table.getSheet();
        List<List<String>> content;
        boolean findTable = false;
        if (!StringUtils.isBlank(table.getTableName())) {
            findTable = true;
        }
        if (findTable) {
            if (table.getKeys().isEmpty()) {
                content = ExcelTableReader.getTable(file, sheet, table.getTableName());
            } else {
                content = ExcelTableReader.getTableDataUsingKey(file, sheet, table.getTableName(), table.getKeys());
            }
        } else {
            content = ExcelTableReader.getTableDataFromSheet(file, sheet);
        }
        content = filterUsingFetchAndIndex(content, query);
        return content;
    }

    public List<List<String>> getContentFromCSV(Query query){
        List<List<String>> content = new ArrayList<>();
        try {
            content = CSVUtilities.readAll(new FileReader(query.getFile()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        content = filterUsingFetchAndIndex(content, query);
        return content;
    }

    public List<List<String>> filterUsingFetchAndIndex(List<List<String>> data, Query query) {
        List<List<String>> results = data;
        if (Objects.nonNull(query.getRange())) {
            Range range = query.getRange();
            int start = range.getStart() == null ? 0 : range.getStart();
            int end = range.getStart() == null ? data.size() : range.getEnd();
            results = ExcelTableReader.filterByRange(data, start, end);
        }
        if (!query.getFetch().isEmpty()) {
            results = ExcelTableReader.filterByIndex(results, query.getFetch());
        }
        return results;
    }

    public String getDataTableString(List<List<String>> content,Query query){
        String orientation = "horizontal";
        if(Objects.nonNull(query.getOrientation())){
            orientation = query.getOrientation();
        }
        if(orientation.equalsIgnoreCase("horizontal")){
            return TableFormatter.formatAsTable(content);
        }else{
            return TableFormatter.formatAsVerticalTable(content);
        }
    }

}
