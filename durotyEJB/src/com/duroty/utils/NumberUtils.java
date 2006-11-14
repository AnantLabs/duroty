/**
 *
 */
package com.duroty.utils;

import java.text.DecimalFormat;
import java.util.Date;


/**
 * @author durot
 *
 */
public class NumberUtils {
    /**
     * DOCUMENT ME!
     */
    private static final DecimalFormat formatter = new DecimalFormat(
            "000000000000000");

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String pad(long n) {
        return formatter.format(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        long n = Long.valueOf(pad(new Date().getTime())).longValue();
        System.out.println(n);
    }
}
