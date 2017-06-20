package org.q3df.common.msg;

import org.q3df.common.Const;
import org.q3df.common.Utils;
import org.q3df.common.serialize.FieldMapper;
import org.q3df.common.serialize.FieldMapperFactory;
import org.q3df.common.struct.EntityState;
import org.q3df.common.struct.PlayerState;
import org.q3df.demo.DemoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by michael on 08.06.17.
 */
public class Q3HuffmanCoder {

    private static Logger logger = LoggerFactory.getLogger(Q3HuffmanCoder.class);

    static final int NYT = 0xFFFFFFFF;
    // '%'
    private static final byte PERCENT_CHAR_BYTE = 37;
    private static final byte DOT_CHAR_BYTE = 46;

    static final byte entityStateFieldSize[] = {

    };

    static final int huff_paths[] = {
            0x0006, 0x003B, 0x00C8, 0x00EC, 0x01A1, 0x0111, 0x0090, 0x007F, 0x0035, 0x00B4, 0x00E9, 0x008B, 0x0093, 0x006D, 0x0139, 0x02AC,
            0x00A5, 0x0258, 0x03F0, 0x03F8, 0x05DD, 0x07F3, 0x062B, 0x0723, 0x02F4, 0x058D, 0x04AB, 0x0763, 0x05EB, 0x0143, 0x024F, 0x01D4,
            0x0077, 0x04D3, 0x0244, 0x06CD, 0x07C5, 0x07F9, 0x070D, 0x07CD, 0x0294, 0x05AC, 0x0433, 0x0414, 0x0671, 0x06F0, 0x03F4, 0x0178,
            0x00A7, 0x01C3, 0x01EF, 0x0397, 0x0153, 0x01B1, 0x020D, 0x0361, 0x0207, 0x02F1, 0x0399, 0x0591, 0x0523, 0x02BC, 0x0344, 0x05F3,
            0x01CF, 0x00D0, 0x00FC, 0x0084, 0x0121, 0x0151, 0x0280, 0x0270, 0x033D, 0x0463, 0x06D7, 0x0771, 0x039D, 0x06AB, 0x05C7, 0x0733,
            0x032C, 0x049D, 0x056B, 0x076B, 0x05D3, 0x0571, 0x05E3, 0x0633, 0x04D7, 0x06CB, 0x0370, 0x02A8, 0x02C7, 0x0305, 0x02EB, 0x01D8,
            0x02F3, 0x013C, 0x03AB, 0x038F, 0x0297, 0x00B0, 0x0141, 0x034F, 0x005C, 0x0128, 0x02BD, 0x02C4, 0x0198, 0x028F, 0x010C, 0x01B3,
            0x0185, 0x018C, 0x0147, 0x0179, 0x00D9, 0x00C0, 0x0117, 0x0119, 0x014B, 0x01E1, 0x01A3, 0x0173, 0x016F, 0x00E8, 0x0088, 0x00E5,
            0x005F, 0x00A9, 0x00CC, 0x00FD, 0x010F, 0x0183, 0x0101, 0x0187, 0x0167, 0x01E7, 0x0157, 0x0174, 0x03CB, 0x03C4, 0x0281, 0x024D,
            0x0331, 0x0563, 0x0380, 0x07D7, 0x042B, 0x0545, 0x046B, 0x043D, 0x072B, 0x04F9, 0x04E3, 0x0645, 0x052B, 0x0431, 0x07EB, 0x05B9,
            0x0314, 0x05F9, 0x0533, 0x042C, 0x06DD, 0x05C1, 0x071D, 0x05D1, 0x0338, 0x0461, 0x06E3, 0x0745, 0x066B, 0x04CD, 0x04CB, 0x054D,
            0x0238, 0x07C1, 0x063D, 0x07BC, 0x04C5, 0x07AC, 0x07E3, 0x0699, 0x07D3, 0x0614, 0x0603, 0x05BC, 0x069D, 0x0781, 0x0663, 0x048D,
            0x0154, 0x0303, 0x015D, 0x0060, 0x0089, 0x07C7, 0x0707, 0x01B8, 0x03F1, 0x062C, 0x0445, 0x0403, 0x051D, 0x05C5, 0x074D, 0x041D,
            0x0200, 0x07B9, 0x04DD, 0x0581, 0x050D, 0x04B9, 0x05CD, 0x0794, 0x05BD, 0x0594, 0x078D, 0x0558, 0x07BD, 0x04C1, 0x07DD, 0x04F8,
            0x02D1, 0x0291, 0x0499, 0x06F8, 0x0423, 0x0471, 0x06D3, 0x0791, 0x00C9, 0x0631, 0x0507, 0x0661, 0x0623, 0x0118, 0x0605, 0x06C1,
            0x05D7, 0x04F0, 0x06C5, 0x0700, 0x07D1, 0x07A8, 0x061D, 0x0D00, 0x0405, 0x0758, 0x06F9, 0x05A8, 0x06B9, 0x068D, 0x00AF, 0x0064
    };

