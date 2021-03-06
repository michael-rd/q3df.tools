package org.q3df.common;

import java.nio.ByteBuffer;
import java.util.EnumSet;

public class Utils {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String toHex (ByteBuffer buffer) {
        return bytesToHex(buffer.array());
    }

    public static byte[] fromHex (String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    public static <E extends Enum<E>> E enumByOrdinal (Class<E> clazz, int ord){
        for(E en : EnumSet.allOf(clazz)){
            if(en.ordinal() == ord){
                return en;
            }
        }
        return null;
    }

    public static int ANGLE2SHORT (float x) {
        return ((int)(x*65536.0f/360.0f)) & 65535;
    }

    public static float SHORT2ANGLE (int x) {
        return ((float)x*(360.0f/65536.0f));
    }
}
