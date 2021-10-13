package companyAll_MVC.utils;

import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

    public static void logger(String data,Class logClass){
        Logger.getLogger(logClass).error(data);
    }

    public static boolean isEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static List<Map<String,String>> errors(BindingResult bindingResult){

        List<Map<String,String>> ls = new LinkedList<>();

        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String fieldMessage = error.getDefaultMessage();

            Map<String,String> map = new HashMap<>();
            map.put("fieldName",fieldName);
            map.put("fieldMessage",fieldMessage);
            ls.add(map);

        });

        return ls;

    }

    //Generate random number
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    //Date Formatter
    public static String getDateFormatter(){
        String pattern = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    //Total Page Calculation
    public static int getTotalPage(int totalData,int showNumber){
        int totalPage = 0;

        if(totalData < showNumber){
            totalPage = 1;
        }else {
            if(totalData % showNumber == 0){
                totalPage = totalData / showNumber;
            }else{
                totalPage = (totalData / showNumber) + 1;
            }
        }

        return totalPage;
    }

}