    // generated
    static int MAX_PATH_VALUE = 0;
    static int sym_table[] ;
    // size in bits for value
    private static final byte sym_size[] = new byte[256];

    static int BIT_POS[] = new int[32];
    static int BIT_POS_REV[] = new int[32];
//    static int BIT_MASK[] = new int[32];
//    static int BIT_MASK_REV[] = new int[32];

    static {
        // prepare table
        for (int v : huff_paths) {
            MAX_PATH_VALUE = Math.max(MAX_PATH_VALUE, v);
        }

        sym_table = new int[MAX_PATH_VALUE + 1];
        Arrays.fill(sym_table, NYT);

        for (int i = 0; i < huff_paths.length; i++) {
            sym_table[ huff_paths[i] ] = i;
            sym_size [i] = bits_len (huff_paths[i]);
        }

        int mask = 0x01;
        for (int i = 0; i < 32; i++){
            BIT_POS[i] = mask;
            BIT_POS_REV[31-i] = mask;

//            BIT_MASK[i] = 0xFFFFFFFF ^ mask;
//            BIT_MASK_REV[31-i] = 0xFFFFFFFF ^ mask;

            mask <<= 1;
        }
    }

    public static int symBits (int byteValue) {
        return sym_size [ byteValue ];
    }

    private static byte bits_len (int path) {
        byte size = 0;
        while (path > 1) {
            size++;
            path >>= 1;
        }
        return size;
    }

    public static class Encoder {

        private byte[] buffer;

        private int limit;
        private int position;
        private int writtenBits;
        private int remainBits;
        private byte currByte;

        public Encoder (int buffer_size) {
            this (new byte[buffer_size], false);
        }

        public Encoder (byte[] buffer, boolean fillZero) {
            this.buffer = buffer;
            this.limit = buffer.length;
            this.position = 0;
            this.writtenBits = 0;
            this.remainBits = limit * 8;
            this.currByte = 0;

            if (fillZero)
                Arrays.fill(this.buffer, (byte)0);
        }

        public int remainBits () {
            return remainBits;
        }

        public int position () {
            return position;
        }

        public int writtenBits () {
            return writtenBits;
        }

        public byte[] buffer () {
            return this.buffer;
        }

        public String dumpBuffer () {
            return Utils.bytesToHex(this.buffer);
        }

        private int writeBitsRaw(int value, int bits) {

            // overflow
            if (bits > remainBits)
                return 0;

            remainBits -= bits;

            int bitIdx = 0;

            while (bitIdx < bits) {
                if ((BIT_POS[bitIdx] & value) != 0) {
                    // set 1
                    currByte |= BIT_POS[writtenBits & 0x7];
                }
                writtenBits++;

                if ((writtenBits & 0x7) == 0) {
                    buffer[position] = currByte;
                    position++;
                    currByte = buffer[position];
                }

                bitIdx++;
            }

            buffer[position] = currByte;

            return bits;
        }

        public boolean writeBits(int value, int bits) {
            // this isn't an exact overflow check, but close enough
            if (limit - position < 4)
                return false;

            if ( bits == 0 || bits < -31 || bits > 32 )
                throw new RuntimeException(String.format("MSG_WriteBits: bad bits %i", bits));

            if (bits < 0)
                bits = -1 * bits;

            value &= (0xffffffff>>(32-bits));

            int fractionBits = bits & 7;

            if (fractionBits > 0) {
                // fraction bits
                writeBitsRaw (value, fractionBits);
                bits -= fractionBits;
                value = value >> fractionBits;
            }

            while (bits > 0) {
                if (writeBitsRaw(huff_paths[0xFF & value], sym_size[0xFF & value]) <= 0)
                    return false;

                bits -= 8;
                value >>= 8;
            }

            return true;
        }

        public boolean writeByte (int value) {
            if ((value & 0xFFFFFF00) != 0)
                return false;

            return writeBitsRaw(huff_paths[0xFF & value], sym_size[0xFF & value]) > 0;
        }


        private boolean writeNum (int value, int bytes) {

            while (bytes > 0) {
                if (writeBitsRaw(huff_paths[0xFF & value], sym_size[0xFF & value]) <= 0)
                    break;

                bytes--;
                value >>= 8;
            }

            return bytes == 0;
        }

        public boolean writeShort (int value) {
            if ((value & 0xFFFF0000) != 0)
                return false;

            return writeNum (value, 2);
        }

        public boolean writeInt (int value) {
            return writeNum (value, 4);
        }

        // for lexical compatibility
        public boolean writeLong (int value) {
            return writeNum (value, 4);
        }


