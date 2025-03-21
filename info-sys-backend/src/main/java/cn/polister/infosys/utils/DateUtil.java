package cn.polister.infosys.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date handleDate(Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        endDate = calendar.getTime();
        return endDate;
    }
}
