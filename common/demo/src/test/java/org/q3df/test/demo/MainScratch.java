package org.q3df.test.demo;


import org.q3df.common.Const;
import org.q3df.common.msg.En_SVC;
import org.q3df.common.msg.Q3HuffmanCoder;
import org.q3df.common.struct.CLSnapshot;
import org.q3df.common.struct.EntityState;
import org.q3df.common.struct.PlayerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static org.q3df.common.Const.MAX_GENTITIES;

/**
 * Created by Mike on 06.06.2017.
 */
public class MainScratch {


    public static String test_file = "lucy-vchrkn[df.vq3]00.44.048(MichaelRD.Russia).dm_68";

    private static Logger logger = LoggerFactory.getLogger(MainScratch.class);

    public static void parseGameState (Q3HuffmanCoder.Decoder decoder) {
        int serverCmdSequence = decoder.readLong();

        while (true) {
            int cmdId = decoder.readByte();

            En_SVC cmd = En_SVC.find(cmdId);

            if (cmd == null) {
                logger.error ("unknown command {}", cmdId);
                return;
            }

            if (cmd == En_SVC.EOF) {
                logger.debug("EOF gamestate");
                break;
            }

            if (cmd == En_SVC.CONFIGSTRING) {
                int key = decoder.readShort();
                if (key < 0 || key > Const.MAX_CONFIGSTRINGS) {
                    logger.debug("wrong config string key {}", key);
                    return;
                }

                String configString = decoder.readBigString();

// @FIXME add check in java
//                if ( len + 1 + cl.gameState.dataCount > MAX_GAMESTATE_CHARS ) {
//                    Com_Error( ERR_DROP, "MAX_GAMESTATE_CHARS exceeded" );
//                }

                logger.debug("config string [{}] = {}", key, configString);
            }
            else if (cmd == En_SVC.BASELINE) {
                int newnum = decoder.readBits(Const.GENTITYNUM_BITS);

                if (newnum < 0 || newnum >= MAX_GENTITIES) {
                    logger.error("Baseline number out of range: {}", newnum);
                    return;
                }

                EntityState es = new EntityState();
                if (!decoder.readDeltaEntity(es, newnum)) {
                    logger.error("unable to parse delta-entity state");
                    return;
                }
                else
                    logger.debug("delta-entity parsed: {}", newnum);
            }
            else {
                logger.error("Parse GameState: bad command {}", cmd);
                return;
            }
        }

        int clientNum = decoder.readLong();
        int checksumFeed = decoder.readLong();

        logger.debug("Parse GameState end: clientNum={}, checksumFeed={}", clientNum, checksumFeed);
    }


    private static void parseCommandString (Q3HuffmanCoder.Decoder decoder) {
//        logger.debug("command = {}", En_SVC.SERVERCOMMAND);
        int sequence = decoder.readLong();
        String command = decoder.readString();

        logger.debug("server cmd, seq={}, cmd={}", sequence, command);
    }


    // CL_ParseSnapshot
    private static void parseSnapshot (Q3HuffmanCoder.Decoder decoder) {
        logger.debug("parse snapshot");
        CLSnapshot snapshot = new CLSnapshot();

        snapshot.serverTime = decoder.readLong();

        int deltaNum = decoder.readByte();

        snapshot.snapFlags = decoder.readByte();

        // read areamask
        int len = decoder.readByte();

        if (len > snapshot.areamask.length) {
            logger.error("CL_ParseSnapshot: Invalid size {} for areamask", len);
            return;
        }

        decoder.readData(snapshot.areamask, len);
        decoder.readDeltaPlayerState (new PlayerState());

        //CL_ParsePacketEntities( msg, old, &newSnap );

        //logger.debug("CL-snapshot: {}, delta: {}, ping: {}", snapshot.messageNum, snapshot.deltaNum, snapshot.ping);
    }

    public static void parsePackets (byte[] msgBuffer, int msgLen) {
        //
        //  CL_ParseServerMessage
        Q3HuffmanCoder.Decoder decoder = Q3HuffmanCoder.decoder(msgBuffer, msgLen);

        int reliableAcknowledge = decoder.readLong();
//        logger.debug("r-ack = {}", reliableAcknowledge);

        while (!decoder.isEOD()) {

            int cmdId = decoder.readByte();
            En_SVC cmd = En_SVC.find(cmdId);
            if (cmd == null) {
                logger.warn("unknown command = {}", cmdId);
                return;
            }

            if (cmd == En_SVC.EOF) {
                logger.debug("end-of-message command");
                return;
            }

            switch (cmd) {
               // case NOP:
                case SERVERCOMMAND:
                    parseCommandString (decoder);
                    break;
                case GAMESTATE:
                    logger.debug("command = {}", cmd);
                    parseGameState(decoder);
                    break;
                case SNAPSHOT:
                    logger.debug("command = {}", cmd);
                    parseSnapshot(decoder);
                    break;
             //   case DOWNLOAD:
                default:
                        logger.warn("wrong command {}", cmd);
                        return;
            }
        }

        logger.debug("end of message packet");
    }

    public static void main (String argv[]) {

        URL url = MainScratch.class.getResource("/demos/" + test_file);

        System.out.println(url);
        logger.debug("logger is working");

        try {
            InputStream is = url.openStream();
            ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            //ByteBuffer dataBuffer = ByteBuffer.allocate(Const.MESSAGE_MAX_SIZE);
            byte[] msgBuffer = new byte[Const.MESSAGE_MAX_SIZE];
//            buffer.flip();

            int totalLen = 0;

            while (true) {
                buffer.clear();
//                dataBuffer.clear();
                is.read(buffer.array(),0, 8);
                Arrays.fill(msgBuffer, (byte)0);

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
                if ((readBytes = is.read(msgBuffer, 0, msgLen)) != msgLen) {
                    logger.debug("wtf? unable to read required {} number of bytes, in fact = {}", msgLen, readBytes);
                }
                else {
                    // parse packets from message buffer
                    parsePackets(msgBuffer, msgLen);
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
