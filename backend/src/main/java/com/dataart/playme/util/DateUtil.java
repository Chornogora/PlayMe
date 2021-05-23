package com.dataart.playme.util;

import com.dataart.playme.exception.ApplicationRuntimeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    public static Date getDateFromString(String dateAsString) {
        String defaultPattern = Constants.DEFAULT_DATETIME_FORMAT;
        return getDateFromString(dateAsString, defaultPattern);
    }

    public static Date getDateFromString(String dateAsString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(dateAsString);
        } catch (ParseException e) {
            throw new ApplicationRuntimeException("Cannot parse date", e);
        }
    }

    public static String dateToString(Date date) {
        String defaultPattern = Constants.DEFAULT_DATETIME_FORMAT;
        SimpleDateFormat dateFormat = new SimpleDateFormat(defaultPattern);
        return dateFormat.format(date);
    }
}
