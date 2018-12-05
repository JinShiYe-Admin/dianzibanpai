package net.jiaobaowang.gonggaopai.util;

import java.util.HashSet;
import java.util.List;

/**
 * 检测字符串是否为空的工具类
 */
public class Validate {

    private Validate() {
        throw new Error("Do not need instantiate!");
    }

    /* ******************************* 字符串检验start *******************************************************/
    public static boolean isNull(String value) {
        return value == null || "".equals(value.trim()) || "null".equals(value.trim());
    }

    public static boolean noNull(String value) {
        return value != null && !"".equals(value.trim()) && !"null".equals(value.trim());
    }

    public static String isNullTodefault(String value, String defalutValue) {
        return isNull(value) == true ? defalutValue : value;
    }

    public static boolean isZero(String value) {
        return value == "0" || "0".equals(value.trim());
    }

    public static String isZeroTodefault(String value, String defalutValue) {
        return isZero(value) == true ? defalutValue : value;
    }


    /**
     * 验证字符串的长度是在指定范围内，不在的话抛出IllegalArgumentException异常，忽略前后的空白符
     *
     * @param string    待验证的字符串
     * @param minLength 最小长度（包括）
     * @param maxLength 最大长度（包括）
     * @throws IllegalArgumentException string的长度不在minLength到maxLength之间时抛出此异常
     */
    public static void valiStringLength(String string, int minLength, int maxLength) throws
            IllegalArgumentException {
        int length = string.trim().length();
        if (!(length >= minLength && length <= maxLength)) {
            throw new IllegalArgumentException("String '" + string + "' length illegal!");
        }
    }

    /**
     * 验证字符串的长度的最小值，不合法的话抛出IllegalArgumentException异常，忽略前后的空白符
     *
     * @param string    待验证的字符串
     * @param minLength 最小长度（包括）
     * @throws IllegalArgumentException string的长度小于minLength时抛出此异常
     */
    public static void valiStringMinLength(String string, int minLength) throws
            IllegalArgumentException {
        int length = string.trim().length();
        if (!(length >= minLength)) {
            throw new IllegalArgumentException("String '" + string + "' length illegal!");
        }
    }

    /**
     * 验证字符串的长度的最大值，不合法的话抛出IllegalArgumentException异常，忽略前后的空白符
     *
     * @param string    待验证的字符串
     * @param maxLength 最大长度（包括）
     * @throws IllegalArgumentException string的长度大于maxLength时抛出此异常
     */
    public static void valiStringMaxLength(String string, int maxLength) throws
            IllegalArgumentException {
        int length = string.trim().length();
        if (!(length <= maxLength)) {
            throw new IllegalArgumentException("String '" + string + "' length illegal!");
        }
    }
	/* ************************************字符串检验end************************************************ */
	/* ************************************Object检验Start************************************************ */


    /**
     *  对比两个Object
     * compare two object
     *
     * @param actual
     * @param expected
     * @return <ul>
     *         <li>if both are null, return true</li>
     *         <li>return actual.{@link Object#equals(Object)}</li>
     *         </ul>
     */
    public static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    /**
     *
     * compare two object
     * <ul>
     * <strong>About result</strong>
     * <li>if v1 > v2, return 1</li>
     * <li>if v1 = v2, return 0</li>
     * <li>if v1 < v2, return -1</li>
     * </ul>
     * <ul>
     * <strong>About rule</strong>
     * <li>if v1 is null, v2 is null, then return 0</li>
     * <li>if v1 is null, v2 is not null, then return -1</li>
     * <li>if v1 is not null, v2 is null, then return 1</li>
     * <li>return v1.{@link Comparable#compareTo(Object)}</li>
     * </ul>
     *
     * @param v1
     * @param v2
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <V> int compare(V v1, V v2) {
        return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1 : ((Comparable)v1).compareTo(v2));
    }
	/* ************************************Object检验end************************************************ */

	/* ***************************************************** int相关验证start*********************************************************************** */

    /**
     *验证Int型数据是在指定范围内，不在的话抛出IllegalArgumentException异常
     *
     * @param number     待验证的数字
     * @param minValue   最小值（包括）
     * @param maxValue   最大值（包括）
     * @param objectName 抛出异常时提示："Int object '"+objectName+"' is illegal!"
     * @throws IllegalArgumentException number的值不在minLength到maxLength之间时抛出此异常
     */
    public static void valiIntValue(int number, int minValue, int maxValue, String objectName) throws
            IllegalArgumentException {
        if (!((number >= minValue) && (number <= maxValue))) {
            throw new IllegalArgumentException("Int object '" + objectName + "' is illegal!");
        }
    }


