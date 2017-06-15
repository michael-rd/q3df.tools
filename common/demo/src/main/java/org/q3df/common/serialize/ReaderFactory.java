package org.q3df.common.serialize;

import org.q3df.common.Const;
import org.q3df.common.Utils;
import org.q3df.common.struct.TrType;

public class ReaderFactory {

    public static ValueReader<Integer> longReader () {
        return intBitsReader(32);
    }

    public static ValueReader<Integer> shortReader () {
        return intBitsReader(16);
    }

    public static ValueReader<Integer> byteReader () {
        return intBitsReader(8);
    }

    public static ValueReader<Integer> intBitsReader (final int size) {
        return decoder -> decoder.readBits(size);
    }

    public static ValueReader<TrType> trTypeValueReader () {
        return decoder -> Utils.enumByOrdinal(TrType.class, decoder.readByte());
    }

    public static ValueReader<Float> floatReader () {
        return decoder -> decoder.readFloat();
    }

    // encoded by scheme: full or 13-bit float value, depends on first bit value
    public static ValueReader<Float> vectValueReader () {
/*
  this is part from original code (iodfe)
  if ( field->bits == 0 ) {
        // float
        if ( MSG_ReadBits( msg, 1 ) == 0 ) {
                                                *(float *)toF = 0.0f;
        } else {
            if ( MSG_ReadBits( msg, 1 ) == 0 ) {
                // integral float
                trunc = MSG_ReadBits( msg, FLOAT_INT_BITS );
                // bias to allow equal parts positive and negative
                trunc -= FLOAT_INT_BIAS;
                *(float *)toF = trunc;
                if ( print ) {
                    Com_Printf( "%s:%i ", field->name, trunc );
                }
            } else {
                // full floating point value
                                                *toF = MSG_ReadBits( msg, 32 );
                if ( print ) {
                    Com_Printf( "%s:%f ", field->name, *(float *)toF );
                }
            }
        }
    } ...
  */
        return decoder -> {
            if (decoder.readBits(1) == 0)
                return 0.0f;

            if (decoder.readBits(1) == 0) {
                int trunc = decoder.readBits(Const.FLOAT_INT_BITS);
                trunc -= Const.FLOAT_INT_BIAS;
                return Float.intBitsToFloat(trunc);
            }
            else {
                return decoder.readFloat();
            }
        };
    }
}
