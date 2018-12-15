package net.jiaobaowang.gonggaopai.util;

/**
 * Array Utils
 * 描述：数组工具类
 * 时间：2016年9月13日
 */
public class ArrayUtils {

    private ArrayUtils() {
        throw new AssertionError();
    }

    /**
     * is null or its length is 0
     * 是否为 null 或者 长度是否为 0
     * @param <V>
     * @param sourceArray
     * @return 长度为0 或者 null return true，否则 return false
     */
    public static <V> boolean isEmpty(V[] sourceArray) {
        return (sourceArray == null || sourceArray.length == 0);
    }

    /**
     * (1.08)、在数组objects中搜索元素element
     *
     * @param objects 待操作的数组
     * @param element 待匹配的元素
     * @return 索引，如不存在，-1
     */
    public static int search(Object[] objects, Object element) {
        int e = -1;
        for (int w = 0; w < objects.length; w++) {
            if (!element.equals(objects[w])) {
                continue;
            }
            else {
                e = w;
                break;
            }
        }
        return e;
    }

    /**
     * Inteher数组转换成int数组
     */
    public static int[] integersToInts(Integer[] integers) {
        int[] ints = new int[integers.length];
        for (int w = 0; w < integers.length; w++) {
            ints[w] = integers[w];
        }
        return ints;
    }


    /**
     * 将给定的数组转换成字符串
     *
     * @param integers 给定的数组
     * @param startSymbols 开始符号
     * @param separator 分隔符
     * @param endSymbols 结束符号
     * @return 例如开始符号为"{"，分隔符为", "，结束符号为"}"，那么结果为：{1, 2, 3}
     */
    public static String toString(int[] integers, String startSymbols, String separator, String endSymbols) {
        boolean addSeparator = false;
        StringBuffer sb = new StringBuffer();
        //如果开始符号不为null且不空
        if (Validate.noNull(startSymbols)) {
            sb.append(startSymbols);
        }

        //循环所有的对象
        for (int object : integers) {
            //如果需要添加分隔符
            if (addSeparator) {
                sb.append(separator);
                addSeparator = false;
            }
            sb.append(object);
            addSeparator = true;
        }

        //如果结束符号不为null且不空
        if (Validate.noNull(endSymbols)) {
            sb.append(endSymbols);
        }
        return sb.toString();
    }


    /**
     * 将给定的数组转换成字符串
     *
     * @param integers 给定的数组
     * @param separator 分隔符
     * @return 例如分隔符为", "那么结果为：1, 2, 3
     */
    public static String toString(int[] integers, String separator) {
        return toString(integers, null, separator, null);
    }

