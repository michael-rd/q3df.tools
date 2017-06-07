package org.q3df.test.demo;


import org.q3df.demo.Const;
import org.q3df.demo.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Created by Mike on 06.06.2017.
 */
public class MainScratch {


    public static String test_file = "lucy-vchrkn[df.vq3]00.44.048(MichaelRD.Russia).dm_68";

    private static Logger logger = LoggerFactory.getLogger(MainScratch.class);

    public static void parsePackets (ByteBuffer buffer) {
        //
        //  CL_ParseServerMessage
    }

    public static void main (String argv[]) {

        URL url = MainScratch.class.getResource("/demos/" + test_file);

        System.out.println(url);
        logger.debug("logger is working");

        try {
            InputStream is = url.openStream();
            ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dataBuffer = ByteBuffer.allocate(Const.MESSAGE_MAX_SIZE);
//            buffer.flip();

            int totalLen = 0;

            while (true) {
                buffer.clear();
                dataBuffer.clear();
                is.read(buffer.array(),0, 8);

//                logger.debug("buffer content: {}", Utils.toHex(buffer));

                totalLen += 8;

                int sequence = buffer.getInt();
                int msgLen = buffer.getInt();

                if (sequence == -1 && msgLen == -1) {
                    logger.debug("EOF is reached in a normal way");
                    break;
                }

//                logger.debug("read next message: sequence = {}, size={} ({} - {})", sequence, msgLen, Integer.toHexString(sequence), Integer.toHexString(msgLen));

                if (msgLen > Const.MESSAGE_MAX_SIZE || msgLen <= 0) {
                    logger.debug("Wow, msg-len is wrong, exit");
                    break;
                }

                int readBytes;
                if ((readBytes = is.read(dataBuffer.array(), 0, msgLen)) != msgLen) {
                    logger.debug("wtf? unable to read required {} number of bytes, in fact = {}", msgLen, readBytes);
                }
                else {
                    // parse packets from message buffer
                    parsePackets(dataBuffer);
                }

                totalLen += readBytes;
            }

            logger.debug("total length = {}", totalLen );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