        public boolean writeFload (float f) {
            return writeNum(Float.floatToRawIntBits(f), 4);
        }

        public boolean writeData (byte[] data, int length) {
            int written = 0;
            length = Math.min(length, data.length);

            for (int i = 0; i < length; i++) {
                if (writeBitsRaw(huff_paths[0xFF & data[i]], sym_size[0xFF & data[i]]) > 0) {
                    written++;
                }
                else
                    break;
            }

            return written == length;
        }

        public boolean writeChar (int c) {
            return writeBitsRaw(huff_paths[0xFF & c], sym_size[0xFF & c]) > 0;
        }

        private boolean writeStringBase (String str, int limit) {
            if (str == null || str.isEmpty())
                return writeByte(0);

            byte[] data = str.getBytes();

            if (data.length >= limit) {
                logger.debug("MSG_WriteString: MAX_STRING_CHARS = {}", limit);
                return writeByte(0);
            }

            for (int i = 0; i < data.length; i++)
                if (data[i] > 127 || data[i] == PERCENT_CHAR_BYTE)
                    data[i] = DOT_CHAR_BYTE;

            return writeData(data, limit-1) && writeByte(0);
        }

        public boolean writeString (String str) {
            return writeStringBase(str, Const.MAX_STRING_CHARS);
        }

        public boolean writeBigString (String str) {
            return writeStringBase(str, Const.BIG_INFO_STRING);
        }

        public boolean writeAngle (float angle) {
            return writeByte ((int)(angle*256/360) & 255);
        }

        public boolean writeAngle16 (float angle) {
            return writeShort (ANGLE2SHORT(angle));
        }
    }

    public static class Decoder {

        private byte[] buffer;
        private int limit;

        // byte offset position, index for buffer data
        private int position;

        // bit offset position
        private int readBitsPos;

        private byte currByte;

        public Decoder(byte[] buffer, int limit) {
            this.buffer = buffer;
            this.limit = Math.max(buffer.length, limit);

            reset();
        }

        // MSG_BeginReading
        public void reset () {
            this.position = 0;
            this.readBitsPos = 0;
            this.currByte = buffer[0];
        }

        public boolean isEOD () {
            return this.position >= limit;
        }

        private int readBitValue () {
            int bit = ((currByte & BIT_POS[readBitsPos&7]) != 0) ? 1 : 0;

            readBitsPos++;
            if ((readBitsPos & 7) == 0) {
                position++;

                if (position < limit)
                    currByte = buffer[position];
                else
                    return 0;
            }

            return bit;
        }

        private int decodeByte () {
            int sym_path = 0;
            int rest = 0;
            int decoded = NYT;
            int bitIdx = 0;
            while (decoded == NYT) {
                if (readBitValue() != 0)
                    rest |= BIT_POS[bitIdx];

                sym_path = BIT_POS[bitIdx+1] | rest;

                if (sym_path > MAX_PATH_VALUE)
                    //throw new RuntimeException("Stream is corrupted");
                    return 0;

                bitIdx++;
                decoded = sym_table[sym_path];
            }

            return decoded;
        }

        public int readBits (int bits) {

            int value = 0;
            boolean neg = bits < 0;

            if (neg)
                bits = bits*-1;

            int fragmentBits = bits & 7;

            if (fragmentBits != 0) {
                for (int i = 0; i < fragmentBits; i++) {
                    value |= readBitValue() != 0 ? BIT_POS[i] : 0;
                }

                bits -= fragmentBits;
            }

            if (bits > 0) {
                int decoded = 0;
                for (int i = 0; i < bits; i+=8) {
                    decoded |= (decodeByte() << i);
                }

                if (fragmentBits > 0)
                    decoded <<= fragmentBits;

                value |= decoded;
            }

            if (neg) {
                if ( (value & ( 1 << ( bits - 1 ))) != 0 ) {
                    value |= -1 ^ ( ( 1 << bits ) - 1 );
                }
            }

            return value;
        }

        private int readNumber (int bits) {
            int v = bits == 8 ? decodeByte () : readBits(bits);
            return isEOD() ? -1 : v;
        }

        public int readChar () {
            byte v = (byte)decodeByte();
            return isEOD() ? -1 : v;
        }

        public int readByte () {
            return readNumber(8);
        }

        public int readShort () {
            return readNumber(16);
        }

        // same as read-int, add for lexical compatibility
        public int readLong () {
            return readNumber(32);
        }

        public int readInt () {
            return readNumber(32);
        }

        public float readFloat () {
            int ival = readBits(32);
            if (isEOD())
                return -1;
            return Float.intBitsToFloat(ival);
        }

