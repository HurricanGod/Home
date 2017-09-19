package cn.hurrican.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NewObject on 2017/9/15.
 */
public class DateTimeFormatUtil {
    private static String[] regex;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    public static SimpleDateFormat simpleDateFormat;
    static {
        regex = new String[]{
                "^\\d{4}-\\d{1,2}-\\d{1,2}$",
                "^\\d{4}/\\d{1,2}/\\d{1,2}$",
                "^\\d{4}\\.\\d{1,2}\\.\\d{1,2}$",
                "^\\d{4}年\\d{1,2}月\\d{1,2}日$"
        };

        simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    }


    public static boolean checkDateTimeFormat(String seq) {
        if (seq == null) {
            return false;
        }
        for (int i = 0; i < regex.length; i++) {
            boolean res = Pattern.matches(regex[i], seq);
            if (res) {
                return true;
            }
        }
        return false;
    }

    public static String formatDateTimeString(String datetimeString){
        datetimeString = datetimeString.replace(" ", "");
        String standard = "^(\\d{4})-(\\d{1,2})-(\\d{1,2})$";
        Pattern pattern = Pattern.compile("(\\d{1,4})");
        for (int i = 0; i < regex.length; i++) {
            boolean res = Pattern.matches(regex[i], datetimeString);
            if (res) {
                if (Pattern.matches(standard, datetimeString)) {
                    return datetimeString;
                } else {
                    Matcher matcher = pattern.matcher(datetimeString);
                    String formatString = "";

                    while (matcher.find()){
                        String s = matcher.group();
                        formatString = formatString + s + "-";
                    }
                    return formatString.substring(0, formatString.length()-1);
                }
            }
        }
        return null;
    }


    public static String getFormatDateString(Date date){
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
        String s = pattern.format(date);
        return s;
    }


    public static Date parseStringToDate(String dateString) throws ParseException {
        if (dateString == null) {
            throw new  NullPointerException();
        }
        return simpleDateFormat.parse(dateString);
    }



}
