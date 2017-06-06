package org.q3df.demo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by michael on 06.06.17.
 */
public class StreamReader {

    private ByteBuffer buf;
    private ReadableByteChannel channel;

    StreamReader (ReadableByteChannel channel) {
        this.buf = ByteBuffer.allocate(Const.MESSAGE_MAX_SIZE);
        this.buf.order(ByteOrder.LITTLE_ENDIAN);

        this.channel = channel;
    }

    public int readInt () throws IOException {
        if (this.buf.remaining() < 4) {
            this.buf.clear();
        }

        this.channel.read(this.buf);
        return this.buf.getInt();
    }


    public int skip (int bytes) {
//        while (bytes > 0) {
//
//            if ()
//
//            this.channel.
//        }
        throw new RuntimeException("operation is not implemented yet");
    }
}