        private String readStringLim (int limit, boolean stopAtNewLine) {
            byte[] str = new byte[limit];

            int ch = 0;
            int len = 0;
            while (len < limit) {
                ch = readByte();

                if (ch <= 0)
                    break;

                if (stopAtNewLine && ch == 0x0A)
                    break;

                // translate all fmt spec to avoid crash bugs
                // don't allow higher ascii values
                if (ch > 127 || ch == PERCENT_CHAR_BYTE)
                    ch = DOT_CHAR_BYTE;

                str[len] = (byte)ch;
                len++;
            }

            return new String(str, 0, len);
        }

        public String readString () {
            return readStringLim (Const.MAX_STRING_CHARS, false);
        }

        public String readBigString () {
            return readStringLim (Const.BIG_INFO_STRING, false);
        }

        public String readStringLine () {
            return readStringLim (Const.MAX_STRING_CHARS, true);
        }

        public float readAngle16 () {
            return SHORT2ANGLE(readShort());
        }

        public void readData (byte[] data, int len) {
            for (int i = 0; i < Math.min(len,data.length); i++) {
                data[i] = (byte)readByte();
            }
        }

        public boolean skipDeltaEntity () {
            return readDeltaEntity(new EntityState(), 0);
        }

        public boolean readDeltaEntity (EntityState state, int number) {
            // check for a remove
//  orig:   if ( MSG_ReadBits( msg, 1 ) == 1 ) {
            if (readBitValue() == 1) {
                state.number = Const.MAX_GENTITIES - 1;
                // clear state and return
                return true;
            }

            // check for no delta
            if (readBitValue() == 0) {
                //
                state.number = number;
                return true;
            }

            int lc = readByte();

            if (lc < 0 || lc > FieldMapperFactory.getEntityStateFieldNum()) {
                logger.error("invalid entityState field count: {}", lc);
                return false;
            }

            state.number = number;
            for (int i = 0; i < lc; i++) {
                if (readBits(1) == 0) {
                    //no change
                    continue;
                }

                FieldMapper mapper = FieldMapperFactory.entStateFieldMapper(i);

                if (readBits(1) == 0)
                    mapper.reset(state);
                else
                    mapper.read(this, state);
            }

            return true;
        }

        public boolean readDeltaPlayerState (PlayerState state) {
            int lc = readByte();

            if (lc < 0 || lc > FieldMapperFactory.getPlayerStateFieldNum()) {
                logger.error("invalid playerState field count: {}", lc);
                return false;
            }

            for (int i = 0; i < lc; i++) {
                if (readBits(1) == 0) {
                    // no change;
                    continue;
                }

                FieldMapperFactory.playerStateFieldMapper(i).read(this, state);
            }

            // read arrays
            if (readBits(1) != 0) {

                //parse stats
                if (readBits(1) != 0) {
                    pstArrayRead(state.stats, Const.MAX_STATS);
                }

                // parse persistant stats
                if (readBits(1) != 0) {
                    pstArrayRead(state.persistant, Const.MAX_PERSISTANT);
                }

                // parse ammo
                if (readBits(1) != 0) {
                    pstArrayRead(state.ammo, Const.MAX_WEAPONS);
                }

                // parse powerups
                if (readBits(1) != 0) {
                    pstLongArrayRead(state.powerups, Const.MAX_POWERUPS);
                }
            }

            return true;
        }

        private void pstArrayRead (int[] arr, int maxbits) {
            int _bits = readBits(maxbits);
            for (int i = 0; i < maxbits; i++) {
                if ((_bits & BIT_POS[i]) != 0) {
                    arr[i] = readShort();
                }
            }
        }

        private void pstLongArrayRead (int[] arr, int maxbits) {
            int _bits = readBits(maxbits);
            for (int i = 0; i < maxbits; i++) {
                if ((_bits & BIT_POS[i]) != 0) {
                    arr[i] = readLong();
                }
            }
        }
    }




    public static int ANGLE2SHORT (float x) {
        return ((int)(x*65536.0f/360.0f)) & 65535;
    }

    public static float SHORT2ANGLE (int x) {
        return ((float)x*(360.0f/65536.0f));
    }


    public static Encoder encoder (int capacity) {
        return new Encoder(capacity);
    }
    public static Decoder decoder (byte[] data) {return new Decoder(data, data.length); }
    public static Decoder decoder (byte[] data, int limit) {return new Decoder(data, limit); }
    public static Decoder decoder (DemoMessage msg) {return new Decoder(msg.data(), msg.length()); }


//    public static void main(String[] args) {
//        System.out.println("Max path value = " + MAX_PATH_VALUE);
//        System.out.println(bits_len(0x0006));
//        System.out.println(bits_len(0x0036));
//
//        System.out.println(String.format("0x%08X", BIT_POS[7]));
//        System.out.println(String.format("0x%08X", BIT_POS_REV[7]));
//
//    }
}
