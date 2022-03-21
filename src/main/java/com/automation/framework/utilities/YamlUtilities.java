package com.automation.framework.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class YamlUtilities {

    public static <T> T loadAs(String file, Class<T> clazz) {
        try {
            InputStream stream = new FileInputStream(file);
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            return om.readValue(stream, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
