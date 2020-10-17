package com.isoftzone.yoappstore.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class DateUtils {

    private static SimpleDateFormat simpleDateFormat;
    private static Date date;

    public static Date dateStringFromDate(String str) {
        String dateString = "";
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = simpleDateFormat.parse(str);
            //  dateString = stringFromDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String stringFromDate(Date date) {
        String dateTime = "";
// 2017-08-31T18:30:00.000Z
        // 2017-09-26T10:35:17.749Z
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = simpleDateFormat.format(date);

        return dateTime;
    }


    public static String stringFromDateNew(Date date) {
        String dateTime = "";

        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        dateTime = simpleDateFormat.format(date);

        return dateTime;
    }

    public static String modifyDateLayout(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("MMM dd , yyyy");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String modifyDateLayoutOnlyDate(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("MMM dd ,yyyy");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static String getPreviousMonthDate() {
        int monthintegValue = Integer.parseInt(stringOnlyMonth(Calendar.getInstance().getTime()));
        int yearIntegValue = Integer.parseInt(stringOnlyYear(Calendar.getInstance().getTime()));
        Calendar calendar = Calendar.getInstance();
        // passing month-1 because 0-->jan, 1-->feb... 11-->dec
        calendar.set(yearIntegValue, monthintegValue - 2, 1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        Date date = calendar.getTime();
        DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        return DATE_FORMAT.format(date);
    }

    public static String stringOnlyMonth(Date date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("MM");
        dateTime = simpleDateFormat.format(date);
        return dateTime;
    }

    public static String stringOnlyYear(Date date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("yyyy");
        dateTime = simpleDateFormat.format(date);
        return dateTime;
    }

    public static String newDateLayout(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("MMM dd , yyyy HH:mm a");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static String newMainDateLayout(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static String dateFormate(String inputDate) {
        String dateString = "";
        try {

            // 2020-07-02 13:41:50

            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String newMainDate(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static String dateLayoutMonthFirst(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("MMM dd , yyyy HH:mm aaa");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String dateScanHistory(String inputDate) {
        String dateString = "";
        try {

            // 8/8/2017 8:24:18 AM

            simpleDateFormat = new SimpleDateFormat("d/M/yyyy HH:mm:ss a");
            SimpleDateFormat output = new SimpleDateFormat("EE, d/M/yy");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static String newOnlyTimeLayout(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("HH:mm a");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static String newOnlyDateLayout(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String newMDDateLayout(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yy");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static String getDayNameWithTime(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            date = simpleDateFormat.parse(inputDate);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE HH:mm a");
            dateString = outFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static String newDateLayoutStyle(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("MMM , dd yyyy");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String newDateMDYStyle(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static Date ymdDate(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            //    SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
            date = simpleDateFormat.parse(inputDate);
            //  dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    public static String newFormateMDYStyle(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static String newDateMDYLayoutStyle(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("MMM , dd yyyy");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String fullDateStyle(String inputDate) {
        String dateString = "";
        try {
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static String getDateRange() {
        Date begining, end;

        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        begining = calendar.getTime();

        Calendar calendar1 = getCalendarForNow();
        calendar1.set(Calendar.DAY_OF_MONTH,
                calendar1.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(calendar1);
        end = calendar1.getTime();

    /*    Log.e("begining=", "==" + DateUtils.stringFromDate(begining));
        Log.e("endend=", "==" + DateUtils.stringFromDate(end));*/
        return DateUtils.stringFromDate(begining);
    }


    public static String getLastThreeMonthOFCurrentMonth() {

        Calendar aCalendar = Calendar.getInstance();
// add -1 month to current month
        aCalendar.add(Calendar.MONTH, -3);
// set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);

        return DateUtils.stringFromDateNewFormate(aCalendar.getTime());
    }


    public static Long getLastSixMonthOFCurrentMonth() {

        Calendar aCalendar = Calendar.getInstance();
// add -1 month to current month
        aCalendar.add(Calendar.MONTH, -6);
// set DATE to 1, so first date of previous month

        aCalendar.set(Calendar.DATE, Calendar.getInstance().get(Calendar.DATE));

        return aCalendar.getTime().getTime();
    }

    public static String getLastTwoYearOFCurrentMonth() {

        Calendar aCalendar = Calendar.getInstance();
// add -1 month to current month
        aCalendar.add(Calendar.YEAR, -2);
// set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);

        return DateUtils.stringFromDateNewFormate(aCalendar.getTime());
    }

    public static String getLastOneYearOFCurrentMonth() {

        Calendar aCalendar = Calendar.getInstance();
// add -1 month to current month
        aCalendar.add(Calendar.YEAR, -1);
// set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);

        return DateUtils.stringFromDateNewFormate(aCalendar.getTime());
    }


    public static String getLastYearFromSameDate() {

        Calendar aCalendar = Calendar.getInstance();
// add -1 month to current month
        aCalendar.add(Calendar.MONTH, -12);
// set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);

        return DateUtils.stringFromDateMMDDYYY(aCalendar.getTime());
    }


    public static String stringFromDateMMDDYYY(Date date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateTime = simpleDateFormat.format(date);

        return dateTime;
    }


    public static String stringFromDateDDMMYYY(Date date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("MMM , dd yyyy hh:mm a");
        dateTime = simpleDateFormat.format(date);

        return dateTime;
    }


    public static String getCurrentDateTimeMMDDYYYY() {

        Date currentTime = Calendar.getInstance().getTime();
        return DateUtils.stringFromDateMMDDYYY(currentTime);
    }


    public static String getCurrentDateTimeDDMMYYYY() {

        Date currentTime = Calendar.getInstance().getTime();
        return DateUtils.stringFromDateDDMMYYY(currentTime);
    }


    public static String getOneWeekFormToday() {
        String dateTime = "";
        try {
            String sDate = DateUtils.getCurrentDateTimeMMDDYYYY();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            Date date = dateFormat.parse(sDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -7);
            dateTime = dateFormat.format(calendar.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateTime;
    }


    public static String getLast30DaysFromToday() {
        String dateTime = "";
        try {
            String sDate = DateUtils.getCurrentDateTimeMMDDYYYY();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            Date date = dateFormat.parse(sDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -30);
            dateTime = dateFormat.format(calendar.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateTime;
    }


    public static String getYesterday() {
        String dateTime = "";
        try {
            String sDate = DateUtils.getCurrentDateTimeMMDDYYYY();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            Date date = dateFormat.parse(sDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            dateTime = dateFormat.format(calendar.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateTime;
    }


    public static String getOneWeekFormTodaydmy() {
        String dateTime = "";
        try {
            String sDate = DateUtils.getCurrentDateTimeMMDDYYYY();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = dateFormat.parse(sDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -7);
            dateTime = dateFormat.format(calendar.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static String getLastThreeMonthOFCurrentMonthYDMT() {

        Calendar aCalendar = Calendar.getInstance();
// add -1 month to current month
        aCalendar.add(Calendar.MONTH, -9);
// set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);

        return DateUtils.stringFromDateYMDTT(aCalendar.getTime());
    }


    public static String getFirstDateOfCurrentMonthMDY() {

        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(c.getTime());

        return DateUtils.stringFromDateMMDDYYY(c.getTime());
    }


    public static String getLastDateOFPreviousMonth() {

        Calendar aCalendar = Calendar.getInstance();
// add -1 month to current month
        aCalendar.add(Calendar.MONTH, -1);
// set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);

        Date firstDateOfPreviousMonth = aCalendar.getTime();

// set actual maximum date of previous month
        aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//read it
        Date lastDateOfPreviousMonth = aCalendar.getTime();

        return DateUtils.stringFromDateMMDDYYY(lastDateOfPreviousMonth);
    }


    public static String getFirstDateOFPreviousMonth() {

        Calendar aCalendar = Calendar.getInstance();
// add -1 month to current month
        aCalendar.add(Calendar.MONTH, -1);
// set DATE to 1, so first date of previous month
        aCalendar.set(Calendar.DATE, 1);

        Date firstDateOfPreviousMonth = aCalendar.getTime();

// set actual maximum date of previous month
        aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//read it
        Date lastDateOfPreviousMonth = aCalendar.getTime();

        return DateUtils.stringFromDateMMDDYYY(firstDateOfPreviousMonth);
    }

    public static String getCurrentDateTime() {

        Date currentTime = Calendar.getInstance().getTime();
        return DateUtils.stringFromDate(currentTime);
    }

    public static String getCurrentDateTimeNewFormate() {

        Date currentTime = Calendar.getInstance().getTime();
        return DateUtils.stringFromDateNewFormate(currentTime);
    }

    public static String getCurrentDateYDMT() {

        Date currentTime = Calendar.getInstance().getTime();
        return DateUtils.stringFromDateYMD(currentTime);
    }

    public static String getCurrenTime() {

        Date currentTime = Calendar.getInstance().getTime();
        return DateUtils.stringTime(currentTime);
    }


    public static String stringTime(Date date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("HH");
        dateTime = simpleDateFormat.format(date);

        return dateTime;
    }


    public static String getCurrenTimeHhmma() {

        Date currentTime = Calendar.getInstance().getTime();
        return DateUtils.stringTimeNewFormate(currentTime);
    }

    public static String stringTimeNewFormate(Date date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        dateTime = simpleDateFormat.format(date);

        return dateTime;
    }

    public static String stringFromDateYMD(Date date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTime = simpleDateFormat.format(date);

        return dateTime;
    }

    public static String stringFromDateYMDTT(Date date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateTime = simpleDateFormat.format(date);

        return dateTime;
    }


    public static String stringFromDateNewFormate(Date date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateTime = simpleDateFormat.format(date);

        return dateTime;
    }

    public static Date stringFromDateReturn(String date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //  dateTime = simpleDateFormat.format(date);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }


    public static String getCloseTime(String inputDate) {
        String dateString = "";
        try { // 2016-06-01T07:00:00.000Z
            simpleDateFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat output = new SimpleDateFormat("HH");
            date = simpleDateFormat.parse(inputDate);
            dateString = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }


    public static Date stringDateReturn(String date) {
        String dateTime = "";
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //  dateTime = simpleDateFormat.format(date);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }


}
