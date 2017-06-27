package org.q3df.common.msg;

public interface BitStreamWriter {
    int getByte(int byteIdx);

    int getBitsSize();

    int getSize();

    int getWriteBitPos();

    boolean isEOWR();

    boolean writeBit(boolean isSet);

    boolean writeBit(int bit);

    boolean writeBits(int val, int bits);

    byte[] data();
}
