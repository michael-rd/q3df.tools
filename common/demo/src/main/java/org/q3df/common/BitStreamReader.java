package org.q3df.common;

public interface BitStreamReader {
    int getByte(int byteIdx);

    int getBitsSize();

    int getSize();

    int getReadBitPos();

    boolean isEORD();

    int readBits(int bitsNum);

    int readBit();

    byte[] data();
}
