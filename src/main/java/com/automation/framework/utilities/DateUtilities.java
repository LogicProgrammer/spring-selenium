package com.automation.framework.utilities;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class DateUtilities {

    static DateTimeFormatter DEFAULT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    static String DEFAULT_ZONE = "America/Anchorage";

    public static String getZonedDate(String zone) {
        return ZonedDateTime.now(ZoneId.of(zone)).format(DEFAULT_FORMAT);
    }

    public static String getZonedDate() {
        return ZonedDateTime.now(ZoneId.of(DEFAULT_ZONE)).format(DEFAULT_FORMAT);
    }

    public static String getZonedDateWithPattern(String pattern) {
        return ZonedDateTime.now(ZoneId.of(DEFAULT_ZONE)).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getZonedDate(String zone, String pattern) {
        return ZonedDateTime.now(ZoneId.of(zone)).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentDate() {
        return LocalDateTime.now().format(DEFAULT_FORMAT);
    }

    public static String getCurrentDate(Locale locale) {
        return LocalDateTime.now().format(DEFAULT_FORMAT.withLocale(locale));
    }

    public static String getCurrentDate(DateTimeFormatter formatter) {
        return LocalDateTime.now().format(formatter);
    }

    public static String getCurrentDate(String pattern) {
        return getCurrentDate(pattern, Locale.getDefault());
    }

    public static String getCurrentDate(String pattern, Locale locale) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern, locale));
    }

    public static String changePattern(String date, String actualPattern, String expectedPattern) {
        LocalDateTime dateTime = LocalDate.parse(date, DateTimeFormatter.ofPattern(actualPattern))
                .atStartOfDay();
        return dateTime.format(DateTimeFormatter.ofPattern(expectedPattern));
    }

    public static String changePattern(String date, String expectedPattern) {
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return dateTime.format(DateTimeFormatter.ofPattern(expectedPattern));
    }

    public static String changeDate(String dateString, String currentPattern, String expectedPattern,
                                    int change, String option) {
        LocalDateTime dateTime = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(currentPattern))
                .atStartOfDay();
        int abs = Math.abs(change);
        switch (option) {
            case "day":
                dateTime = change > 0 ? dateTime.plusDays(abs) : dateTime.minusDays(abs);
                break;
            case "month":
                dateTime = change > 0 ? dateTime.plusMonths(abs) : dateTime.minusMonths(abs);
                break;
            case "year":
                dateTime = change > 0 ? dateTime.plusYears(abs) : dateTime.minusYears(abs);
                break;
            default:
                throw new IllegalArgumentException("given option " + option + " is not a valid option");
        }
        return dateTime.format(DateTimeFormatter.ofPattern(expectedPattern));
    }

    public static String changeDate(String dateString, int change, String option) {
        LocalDateTime dateTime = LocalDateTime.parse(dateString);
        int abs = Math.abs(change);
        switch (option) {
            case "day":
                dateTime = change > 0 ? dateTime.plusDays(abs) : dateTime.minusDays(abs);
                break;
            case "month":
                dateTime = change > 0 ? dateTime.plusMonths(abs) : dateTime.minusMonths(abs);
                break;
            case "year":
                dateTime = change > 0 ? dateTime.plusYears(abs) : dateTime.minusYears(abs);
                break;
            default: throw new IllegalArgumentException("given option " + option + " is not a valid option");
        }
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static String changeDate(String dateString, String expectedPattern, int change, String option) {
        LocalDateTime dateTime = LocalDateTime.parse(dateString);
        int abs = Math.abs(change);
        switch (option) {
            case "day":
                dateTime = change > 0 ? dateTime.plusDays(abs) : dateTime.minusDays(abs);
                break;
            case "month":
                dateTime = change > 0 ? dateTime.plusMonths(abs) : dateTime.minusMonths(abs);
                break;
            case "year":
                dateTime = change > 0 ? dateTime.plusYears(abs) : dateTime.minusYears(abs);
                break;
            default:
                throw new IllegalArgumentException("given option " + option + " is not a valid option");
        }
        return dateTime.format(DateTimeFormatter.ofPattern(expectedPattern));
    }

    public static String changeZone(String date, String pattern, String zone) {
        try {
            return ZonedDateTime.parse(date, DateTimeFormatter.ofPattern(pattern))
                    .withZoneSameLocal(ZoneId.of(zone)).format(DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
            return localDate.atStartOfDay(ZoneId.of(zone)).format(DateTimeFormatter.ofPattern(pattern));
        }
    }

    public static String changeZone(String date, String zone) {
        return ZonedDateTime.parse(date, DEFAULT_FORMAT)
                .withZoneSameLocal(ZoneId.of(zone)).toLocalDateTime().format(DEFAULT_FORMAT);
    }

    /**
     * following the pattern of date exp string
     *
     * @param exp : exp for provide date options
     * @return date string
     */
    public static String evalDate(String exp) {
        DateOptions dateOptions = getDateOptions(exp);
        String date = "";
        if (Objects.nonNull(dateOptions.getDate())) {
            date = dateOptions.getDate();
            if (Objects.nonNull(dateOptions.getZone())) {
                date = Objects.nonNull(dateOptions.getPattern())
                        ? changeZone(date, dateOptions.getPattern(), dateOptions.getZone())
                        : changeZone(date, dateOptions.getZone());
            }
        } else {
            if (Objects.nonNull(dateOptions.getZone()))
                date = dateOptions.getPattern() != null
                        ? getZonedDate(dateOptions.getZone(), dateOptions.getPattern())
                        : getZonedDate(dateOptions.getZone());
            else
                date = dateOptions.getPattern() != null
                        ? getCurrentDate(dateOptions.getPattern())
                        : getCurrentDate();
        }
        if (Objects.nonNull(dateOptions.getExpectedPattern())) {
            if (Objects.nonNull(dateOptions.getPattern()))
                date = changePattern(date, dateOptions.getPattern(), dateOptions.getExpectedPattern());
            else
                date = changePattern(date, dateOptions.getExpectedPattern());
        }
        if (Objects.nonNull(dateOptions.getChange())) {
            String[] changeArgs = StringUtils.split(dateOptions.getChange(), ' ');
            int change = NumberUtils.toInt(changeArgs[0]);
            String changeOption = changeArgs[1].strip();
            if (Objects.isNull(dateOptions.getExpectedPattern()))
                date = changeDate(date, change, changeOption);
            else
                date = changeDate(date, dateOptions.getExpectedPattern(), dateOptions.getExpectedPattern(),
                        change, changeOption);
        }
        return date;
    }

    @Data
    @ToString
    static final class DateOptions {
        String pattern;
        String date;
        String expectedPattern;
        String change;
        String zone;
    }

    public static DateOptions getDateOptions(String exp) {
        String[] args = StringUtils.split(exp, ';');
        DateOptions dateOptions = new DateOptions();
        Arrays.stream(args).forEach(arg -> {
            String[] vars = StringUtils.split(arg, '=');
            switch (vars[0].strip()) {
                case "pattern":
                    dateOptions.setPattern(vars[1].trim());
                    break;
                case "date":
                    dateOptions.setDate(vars[1].strip());
                    break;
                case "expectedPattern":
                    dateOptions.setExpectedPattern(vars[1].strip());
                    break;
                case "change":
                    dateOptions.setChange(vars[1].strip());
                    break;
                case "zone":
                    dateOptions.setZone(vars[1].strip());
                    break;
            }
        });
        return dateOptions;
    }

}
