package lv.team3.botcovidlab.adapter.facebook;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtility {

    public static String getYesterdayDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    public static String getSevenDaysAgoDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -8);
        return dateFormat.format(cal.getTime());
    }

    public static String getThirtyDaysAgoDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -31);
        return dateFormat.format(cal.getTime());
    }

}
