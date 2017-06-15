package org.q3df.common.serialize;

import org.q3df.demo.Q3HuffmanCoder;

public interface ValueReader<T> {
    T read(Q3HuffmanCoder.Decoder decoder);
}
