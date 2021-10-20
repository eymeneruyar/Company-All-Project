package companyAll_MVC.utils;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

    public static final String UPLOAD_DIR_PRODUCTS =  "src/main/resources/static/uploadImages/_products/";
    public static final String UPLOAD_DIR_NEWS =  "src/main/resources/static/uploadImages/_news/";
    public static final String UPLOAD_DIR_PROFILEIMAGES  = "src/main/resources/static/uploadImages/_profileImages/";
    public static final String UPLOAD_DIR_ADVERTISEMENT = "src/main/resources/images/ad_images/";

    public static long maxFileUploadSize = 5120;

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

    //Add Folder
    public static Map<Check, Object> imageUpload(MultipartFile file, String UPLOAD_DIR) {
        String errorMessage = "";
        Map<Check, Object> hm = new LinkedHashMap<>();
        if (!file.isEmpty() ) {
            long fileSizeMB = file.getSize() / 1024;
            if ( fileSizeMB > maxFileUploadSize ) {
                errorMessage = "File should be max "+ (maxFileUploadSize / 1024) +"MB!";
            }else {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String ext = fileName.substring(fileName.length()-5);
                String uui = UUID.randomUUID().toString();
                fileName = uui + ext;
                try {
                    Path path = Paths.get(UPLOAD_DIR + fileName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    hm.put(Check.result, fileName);
                } catch ( IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            errorMessage = "Please select an image!";
        }

        if ( errorMessage.equals("") ) {
            hm.put(Check.status, true);
        }else {
            hm.put(Check.status, false);
            hm.put(Check.message, errorMessage);
        }

        return hm;
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
