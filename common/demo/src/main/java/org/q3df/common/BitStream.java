package org.q3df.common;

/**
 * Created by michael on 21.06.17.
 */
public class BitStream {


    private static int BIT_POS[] = new int[32];
    private static int BIT_POS_REV[] = new int[32];

    static {
        int mask = 0x01;
        for (int i = 0; i < 32; i++){
            BIT_POS[i] = mask;
            BIT_POS_REV[31-i] = mask;
            mask <<= 1;
        }
    }


    private byte[] data;
    private int bitsSize;
    private int size;

    private int readBitPos;
    private int writeBitPos;

    public BitStream(byte[] data, int bitsLen) {
        if (bitsLen <= 0)
            throw new RuntimeException("wrong bits length : " + bitsLen);

        this.data = data;
        this.bitsSize = Math.min(data.length*8, bitsLen);
        this.size = Math.min(data.length, (int)Math.ceil((double)bitsLen/8.0));
        this.readBitPos = 0;
        this.writeBitPos = 0;
    }

    public int getBitsSize() {
        return bitsSize;
    }

    public int getSize() {
        return size;
    }

    public int getReadBitPos () {
        return readBitPos;
    }

    public int getWriteBitPos() {
        return writeBitPos;
    }

    public boolean write (int val, int bits) {
        if (bits + writeBitPos >= bitsSize)
            return false;


    }

    public byte[] data () {
        return data;
    }
}
