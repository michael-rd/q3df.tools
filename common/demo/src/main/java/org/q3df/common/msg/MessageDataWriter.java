package org.q3df.common.msg;

import org.q3df.common.Const;
import org.q3df.common.Utils;

public interface MessageDataWriter {

    boolean isEOD ();

    boolean writeBits(int value, int bits);

    default boolean writeByte (int value) { return writeBits(value, 8); }
    default boolean writeChar (int c) {
        return writeBits(0xFF & c, 8);
    }
    default boolean writeShort (int value) { return writeBits(value, 16); }
    default boolean writeLong (int value) { return writeBits(value, 32); }
    default boolean writeInt (int value) { return writeBits(value, 32); }
    default boolean writeFloat (float f) {
        return writeBits(Float.floatToRawIntBits(f), 32);
    }
    default boolean writeAngle (float angle) {
        return writeByte ((int)(angle*256/360) & 255);
    }
    default boolean writeAngle16 (float angle) {
        return writeShort (Utils.ANGLE2SHORT(angle));
    }

    default boolean writeData (byte[] data, int length) {
        int written = 0;
        length = Math.min(length, data.length);

        while (written < length && writeByte(data[written]))
            written++;

        return written == length;
    }

    boolean writeStringBase (String str, int limit);
    default boolean writeString (String str) {
        return writeStringBase(str, Const.MAX_STRING_CHARS);
    }
    default boolean writeBigString (String str) {
        return writeStringBase(str, Const.BIG_INFO_STRING);
    }
}