    /**
     * 验证Int数据的最小值，不合法的话抛出IllegalArgumentException异常
     *
     * @param number     待验证的Int数据
     * @param minValue   最小值（包括）
     * @param objectName 抛出异常时提示："Int object '"+objectName+"' is illegal!"
     * @throws IllegalArgumentException number的值小于minLength时抛出此异常
     */
    public static void valiIntMinValue(int number, int minValue, String objectName) throws
            IllegalArgumentException {
        if (!(number >= minValue)) {
            throw new IllegalArgumentException("Int object '" + objectName + "' is illegal!");
        }
    }


    /**
     * 验证Int数据的最大值，不合法的话抛出IllegalArgumentException异常
     *
     * @param number     待验证的Int数据
     * @param maxValue   最大值（包括）
     * @param objectName 抛出异常时提示："Int object '"+objectName+"' is illegal!"
     * @throws IllegalArgumentException number的值大于maxLength时抛出此异常
     */
    public static void valiIntMaxValue(int number, int maxValue, String objectName) throws
            IllegalArgumentException {
        if (!(number <= maxValue)) {
            throw new IllegalArgumentException("Int object '" + objectName + "' is illegal!");
        }
    }
    /* ************************************int检验 end ************************************************ */

    /* *****************************************************Long检验start************************************************************************ */


    /**
     * 验证Long型数据是在指定范围内，不在的话抛出IllegalArgumentException异常
     *
     * @param number     待验证的数字
     * @param minValue   最小值（包括）
     * @param maxValue   最大值（包括）
     * @param objectName 抛出异常时提示："Long object '"+objectName+"' is illegal!"
     * @throws IllegalArgumentException number的值不在minLength到maxLength之间时抛出此异常
     */
    public static void valiLongValue(long number, long minValue, long maxValue, String objectName) throws
            IllegalArgumentException {
        if (!((number >= minValue) && (number <= maxValue))) {
            throw new IllegalArgumentException("Long object '" + objectName + "' is illegal!");
        }
    }


    /**
     * 验证Long数据的最小值，不合法的话抛出IllegalArgumentException异常
     *
     * @param number     待验证的Int数据
     * @param minValue   最小值（包括）
     * @param objectName 抛出异常时提示："Long object '"+objectName+"' is illegal!"
     * @throws IllegalArgumentException number的值小于minLength时抛出此异常
     */
    public static void valiLongMinValue(long number, long minValue, String objectName) throws
            IllegalArgumentException {
        if (!(number >= minValue)) {
            throw new IllegalArgumentException("Long object '" + objectName + "' is illegal!");
        }
    }


    /**
     * 验证Long数据的最大值，不合法的话抛出IllegalArgumentException异常
     *
     * @param number     待验证的Int数据
     * @param maxValue   最大值（包括）
     * @param objectName 抛出异常时提示："Int object '"+objectName+"' is illegal!"
     * @throws IllegalArgumentException number的值大于maxLength时抛出此异常
     */
    public static void valiLongMaxValue(long number, long maxValue, String objectName) throws
            IllegalArgumentException {
        if (!(number <= maxValue)) {
            throw new IllegalArgumentException("Long object '" + objectName + "' is illegal!");
        }
    }

    /**
     *  普通字符转换成16进制字符串
     * @param str
     * @return
     */
    public static String str2HexStr(String str)
    {
        String strHex = Integer.toHexString(BitConverter.parseUnsignedInt(str));
        return strHex;
    }
    /**
     *  16进制字符串转换成普通字符
     * @param str
     * @return
     */
    public static String hexStr2Str(String str)
    {
        Integer valueTen2 = Integer.valueOf(str,16);
        return valueTen2.toString();
    }

    /**
     *  List 去重
     * @return
     */
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     *  将整数num转化为32位的二进制数
     * @return
     */
    public static String toFullBinaryString(int num)
    {
        char[] chs = new char[Integer.SIZE];
        for (int i = 0; i < Integer.SIZE; i++)
        {
            chs[Integer.SIZE - 1 - i] = (char) (((num >> i) & 1) + '0');
        }
        return new String(chs);
    }

    /**
     * 二进制转十进制
     *
     * @param binaryNumber
     * @return
     */
    public static int binaryToDecimal(int binaryNumber) {

        int decimal = 0;
        int p = 0;
        while (true) {
            if (binaryNumber == 0) {
                break;
            } else {
                int temp = binaryNumber % 10;
                decimal += temp * Math.pow(2, p);
                binaryNumber = binaryNumber / 10;
                p++;
            }
        }
        return decimal;
    }


}
