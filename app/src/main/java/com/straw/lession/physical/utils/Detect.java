package com.straw.lession.physical.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author xuegongjian
 * 
 */
public abstract class Detect {

    /** notEmpty */
    public static boolean notEmpty(String string) {
        return null != string && !"".equals(string);
    }

    public static boolean notEmpty(Collection<?> collection) {
        // List<Object> list = new ArrayList<Object>();
        // list.add(null);
        // System.out.println(notEmpty(list)); //true

        if (null != collection) {
            Iterator<?> iterator = collection.iterator();
            if (null != iterator) {
                while (iterator.hasNext()) {
                    if (null != iterator.next()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean notEmpty(Map<?, ?> map) {
        return null != map && !map.isEmpty();
    }

    public static boolean notEmpty(short[] array) {
        return null != array && array.length > 0;
    }

    public static boolean notEmpty(int[] array) {
        return null != array && array.length > 0;
    }

    public static boolean notEmpty(long[] array) {
        return null != array && array.length > 0;
    }

    public static <T> boolean notEmpty(T[] array) {
        return null != array && array.length > 0;
    }
}
