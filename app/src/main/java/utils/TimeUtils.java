package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TimeUtils {
    public static final String YM = "yyyy-MM";
    public static final String YMD = "yyyy-MM-dd";
    public static final String YMDHM = "yyyy-MM-dd HH:mm";
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String MD_CN = "MM月dd日";
    public static final String YMDHM_2 = "yyyy.MM.dd HH:mm";
    public static final String YMD_CN = "yyyy年MM月dd日";
    public static final String YM_CN = "yyyy年MM月";

    public static String formatDate(long date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getWenhou() {
        long cu = System.currentTimeMillis();
        int hour = Integer.parseInt(getCurrentHour(cu / 1000));
        if (hour > 5 && hour < 10) {
            return "早上";
        } else if (hour > 8 && hour < 13) {
            return "上午";
        } else if (hour > 11 && hour < 15) {
            return "中午";
        } else if (hour > 13 && hour < 19) {
            return "下午";
        } else if (hour > 17 && hour < 24 || hour > -1 && hour < 7) {
            return "晚上";
        }
        return "";
    }

    /**
     * 得到时间戳里的时间-小时
     *
     * @param dateline
     * @return
     */
    public static String getCurrentHour(long dateline) {
        Calendar date = Calendar.getInstance();
        date.setTime(new Date(dateline * (long) 1000));
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("HH", Locale.getDefault());

        return sdf.format(date.getTime());
    }

    public static String getUnite(int repurchaseFreqUnit) {
        if (repurchaseFreqUnit == 1) {
            return "天";
        } else if (repurchaseFreqUnit == 0) {
            return "个月";
        } else {
            return "";
        }

    }

    //毫秒换成00:00:00
    public static String getCountTimeByLong(long finishTime) {
        int totalTime = (int) (finishTime / 1000);//秒
        int hour = 0, minute = 0, second = 0;

        if (3600 <= totalTime) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        if (60 <= totalTime) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        if (0 <= totalTime) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();

        if (hour < 10) {
            sb.append("0").append(hour).append(":");
        } else {
            sb.append(hour).append(":");
        }
        if (minute < 10) {
            sb.append("0").append(minute).append(":");
        } else {
            sb.append(hour).append(":");
        }
        if (second < 10) {
            sb.append("0").append(second);
        } else {
            sb.append(second);
        }
        return sb.toString();

    }

    public static Date previousYears(Date date, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -num);
        return cal.getTime();
    }

    public static Date nextMonth(Date date, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, num);
        return cal.getTime();
    }


    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }


    public  static String getCurrentDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
