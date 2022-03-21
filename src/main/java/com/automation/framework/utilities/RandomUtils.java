package com.automation.framework.utilities;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Locale;

public class RandomUtils {

    public static String domain = "ztafAutomation.com";
    public static String locale = "en_US";
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";
    private static final String MIDDLE_NAME = "middle name";
    private static final String NAME_WITH_MIDDLE = "name with middle";
    private static final String EMAIL = "email";
    private static final String NUMERIC = "numeric";
    private static final String ALPHA_NUMERIC = "alpha numeric";
    private static final String ALPHABET = "alphabet";
    private static final String SPECIAL_CHARACTER = "special character";

    /**
     *  key should be in the format 'random:<type>:size'
     *  size can be only application for type : alphabet, numeric,
     *  alphanumeric, special character
     * @param key
     * @return
     */
    public static String getValue(String key){
        String[] values = StringUtils.split(key,":",3);
        String type = values[1].toLowerCase(Locale.ROOT);
        Faker faker = new Faker(new Locale(locale));
        switch (type){
            case FIRST_NAME:
            case MIDDLE_NAME:
                return faker.name().firstName();
            case LAST_NAME:
                return faker.name().lastName();
            case NAME_WITH_MIDDLE:
                return faker.name().nameWithMiddle();
            case EMAIL:
                return faker.name().firstName()+faker.number().digits(4)+"@"+domain;
            case NUMERIC:
                return RandomStringUtils.randomNumeric(NumberUtils.toInt(values[2]));
            case ALPHA_NUMERIC:
                return RandomStringUtils.randomAlphanumeric(NumberUtils.toInt(values[2]));
            case ALPHABET:
                return RandomStringUtils.randomAlphabetic(NumberUtils.toInt(values[2]));
            case SPECIAL_CHARACTER:
                String characters = "~`!@#$%^&*()-_=+[{]}\\|;:\\'\",<.>/?";
                return RandomStringUtils.random(NumberUtils.toInt(values[2]), characters);
            default:
                System.err.println("given random item " + values[1] + " is not available");
                return key;
        }
    }

    public static void setDomain(String value){
        domain = value;
    }

    public static void setLocale(String value){
        locale = value;
    }


}
