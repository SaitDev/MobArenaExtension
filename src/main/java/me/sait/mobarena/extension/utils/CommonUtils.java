package me.sait.mobarena.extension.utils;

import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;

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

    public static void ignoreException(Runnable function) {
        try {
            if (function != null) {
                function.run();
            }
        } catch (Throwable e) {
            LogHelper.log(e.getMessage(), LogLevel.WARNING, e);
        }
    }
}
