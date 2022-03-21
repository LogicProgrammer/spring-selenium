package com.automation.framework.utilities;

import com.opencsv.CSVReader;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVUtilities {

    public static List<List<String>> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<List<String>> list = new ArrayList<>();
        csvReader.readAll().forEach(array -> {
            list.add(Arrays.asList(array));
        });
        reader.close();
        csvReader.close();
        return list;
    }

}
