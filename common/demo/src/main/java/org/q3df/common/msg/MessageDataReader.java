package org.q3df.common.msg;

import org.q3df.common.Const;
import org.q3df.common.Utils;
import org.q3df.common.struct.EntityState;
import org.q3df.common.struct.PlayerState;

public interface MessageDataReader {

    boolean isEOD ();
    int readBits (int bits);
    int readByte ();

    default int readNumber (int bits) {
        int v = bits == 8 ? readByte () : readBits(bits);
        return isEOD() ? -1 : v;
    }

    default int readChar () {
        int v = readByte();
        return isEOD() ? -1 : v;
    }

    default int readShort () {
        return readNumber(16);
    }
    default int readLong () {
        return readNumber(32);
    }
    default int readInt () {
        return readNumber(32);
    }

    default float readFloat () {
        int ival = readBits(32);
        if (isEOD())
            return -1;
        return Float.intBitsToFloat(ival);
    }

    default float readAngle16 () {
        int v = readShort();
        return isEOD() ? 0 : Utils.SHORT2ANGLE(v);
    }

    String readStringBase (int limit, boolean stopAtNewLine);
    default String readString () {
        return readStringBase (Const.MAX_STRING_CHARS, false);
    }
    default String readBigString () {
        return readStringBase (Const.BIG_INFO_STRING, false);
    }
    default String readStringLine () {
        return readStringBase (Const.MAX_STRING_CHARS, true);
    }


    boolean readDeltaPlayerState (PlayerState state);
    boolean readDeltaEntity (EntityState state, int number);

    default boolean skipDeltaEntity () {
        return readDeltaEntity(new EntityState(), 0);
    }


    default void readData (byte[] data, int len) {
        for (int i = 0; i < Math.min(len,data.length); i++) {
            data[i] = (byte)readByte();
        }
    }
}
