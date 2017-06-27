package org.q3df.common;

/**
 * Created by michael on 21.06.17.
 */
public class BitStream implements BitStreamReader, BitStreamWriter {

    private static int BIT_POS[] = new int[32];
    private static int BIT_POS_REV[] = new int[32];
    private static int BIT_MASK[] = new int[32];

    static {
        int bit_set = 0x01;
        for (int i = 0; i < 32; i++){
            BIT_POS[i] = bit_set;
            BIT_POS_REV[31-i] = bit_set;
            BIT_MASK[i] = (0xFFFFFFFF ^ bit_set);
            bit_set <<= 1;
        }
    }


    private byte[] data;
    private int bitsSize;
    private int size;

    private int readBitPos;
    private int writeBitPos;

    public BitStream(byte[] data) {
        this (data, data.length * 8);
    }

    public BitStream(byte[] data, int bitsLen) {
        if (bitsLen <= 0)
            throw new RuntimeException("wrong bits length : " + bitsLen);

        this.data = data;
        this.bitsSize = Math.min(data.length*8, bitsLen);
        this.size = Math.min(data.length, (int)Math.ceil((double)bitsLen/8.0));
        this.readBitPos = 0;
        this.writeBitPos = 0;
    }

    @Override
    public int getByte(int byteIdx) {
        return 0xFF & ((int)data[byteIdx]);
    }

    @Override
    public int getBitsSize() {
        return bitsSize;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getReadBitPos() {
        return readBitPos;
    }

    @Override
    public int getWriteBitPos() {
        return writeBitPos;
    }

    @Override
    public boolean isEOWR() {
        return writeBitPos >= bitsSize;
    }

    @Override
    public boolean isEORD() {
        return readBitPos >= bitsSize;
    }


    @Override
    public int readBits(int bitsNum) {
        if (readBitPos + bitsNum < bitsSize) {
            int val = 0;
            for (int i = 0; i < bitsSize; i++) {
                val |= (data[readBitPos / 8] & BIT_POS[readBitPos & 7]) != 0 ? BIT_POS[i] : 0;
                readBitPos++;
            }
            return val;
        }

        return 0xFFFFFFFF;
    }

    @Override
    public int readBit() {
        if (readBitPos < bitsSize) {
            int val = (data[readBitPos / 8] & BIT_POS[readBitPos & 7]) != 0 ? 1 : 0;
            readBitPos++;
            return val;
        }
        else
            return 0xFFFFFFFF;
    }


    @Override
    public boolean writeBit(boolean isSet) {
        if (writeBitPos < bitsSize) {
            if (isSet)
                data[writeBitPos / 8] |= BIT_POS[writeBitPos & 7];
            else
                data[writeBitPos / 8] &= BIT_MASK[writeBitPos & 7];

            writeBitPos++;
            return true;
        }
        return false;
    }

    @Override
    public boolean writeBit(int bit) {
        return writeBit(bit != 0);
    }

    @Override
    public boolean writeBits(int val, int bits) {
        while (bits > 0 && writeBitPos < bitsSize) {
            if ((val & 1) != 0)
                data[writeBitPos / 8] |= BIT_POS[writeBitPos & 7];
            else
                data[writeBitPos / 8] &= BIT_MASK[writeBitPos & 7];

            writeBitPos++;
            bits--;
            val >>= 1;
        }

        return bits == 0;
    }

    @Override
    public byte[] data() {
        return data;
    }
}