    /**
     * long[]转称Long[]
     * convert long array to Long array
     *
     * @param source
     * @return
     */
    public static Long[] transformLongArray(long[] source) {
        Long[] destin = new Long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * Long[]转称long[]
     * convert Long array to long array
     * @param source
     * @return
     */
    public static long[] transformLongArray(Long[] source) {
        long[] destin = new long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * int[]转存Integer[]
     * convert int array to Integer array
     *
     * @param source
     * @return
     */
    public static Integer[] transformIntArray(int[] source) {
        Integer[] destin = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * Integer[]转成 int[]
     * convert Integer array to int array
     *
     * @param source
     * @return
     */
    public static int[] transformIntArray(Integer[] source) {
        int[] destin = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * 将给定的数组转换成字符串，默认分隔符为", "
     *
     * @param integers 给定的数组
     * @return 例如：1, 2, 3
     */
    public static String toString(int[] integers) {
        return toString(integers, null, ", ", null);
    }


    /**
     * 将给定的数组转换成字符串
     *
     * @param objects 给定的数组
     * @param startSymbols 开始符号
     * @param separator 分隔符
     * @param endSymbols 结束符号
     * @return 例如开始符号为"{"，分隔符为", "，结束符号为"}"，那么结果为：{1, 2, 3}
     */
    public static String toString(Object[] objects, String startSymbols, String separator, String endSymbols) {
        boolean addSeparator = false;
        StringBuffer sb = new StringBuffer();
        //如果开始符号不为null且不空
        if (Validate.noNull(startSymbols)) {
            sb.append(startSymbols);
        }

        //循环所有的对象
        for (Object object : objects) {
            //如果需要添加分隔符
            if (addSeparator) {
                sb.append(separator);
                addSeparator = false;
            }
            sb.append(object);
            addSeparator = true;
        }

        //如果结束符号不为null且不空
        if (Validate.noNull(endSymbols)) {
            sb.append(endSymbols);
        }
        return sb.toString();
    }


    /**
     * 将给定的数组转换成字符串
     *
     * @param objects 给定的数组
     * @param separator 分隔符
     * @return 例如分隔符为", "那么结果为：1, 2, 3
     */
    public static String toString(Object[] objects, String separator) {
        return toString(objects, null, separator, null);
    }


    /**
     * 将给定的数组转换成字符串，默认分隔符为", "
     *
     * @param objects 给定的数组
     * @return 例如：1, 2, 3
     */
    public static String toString(Object[] objects) {
        return toString(objects, null, ", ", null);
    }

    /**
     * 将数组颠倒
     */
    public static Object[] upsideDown(Object[] objects) {
        int length = objects.length;
        Object tem;
        for (int w = 0; w < length / 2; w++) {
            tem = objects[w];
            objects[w] = objects[length - 1 - w];
            objects[length - 1 - w] = tem;
            tem = null;
        }
        return objects;
    }

    /**
     * 得到数组中某个元素前一个元素，isCircle表示是否循环
     * <ul>
     * <li>如果数组为空，返回默认元素</li>
     * <li>如果目标值不在数组中，返回默认元素</li>
     * <li>如果目标元素在数组中存在且不是数组中第一个元素，返回 return 目标元素前一个元素</li>
     * <li>如果目标元素在数组中存在且是数组中第一个元素， 如果是循环遍历，则返回数组中最后一个元素，否则返回默认元素</li>
     * </ul>
     * 
     * @param <V>
     * @param sourceArray
     * @param value 目标元素
     * @param defaultValue 默认值
     * @param isCircle 是否循环
     * @return
     */
    public static <V> V getLast(V[] sourceArray, V value, V defaultValue, boolean isCircle) {
        if (isEmpty(sourceArray)) {
            return defaultValue;
        }

        int currentPosition = -1;
        for (int i = 0; i < sourceArray.length; i++) {
            if (Validate.isEquals(value, sourceArray[i])) {
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
     * 得到数组中某个元素下一个元素，isCircle表示是否循环
     * <li>如果数组为空，返回默认元素</li>
     * <li>如果目标值不在数组中，返回默认元素</li>
     * <li>如果目标元素在数组中存在且不是数组中最后一个元素，返回 return 目标元素后一个元素</li>
     * <li>如果目标元素在数组中存在且是数组中最后一个元素， 如果是循环遍历，则返回数组中第一个元素，否则返回默认元素</li>
     *
     * @param <V>
     * @param sourceArray
     * @param value 目标元素
     * @param defaultValue 默认元素
     * @param isCircle 是否循环
     * @return
     */
    public static <V> V getNext(V[] sourceArray, V value, V defaultValue, boolean isCircle) {
        if (isEmpty(sourceArray)) {
            return defaultValue;
        }

        int currentPosition = -1;
        for (int i = 0; i < sourceArray.length; i++) {
            if (Validate.isEquals(value, sourceArray[i])) {
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
     * @see {@link ArrayUtils#getLast(Object[], Object, Object, boolean)} defaultValue is null
     */
    public static <V> V getLast(V[] sourceArray, V value, boolean isCircle) {
        return getLast(sourceArray, value, null, isCircle);
    }

    /**
     * @see {@link ArrayUtils#getNext(Object[], Object, Object, boolean)} defaultValue is null
     */
    public static <V> V getNext(V[] sourceArray, V value, boolean isCircle) {
        return getNext(sourceArray, value, null, isCircle);
    }

    /**
     * @see {@link ArrayUtils#getLast(Object[], Object, Object, boolean)} Object is Long
     */
    public static long getLast(long[] sourceArray, long value, long defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Long[] array = transformLongArray(sourceArray);
        return getLast(array, value, defaultValue, isCircle);

    }

    /**
     * @see {@link ArrayUtils#getNext(Object[], Object, Object, boolean)} Object is Long
     */
    public static long getNext(long[] sourceArray, long value, long defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Long[] array = transformLongArray(sourceArray);
        return getNext(array, value, defaultValue, isCircle);
    }

    /**
     * @see {@link ArrayUtils#getLast(Object[], Object, Object, boolean)} Object is Integer
     */
    public static int getLast(int[] sourceArray, int value, int defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Integer[] array =transformIntArray(sourceArray);
        return getLast(array, value, defaultValue, isCircle);

    }

    /**
     * @see {@link ArrayUtils#getNext(Object[], Object, Object, boolean)} Object is Integer
     */
    public static int getNext(int[] sourceArray, int value, int defaultValue, boolean isCircle) {
        if (sourceArray.length == 0) {
            throw new IllegalArgumentException("The length of source array must be greater than 0.");
        }

        Integer[] array = transformIntArray(sourceArray);
        return getNext(array, value, defaultValue, isCircle);
    }
}
