package me.sait.mobarena.extension.utils;

import java.util.List;

public class CommonUtils {
    public static boolean isEmptyList(List<? extends Object> list) {
        if (list == null || list.size() < 1) {
            return true;
        }
        return false;
    }
}
