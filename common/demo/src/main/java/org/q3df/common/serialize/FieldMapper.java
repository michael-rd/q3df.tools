package org.q3df.common.serialize;

import org.q3df.common.msg.Q3HuffmanCoder;

public interface FieldMapper<E> {
    String name();
    void read (Q3HuffmanCoder.Decoder decoder, E state);
    void reset (E state);
}
