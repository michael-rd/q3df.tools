package org.q3df.common;

/**
 * Created by michael on 06.06.17.
 */
public class Const {
    public static final int MESSAGE_MAX_SIZE = 0x4000;
    public static final int MAX_STRING_CHARS = 1024;
    public static final int BIG_INFO_STRING = 8192;

    public static final int MAX_CONFIGSTRINGS = 1024;

    public static final int GENTITYNUM_BITS = 10;
    public static final int MAX_GENTITIES = 1<<GENTITYNUM_BITS;

    public static final int FLOAT_INT_BITS = 13;
    public static final int FLOAT_INT_BIAS = (1<<(FLOAT_INT_BITS-1));

    public static final int MAX_POWERUPS = 16;
    public static final int MAX_WEAPONS = 16;

}
