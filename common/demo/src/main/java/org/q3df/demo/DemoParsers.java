package org.q3df.demo;

import org.q3df.common.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DemoParsers {

    private static Logger logger = LoggerFactory.getLogger(DemoParsers.class);



    public static boolean parse (URL url, DemoMessageParser p) throws IOException {
        try (InputStream is = url.openStream()){
            return parse(is, p);
        }
    }

    public static boolean parse (InputStream is, DemoMessageParser p) throws IOException {
        return new InputStreamMessageWalker(is, p).parse();
    }



    public interface DemoStreamParser extends AutoCloseable, Closeable{

        boolean parse () throws IOException;

    }


    public static class InputStreamMessageWalker implements DemoStreamParser {

        private InputStream inputStream;
        private DemoMessageParser parser;
        private ByteBuffer buffer;
        private DemoMessage message;
        private long totalRead;

        public InputStreamMessageWalker(InputStream inputStream, DemoMessageParser parser) {
            this.inputStream = inputStream;
            this.parser = parser;
            this.buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            this.message = new DemoMessage();
        }

        @Override
        public boolean parse() throws IOException {

            while (this.inputStream.read(buffer.array(), 0, 8) == 8) {

//                logger.debug("buffer content: {}", Utils.toHex(buffer));

                totalRead += 8;

                int sequence = buffer.getInt();
                int msgLen = buffer.getInt();

                if (sequence == -1 && msgLen == -1) {
                    logger.debug("EOF is reached in a normal way");
                    break;
                }

//                logger.debug("read next message: sequence = {}, size={} ({} - {})", sequence, msgLen, Integer.toHexString(sequence), Integer.toHexString(msgLen));

                if (msgLen > Const.MESSAGE_MAX_SIZE || msgLen <= 0) {
                    logger.debug("Wow, msg-len is wrong, exit");
                    return false;
                }

                message.resize(msgLen);

                int readBytes;
                if ((readBytes = inputStream.read(message.data(), 0, msgLen)) != msgLen) {
                    logger.debug("wtf? unable to read required {} number of bytes, in fact = {}", msgLen, readBytes);
                }
                else {
                    // parse packets from message buffer
                    //parsePackets(msgBuffer, msgLen, clientState);
                    if (!this.parser.parse(message)) {
                        return false;
                    }
                }

                totalRead += readBytes;
                buffer.clear();
            }

            logger.debug("total read = {}", totalRead );

            return false;
        }


        public void close () {
            this.buffer = null;
            this.inputStream = null;
            this.parser = null;
            this.message = null;
        }
    }
}
