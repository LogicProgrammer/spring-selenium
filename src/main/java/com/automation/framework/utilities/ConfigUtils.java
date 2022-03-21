package com.automation.framework.utilities;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigUtils {

    private Configuration configuration;

    public ConfigUtils(){};

    public ConfigUtils(String file){
        load(file);
    };

    public void load(String file) {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<?> builder;
        if(file.endsWith(".properties")){
            builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(params.properties()
                            .setFileName(file));
        }else if(file.endsWith(".yaml")||file.endsWith("yml")){
            builder = new FileBasedConfigurationBuilder<>(YAMLConfiguration.class)
                    .configure(params.properties()
                            .setFileName(file));
        }else if(file.endsWith(".ini")){
            builder = new FileBasedConfigurationBuilder<>(INIConfiguration.class)
                    .configure(params.properties()
                            .setFileName(file));
        }else{
            throw new IllegalArgumentException("given file type is not supported");
        }
        assert builder != null;
        builder.setAutoSave(true);
        try {
            configuration = builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        return configuration.getString(key);
    }

    public String getDecodedValue(String key){
        String value = configuration.getString(key);
        return Base64.isBase64(value)? new String(Base64.decodeBase64(value)):value;
    }

    public String getValue(String key,String defaultValue) {
        return configuration.getString(key,defaultValue);
    }

    public <T> T getValue(String key, Class<T> t) {
        return configuration.get(t, key);
    }

    public String[] getValues(String key){
        return configuration.getStringArray(key);
    }

    public void saveValue(String key, String value) {
        configuration.addProperty(key, value);
    }

}
