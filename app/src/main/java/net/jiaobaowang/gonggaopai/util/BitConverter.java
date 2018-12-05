package net.jiaobaowang.gonggaopai.util;

import static java.lang.Integer.parseInt;

/**
 * 位处理，字节处理
 *
 * @author Master.Xia Date:2017年12月15日
 */
public class BitConverter {

    public static short ToInt16(byte[] bytes, int offset) {
        short result = (short) ((int) bytes[offset] & 0xff);
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        return (short) (result & 0xffff);
    }

    public static int ToUInt16(byte[] bytes, int offset) {
        int result = (int) bytes[offset + 1] & 0xff;
        result |= ((int) bytes[offset] & 0xff) << 8;
        return result & 0xffff;
    }

    public static int ToInt32(byte[] bytes, int offset) {
        int result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        return result;
    }

    public static long ToUInt32(byte[] bytes, int offset) {
        long result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        return result & 0xFFFFFFFFL;
    }

    public static long ToInt64(byte[] buffer, int offset) {
        long values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8;
            values |= (buffer[offset + i] & 0xFF);
        }
        return values;
    }

    public static long ToUInt64(byte[] bytes, int offset) {
        long result = 0;
        for (int i = 0; i <= 56; i += 8) {
            result |= ((int) bytes[offset++] & 0xff) << i;
        }
        return result;
    }

    public static float ToFloat(byte[] bs, int index) {
        return Float.intBitsToFloat(ToInt32(bs, index));
    }

    public static double ToDouble(byte[] arr, int offset) {
        return Double.longBitsToDouble(ToUInt64(arr, offset));
    }

    public static boolean ToBoolean(byte[] bytes, int offset) {
        return (bytes[offset] == 0x00) ? false : true;
    }

    public static byte[] GetBytes(short value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value & 0xff);
        bytes[1] = (byte) ((value & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] GetBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value) & 0xFF); //最低位
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >>> 24)); //最高位，无符号右移
        return bytes;
    }

    //将int类型转换成字节数组 (高位在后，低位在前的顺序) 大段序
    public static byte[] intToByte(int i) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (i & 0xFF);
        targets[1] = (byte) (i >> 8 & 0xFF);
        targets[2] = (byte) (i >> 16 & 0xFF);
        targets[3] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    //将int类型转换成字节数组 (高位在前，低位在后的顺序) 小段序
    public static byte[] intToByte2(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    public static byte[] GetBytes(long values) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }

    public static byte[] GetBytes(float value) {
        return GetBytes(Float.floatToIntBits(value));
    }

    public static byte[] GetBytes(double val) {
        long value = Double.doubleToLongBits(val);
        return GetBytes(value);
    }

    public static byte[] GetBytes(boolean value) {
        return new byte[]{(byte) (value ? 1 : 0)};
    }

    public static byte IntToByte(int x) {
        return (byte) x;
    }

    public static int ByteToInt(byte b) {
        return b & 0xFF;
    }

    public static char ToChar(byte[] bs, int offset) {
        return (char) (((bs[offset] & 0xFF) << 8) | (bs[offset + 1] & 0xFF));
    }

    public static byte[] GetBytes(char value) {
        byte[] b = new byte[2];
        b[0] = (byte) ((value & 0xFF00) >> 8);
        b[1] = (byte) (value & 0xFF);
        return b;
    }

    public static byte[] Concat(byte[]... bs) {
        int len = 0, idx = 0;
        for (byte[] b : bs) {
            len += b.length;
        }
        byte[] buffer = new byte[len];
        for (byte[] b : bs) {
            System.arraycopy(b, 0, buffer, idx, b.length);
            idx += b.length;
        }
        return buffer;
    }

    public static void main(String[] args) {
        long a = 123456;
        byte[] b1 = GetBytes(a);
        long b = ToInt64(b1, 0);
        System.out.println(b);
    }

    //将long类型转换成byte[]数组 （大端序，低位在前，高位在后）
    public static byte[] longToByteArray(long s) {
        byte[] targets = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[7 - i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }


    //byte数组中取int数值，本方法适用于(高位在前，低位在后)的顺序。小段序
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
        return value;

    }


    // byte数组中取int数值，本方法适用于(高位在后，低位在前)的顺序。 大段序

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset + 3] & 0xFF) << 24)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 1] & 0xFF) << 8)
                | (src[offset + 0] & 0xFF));
        return value;
    }


    //byte[8]转long 大端序（低位在前，高位在后）
    public static long byte2Long(byte[] b) {
        return
                ((b[7] & 0xff) << 56) |
                        ((b[6] & 0xff) << 48) |
                        ((b[5] & 0xff) << 40) |
                        ((b[4] & 0xff) << 32) |
                        ((b[3] & 0xff) << 24) |
                        ((b[2] & 0xff) << 16) |
                        ((b[1] & 0xff) << 8) |
                        (b[0] & 0xff);
    }


    //byte[8]转long 大端序（低位在前，高位在后）
    public static long byte2Long(byte[] b, int offset) {
        return ((((long) b[offset + 7] & 0xff) << 56)
                | (((long) b[offset + 6] & 0xff) << 48)
                | (((long) b[offset + 5] & 0xff) << 40)
                | (((long) b[offset + 4] & 0xff) << 32)

                | (((long) b[offset + 3] & 0xff) << 24)
                | (((long) b[offset + 2] & 0xff) << 16)
                | (((long) b[offset + 1] & 0xff) << 8)
                | (((long) b[offset + 0] & 0xff) << 0));
    }


    public static int parseUnsignedInt(String s) throws NumberFormatException {
        return parseUnsignedInt(s, 10);
    }

    public static int parseUnsignedInt(String s, int radix)
            throws NumberFormatException {
        if (s == null)  {
            throw new NumberFormatException("null");
        }
        int len = s.length();
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar == '-') {
                throw new NumberFormatException(String.format("Illegal leading minus sign " + "on unsigned string %s.", s));
            } else {
                if (len <= 5 || // Integer.MAX_VALUE in Character.MAX_RADIX is 6 digits
                        (radix == 10 && len <= 9) ) { // Integer.MAX_VALUE in base 10 is 10 digits
                    return parseInt(s, radix);
                } else {
                    long ell = Long.parseLong(s, radix);
                    if ((ell & 0xffff_ffff_0000_0000L) == 0) {
                        return (int) ell;
                    } else {
                        throw new NumberFormatException(String.format("String value %s exceeds " + "range of unsigned int.", s));
                    }
                }
            }
        } else {
            throw new NumberFormatException(s);
        }
    }
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}