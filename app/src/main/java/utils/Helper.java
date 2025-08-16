package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Helper {

    public static String setDateFormate(String dateList){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMMM, yyyy");
//        String dateList = dateFormat.format(calendar.getTime());

       return   dateFormat.format(dateList);

    }

}
