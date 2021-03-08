package com.minimon.common;

public class CommonUtil {

    public static double getPerData(double data, int per, int type) {
        if (type == 1) {
            return data + (data / 100 * per);
        } else {
            return data - (data / 100 * per);
        }
    }

}
