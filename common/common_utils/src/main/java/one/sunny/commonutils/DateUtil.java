package one.sunny.commonutils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String dateToString(Date date, String format){
        SimpleDateFormat sdf =new SimpleDateFormat(format);
        String str = sdf.format(date);
        return str;
    }
    public static String dateToString(Date date){
        SimpleDateFormat sdf =new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String str = sdf.format(date);
        return str;
    }
    public static Long millsDurationToMinutes(Long mills){
        return mills / (1000 * 60);
    }
}
