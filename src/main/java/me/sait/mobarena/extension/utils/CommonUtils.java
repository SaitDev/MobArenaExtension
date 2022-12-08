package me.sait.mobarena.extension.utils;

import java.util.List;

public class CommonUtils {
    public static boolean isEmptyList(List<?> list) {
        if (list == null || list.size() < 1) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmptyList(List<?> list) {
        return !isEmptyList(list);
    }
}
