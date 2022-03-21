package com.automation.data;

import com.automation.framework.utilities.TableFormatter;
import com.automation.framework.utilities.spreadsheets.BasicTableUtility;
import com.automation.framework.utilities.spreadsheets.ExcelTableReader;
import com.automation.framework.utilities.spreadsheets.SpreadSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.*;

@SpringBootTest
public class ExcelTableTest extends AbstractTestNGSpringContextTests {

    @Test
    public void excelTableTest(){
        String file = "src/test/resources/data/tables.xlsx";
        String sheet = "Sheet1";
        List<String> keys = new ArrayList<>(){{
            add("James");
            add("Peter");
            add("Huggs");
        }};
        String table1 =TableFormatter.formatAsTable(ExcelTableReader.getTable(file,sheet,"persons"));
        String table2 =TableFormatter.formatAsTable(ExcelTableReader.getTableDataUsingKey(file,sheet,"MarksList",keys));
        String table3 =TableFormatter.formatAsTable(ExcelTableReader.getTableDataUsingIndex(file,sheet,"MarksList",5));
        String table4 =TableFormatter
                .formatAsTable(ExcelTableReader.getTableDataUsingRange(file,sheet,"MarksList",1,6));
        String table5 = TableFormatter.formatAsTable(ExcelTableReader.getTableDataFromSheet(file,"Sheet2"));
        String table6 = TableFormatter.formatAsTable(BasicTableUtility.getSheetContent(file,"Sheet3"));
        System.out.println(table1);
        System.out.println(table2);
        System.out.println(table3);
        System.out.println(table4);
        System.out.println(table5);
        System.out.println(table6);
    }

    @Test
    public void printExcelTables() {
        Workbook book = SpreadSheet.getWorkbook("src/test/resources/data/tables.xlsx");
        int sheetCount = book.getNumberOfSheets();
        for (int sheetIdx = 0; sheetIdx < sheetCount; sheetIdx++) {
            XSSFSheet sheet = (XSSFSheet) book.getSheetAt(sheetIdx);
            List<XSSFTable> tables = sheet.getTables();
            for (XSSFTable t : tables) {
                List<List<String>> table = new ArrayList<>();
                System.out.println("table name = "+t.getDisplayName());
                System.out.println("total number of columns = "+t.getColumnCount());
                System.out.println("total number of rows = "+t.getDataRowCount());
                int startRow = t.getStartCellReference().getRow();
                int endRow = t.getEndCellReference().getRow();
                System.out.println("startRow = " + startRow);
                System.out.println("endRow = " + endRow);

                int startColumn = t.getStartCellReference().getCol();
                int endColumn = t.getEndCellReference().getCol();

                System.out.println("startColumn = " + startColumn);
                System.out.println("endColumn = " + endColumn);
                for (int i = startRow; i <= endRow; i++) {
                    List<String> rowData = new ArrayList<>();
                    for (int j = startColumn; j <= endColumn; j++) {
                        XSSFCell cell = sheet.getRow(i).getCell(j);
                        String cellVal = "";
                        if (cell != null) {
                            cellVal = cell.getStringCellValue();
                        }
                        rowData.add(cellVal);
                    }
                    table.add(rowData);
                }
                String tableString = TableFormatter.formatAsTable(table);
                System.out.println(tableString);
            }
        }
    }

}
