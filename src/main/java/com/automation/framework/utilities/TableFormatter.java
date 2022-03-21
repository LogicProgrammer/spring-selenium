package com.automation.framework.utilities;

import java.util.List;


public class TableFormatter {

    public static String formatAsTable(List<List<String>> rows) {
        int[] maxLengths = new int[rows.get(0).size()];
        rows.forEach(r -> {
            for (int i = 0; i < r.size(); i++) {
                maxLengths[i] = Math.max(maxLengths[i], r.get(i).length());
            }
        });
        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths) {
            formatBuilder.append("%-").append(maxLength + 2).append("s").append("|");
        }
        String format = formatBuilder.toString();
        StringBuilder result = new StringBuilder();
        for (List<String> row : rows) {
            result.append("|").append(String.format(format, row.toArray())).append("\n");
        }
        return result.toString();
    }

    public static String formatAsVerticalTable(List<List<String>> rows) {
        StringBuilder tableString = new StringBuilder();
        List<String> headerList = rows.get(0);
        for (int count = 0; count < headerList.size(); count++) {
            StringBuilder rowString = new StringBuilder("| ");
            for (List<String> row : rows) {
                rowString.append(row.get(count)).append(" |");
            }
            tableString.append(rowString).append("\n");
        }
        return tableString.toString();
    }

}
