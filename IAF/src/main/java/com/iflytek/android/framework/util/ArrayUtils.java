package com.iflytek.android.framework.util;

/**
 * 
 * 数组工具类，可用于数组常用操作，如：
 * isEmpty(V[] sourceArray) 判断数组是否为空或长度为0
 * getLast(V[] sourceArray, V value, V defaultValue, boolean isCircle) 得到数组中某个元素前一个元素，isCircle表示是否循环
 * getNext(V[] sourceArray, V value, V defaultValue, boolean isCircle) 得到数组中某个元素下一个元素，isCircle表示是否循环
 * 
 * @author wzgao
 *
 */
public class ArrayUtils {

    /**
     * 数组是否为空
     * 
     * @param <V>
     * @param sourceArray
     * @return
     */
    public static <V> boolean isEmpty(V[] sourceArray) {
        return (sourceArray == null || sourceArray.length == 0);
    }

    /**
     * 得到数组中某个元素前一个元素
     * @param <V>
     * @param 原数组
     * @param 目标元素
     * @param 默认值，数组为空时返回
     * @param 是否循环
     * @return
     */
    public static <V> V getLast(V[] sourceArray, V value, V defaultValue, boolean isCircle) {
        if (isEmpty(sourceArray)) {
            return defaultValue;
        }

        int currentPosition = -1;
        for (int i = 0; i < sourceArray.length; i++) {
            if (ObjectUtils.isEquals(value, sourceArray[i])) {
                currentPosition = i;
                break;
            }
        }
        if (currentPosition == -1) {
            return defaultValue;
        }

        if (currentPosition == 0) {
            return isCircle ? sourceArray[sourceArray.length - 1] : defaultValue;
        }
        return sourceArray[currentPosition - 1];
    }

    /**
     * 得到数组中某个元素前一个元素
     * @param <V>
     * @param 原数组
     * @param 目标元素
     * @param 默认值，数组为空时返回
     * @param 是否循环
     * @return
     */
    public static <V> V getNext(V[] sourceArray, V value, V defaultValue, boolean isCircle) {
        if (isEmpty(sourceArray)) {
            return defaultValue;
        }

        int currentPosition = -1;
        for (int i = 0; i < sourceArray.length; i++) {
            if (ObjectUtils.isEquals(value, sourceArray[i])) {
                currentPosition = i;
                break;
            }
        }
        if (currentPosition == -1) {
            return defaultValue;
        }

        if (currentPosition == sourceArray.length - 1) {
            return isCircle ? sourceArray[0] : defaultValue;
        }
        return sourceArray[currentPosition + 1];
    }

    /**
     * 得到数组中某个元素前一个元素
     * @param <V>
     * @param 原数组
     * @param 目标元素
     * @param 是否循环
     * @return
     */
    public static <V> V getLast(V[] sourceArray, V value, boolean isCircle) {
        return getLast(sourceArray, value, null, isCircle);
    }

    /**
     * 得到数组中某个元素前一个元素
     * @param <V>
     * @param 原数组
     * @param 目标元素
     * @param 是否循环
     * @return
     */
    public static <V> V getNext(V[] sourceArray, V value, boolean isCircle) {
        return getNext(sourceArray, value, null, isCircle);
    }

    /**
     * 得到数组中某个元素前一个元素
     * @param <V>
     * @param 原数组
     * @param 目标元素
     * @param 默认值，数组为空时返回
     * @param 是否循环
     * @return
     */
    public static long getLast(long[] sourceArray, long value, long defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Long[] array = ObjectUtils.transformLongArray(sourceArray);
        return getLast(array, value, defaultValue, isCircle);

    }

    /**
     * 得到数组中某个元素前一个元素
     * @param <V>
     * @param 原数组
     * @param 目标元素
     * @param 默认值，数组为空时返回
     * @param 是否循环
     * @return
     */
    public static long getNext(long[] sourceArray, long value, long defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Long[] array = ObjectUtils.transformLongArray(sourceArray);
        return getNext(array, value, defaultValue, isCircle);
    }

    /**
     * 得到数组中某个元素前一个元素
     * @param <V>
     * @param 原数组
     * @param 目标元素
     * @param 默认值，数组为空时返回
     * @param 是否循环
     * @return
     */
    public static int getLast(int[] sourceArray, int value, int defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Integer[] array = ObjectUtils.transformIntArray(sourceArray);
        return getLast(array, value, defaultValue, isCircle);

    }

    /**
     * 得到数组中某个元素前一个元素
     * @param <V>
     * @param 原数组
     * @param 目标元素
     * @param 默认值，数组为空时返回
     * @param 是否循环
     * @return
     */
    public static int getNext(int[] sourceArray, int value, int defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Integer[] array = ObjectUtils.transformIntArray(sourceArray);
        return getNext(array, value, defaultValue, isCircle);
    }
}
