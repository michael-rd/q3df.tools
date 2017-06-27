package org.q3df.common.serialize;

import org.q3df.common.msg.MessageDataReader;
import org.q3df.common.msg.Q3HuffmanCoder;

public interface ValueReader<T> {
    T read(Q3HuffmanCoder.Decoder decoder);
    T read(MessageDataReader reader);
}
