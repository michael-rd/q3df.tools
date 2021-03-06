package org.q3df.common.msg;

public class Q3Huffman {

    public static final int EOF_SYM = 0xFFFFFFFF;

    public static final class HuffSymbol {
        private int bitsLen;
        private int charVal;
        private int huffBits;

        public HuffSymbol(int charVal, int huffBits, int bitsLen) {
            this.bitsLen = bitsLen;
            this.charVal = charVal;
            this.huffBits = huffBits;
        }

        public int getBitsLen() {
            return bitsLen;
        }

        public int getCharVal() {
            return charVal;
        }

        public int getHuffBits() {
            return huffBits;
        }

        public boolean isValid () {
            return this.charVal != EOF_SYM;
        }

        public static HuffSymbol createFromPath (int huffpath, int charVal) {
            int len = bits_len (huffpath);
            int mask = (0x7FFFFFFF >> (31 - len));
            int huffBits =  mask & huffpath;
            return new HuffSymbol(charVal, huffBits, len);
        }
    }


    public static boolean writeSym (int val, BitStreamWriter writer) {
        HuffSymbol huffSymbol = sym[val & 0xFF];
        return writer.writeBits(huffSymbol.huffBits, huffSymbol.bitsLen);
    }

    public static HuffSymbol readSym (BitStreamReader reader) {
        int path_mask = 0b10;
        int bit = 1;
        int rest = 0;
        int path = path_mask;

        while (true) {
            rest = rest | (reader.readBit() != 0 ? bit : 0);
            path = path_mask | rest;
            if (path > MAX_PATH_VALUE)
                return NOT_FOUND;

            HuffSymbol sym = sym_table [path];

            if (sym != null)
                return sym;

            path_mask <<= 1;
            bit <<= 1;
        }
    }


    public static HuffSymbol byteSymbol (int byteVal) {
        return sym[byteVal & 0xFF];
    }

    public static HuffSymbol findSymbol (int bitsValue, int bitsLen) {
        int idx =  bitsValue | (1 << bitsLen);
        if (idx > MAX_PATH_VALUE) {
            return NOT_FOUND;
        }
        else
            return sym_table[idx];
    }

    static int MAX_PATH_VALUE = 0;
    static int MAX_PATH_LEN = 0;

    static final HuffSymbol sym[];
    static final HuffSymbol sym_table[] ;
    static final HuffSymbol NOT_FOUND = new HuffSymbol(EOF_SYM, EOF_SYM, 32);


    static {
        int huff_paths[] = {
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

        sym = new HuffSymbol[256];
        MAX_PATH_VALUE = 0;
        MAX_PATH_LEN = 0;
        for (int i = 0; i< huff_paths.length; i++) {
            MAX_PATH_VALUE = Math.max(MAX_PATH_VALUE, huff_paths[i]);
            sym[i] = HuffSymbol.createFromPath(huff_paths[i], i);
            MAX_PATH_LEN = Math.max(MAX_PATH_LEN, sym[i].getBitsLen());
        }
        sym_table = new HuffSymbol[MAX_PATH_VALUE+1];
        for (int i = 0; i < huff_paths.length; i++) {
            sym_table[huff_paths[i]] = sym[i];
        }
    }

    private static int bits_len (int path) {
        int size = 0;
        while (path > 1) {
            size++;
            path >>= 1;
        }
        return size;
    }
}
