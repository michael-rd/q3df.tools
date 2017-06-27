package org.q3df.common.msg;

import org.q3df.common.Const;
import org.q3df.common.serialize.FieldMapper;
import org.q3df.common.serialize.FieldMapperFactory;
import org.q3df.common.struct.EntityState;
import org.q3df.common.struct.PlayerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.q3df.common.Const.DOT_CHAR_BYTE;
import static org.q3df.common.Const.PERCENT_CHAR_BYTE;

public class MsgStreamFactory {

    private static Logger logger = LoggerFactory.getLogger(MsgStreamFactory.class);

    public static BitStream bitStream (int bytes) {
        return new BitStream(new byte[bytes], bytes*8);
    }

    public static BitStreamReader bitsReader (byte[] data, int bytes) {
        return new BitStream(data, bytes*8);
    }

    public static BitStreamWriter bitsWriter (byte[] data, int bytes) {
        return new BitStream(data, bytes*8);
    }

    public static MessageDataReader msgReader (byte[] data) {
        return msgReader(data, data.length);
    }

    public static MessageDataReader msgReader (byte[] data, int length) {
        return new HuffMessageDataReader (bitsReader(data, length));
    }

    public static MessageDataReader msgReader (BitStreamReader reader) {
        return new HuffMessageDataReader(reader);
    }

    public static MessageDataWriter msgWriter (byte[] data) {
        return msgWriter(data, data.length);
    }

    public static MessageDataWriter msgWriter (byte[] data, int length) {
        return new HuffMessageDataWriter(bitsWriter(data, length));
    }

    public static MessageDataWriter msgWriter (BitStreamWriter writer) {
        return new HuffMessageDataWriter(writer);
    }



    public static class HuffMessageDataWriter implements MessageDataWriter {

        private BitStreamWriter bitStreamWriter;

        public HuffMessageDataWriter (BitStreamWriter writer) {
            this.bitStreamWriter = writer;
        }

        @Override
        public boolean isEOD() {
            return bitStreamWriter.isEOWR();
        }

        @Override
        public boolean writeBits(int value, int bits) {
            if ((bitStreamWriter.getSize() - bitStreamWriter.getWriteBitPos()/8) < 4)
                return false;

            if ( bits == 0 || bits < -31 || bits > 32 )
                throw new RuntimeException(String.format("MSG_WriteBits: bad bits %i", bits));

            if (bits < 0)
                bits = -1 * bits;

            value &= (0xffffffff>>(32-bits));

            int fractionBits = bits & 7;

            if (fractionBits > 0) {
                // fraction bits
                bitStreamWriter.writeBits (value, fractionBits);
                bits -= fractionBits;
                value = value >> fractionBits;
            }

            while (bits > 0) {
                if (!Q3Huffman.writeSym(value, bitStreamWriter))
                    return false;

                bits -= 8;
                value >>= 8;
            }

            return true;
        }

        @Override
        public boolean writeStringBase(String str, int limit) {
            if (str == null || str.isEmpty())
                return Q3Huffman.writeSym(0, bitStreamWriter);

            byte[] data = str.getBytes();

            if (data.length >= limit) {
                logger.debug("MSG_WriteString: MAX_STRING_CHARS = {}", limit);
                return Q3Huffman.writeSym(0, bitStreamWriter);
            }

            for (int i = 0; i < data.length; i++)
                if (data[i] > 127 || data[i] == Const.PERCENT_CHAR_BYTE)
                    data[i] = Const.DOT_CHAR_BYTE;

            return writeData(data, limit-1) && Q3Huffman.writeSym(0, bitStreamWriter);
        }
    }


    public static class HuffMessageDataReader implements MessageDataReader {

        private BitStreamReader bitStreamReader;

        public HuffMessageDataReader(BitStreamReader bitStreamReader) {
            this.bitStreamReader = bitStreamReader;
        }

        @Override
        public boolean isEOD() {
            return bitStreamReader.isEORD();
        }

        @Override
        public int readBits(int bits) {
            int value = 0;
            boolean neg = bits < 0;

            if (neg)
                bits = bits*-1;

            int fragmentBits = bits & 7;

            if (fragmentBits != 0) {
                value = bitStreamReader.readBits(fragmentBits);
                bits -= fragmentBits;
            }

            if (bits > 0) {
                int decoded = 0;
                for (int i = 0; i < bits; i+=8) {
                    Q3Huffman.HuffSymbol sym = Q3Huffman.readSym(bitStreamReader);
                    if (!sym.isValid())
                        return -1;

                    decoded |= (sym.getCharVal() << i);
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

        @Override
        public int readByte() {
            return Q3Huffman.readSym(bitStreamReader).getCharVal();
        }

        @Override
        public String readStringBase(int limit, boolean stopAtNewLine) {
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


        @Override
        public boolean readDeltaEntity(EntityState state, int number) {
            // check for a remove
//  orig:   if ( MSG_ReadBits( msg, 1 ) == 1 ) {
            if (bitStreamReader.readBit() == 1) {
                state.number = Const.MAX_GENTITIES - 1;
                // clear state and return
                return true;
            }

            // check for no delta
            if (bitStreamReader.readBit() == 0) {
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
                if (bitStreamReader.readBit() == 0) {
                    //no change
                    continue;
                }

                FieldMapper mapper = FieldMapperFactory.entStateFieldMapper(i);

                if (bitStreamReader.readBit() == 0)
                    mapper.reset(state);
                else
                    mapper.read(this, state);
            }

            return true;
        }


        @Override
        public boolean readDeltaPlayerState(PlayerState state) {
            int lc = Q3Huffman.readSym(bitStreamReader).getCharVal();

            if (lc < 0 || lc > FieldMapperFactory.getPlayerStateFieldNum()) {
                logger.error("invalid playerState field count: {}", lc);
                return false;
            }

            for (int i = 0; i < lc; i++) {
                if (bitStreamReader.readBit() == 0) {
                    // no change;
                    continue;
                }
                FieldMapperFactory.playerStateFieldMapper(i).read(this, state);
            }

            // read arrays
            if (bitStreamReader.readBit() != 0) {

                //parse stats
                if (bitStreamReader.readBit() != 0) {
                    pstArrayRead(state.stats, Const.MAX_STATS);
                }

                // parse persistant stats
                if (bitStreamReader.readBit() != 0) {
                    pstArrayRead(state.persistant, Const.MAX_PERSISTANT);
                }

                // parse ammo
                if (bitStreamReader.readBit() != 0) {
                    pstArrayRead(state.ammo, Const.MAX_WEAPONS);
                }

                // parse powerups
                if (bitStreamReader.readBit() != 0) {
                    pstLongArrayRead(state.powerups, Const.MAX_POWERUPS);
                }
            }

            return true;
        }

        private void pstArrayRead (int[] arr, int maxbits) {
            int _bits = readBits(maxbits);
            int _mask = 1;
            for (int i = 0; i < maxbits; i++) {
                if ((_bits & _mask) != 0) {
                    arr[i] = readShort();
                }
                _mask<<=1;
            }
        }

        private void pstLongArrayRead (int[] arr, int maxbits) {
            int _bits = readBits(maxbits);
            int _mask = 1;
            for (int i = 0; i < maxbits; i++) {
                if ((_bits & _mask) != 0) {
                    arr[i] = readLong();
                }

                _mask <<= 1;
            }
        }

    }
}
