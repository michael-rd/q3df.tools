package org.q3df.common.serialize;

import org.q3df.common.struct.EntityState;
import org.q3df.demo.Q3HuffmanCoder;

public interface FieldMapper {
    int id();
    String name();
    void read (Q3HuffmanCoder.Decoder decoder, EntityState state);
}
