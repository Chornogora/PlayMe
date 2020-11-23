package com.dataart.playme.util;

import com.dataart.playme.exception.ApplicationRuntimeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    public static Date getDateFromString(String dateAsString) {
        String datePattern = Constants.get(Constants.DATE_PATTERN_ID);
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        try {
            return dateFormat.parse(dateAsString);
        } catch (ParseException e) {
            throw new ApplicationRuntimeException("Cannot parse date", e);
        }
    }

    public static String dateToString(Date date) {
        String datePattern = Constants.get(Constants.DATE_PATTERN_ID);
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        return dateFormat.format(date);
    }
}
