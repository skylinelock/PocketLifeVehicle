package dev.sky_lock.mocar.util;

/**
 * @author sky_lock
 */

public class StringUtil {

    public static String formatDecimal(float f) {
        return String.format("%.1f", f);
    }

    public static String removeBlanks(String blankText) {
        return blankText.replaceAll("\\s", "");
    }


}
