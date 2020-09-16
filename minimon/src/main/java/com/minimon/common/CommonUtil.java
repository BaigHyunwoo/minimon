package com.minimon.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CommonUtil {

    static long convertUTCtoGMT(Double input) {
        String format = "yyyy/MM/dd HH:mm:ss.SSS";

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        String retime = sdf.format(input * 1000);

        String[] str = retime.split("\\.");

        Date date = new Date(str[0]);

        long mills = date.getTime();

        mills = mills + Long.parseLong(str[1]);

        return mills;
    }


    static String datetimeFormat(Date input) {
        String format = "yyyy/MM/dd HH:mm:ss.SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(input);
    }


    public static Map<String, String> jsonStringToMap(String inputString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> result = null;

        try {
            result = mapper.readValue(inputString, new TypeReference<Map<String, String>>() {
            });

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }

    public static double getPerData(double data, int per, int type) {
        if (type == 1) {
            return data + (data / 100 * per);
        } else {
            return data - (data / 100 * per);
        }

    }

    public static long getByteLength(String str) {

        long strLength = 0;
        char tempChar[] = new char[str.length()];
        for (int i = 0; i < tempChar.length; i++) {

            tempChar[i] = str.charAt(i);
            if (tempChar[i] < 128) {
                strLength++;
            } else {
                strLength += 2;
            }
        }
        return strLength;
    }

}
