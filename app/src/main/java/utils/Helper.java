package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    public static String setDateFormate(Date date){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd, MMMM, yyyy");
       return   dateFormat.format(date);

    }

}
