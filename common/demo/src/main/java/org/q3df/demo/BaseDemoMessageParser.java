package org.q3df.demo;

import org.q3df.common.Const;
import org.q3df.common.msg.En_SVC;
import org.q3df.common.msg.Q3HuffmanCoder;
import org.q3df.common.struct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.q3df.common.Const.MAX_GENTITIES;

/**
 * Created by michael on 20.06.17.
 */
public class BaseDemoMessageParser implements DemoMessageParser {

    private static Logger logger = LoggerFactory.getLogger(BaseDemoMessageParser.class);


    private ClientState client;
    private ClientConnection clc;

    public BaseDemoMessageParser() {
        this.clc = new ClientConnection();
        this.client = new ClientState();
    }


    private void parseGameState (Q3HuffmanCoder.Decoder decoder) {
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
                logger.debug("config string [{}] = {}", key, configString);

                client.gameState.put(key, configString);
            }
            else if (cmd == En_SVC.BASELINE) {
                int newnum = decoder.readBits(Const.GENTITYNUM_BITS);

                if (newnum < 0 || newnum >= MAX_GENTITIES) {
                    logger.error("Baseline number out of range: {}", newnum);
                    return;
                }

                EntityState es = client.entityBaselines[newnum];
                if (es == null) {
                    es = new EntityState();
                    client.entityBaselines[newnum] = es;
                }

                if (!decoder.readDeltaEntity(es, newnum)) {
                    logger.error("unable to parse delta-entity state");
                    return;
                }
                else
                    logger.debug("delta-entity parsed: {}", newnum);
            }
            else {
                logger.error("Parse GameState: bad command {} (byte : {})", cmd, cmdId);
                return;
            }
        }

        int clientNum = decoder.readLong();
        int checksumFeed = decoder.readLong();

        logger.debug("Parse GameState end: clientNum={}, checksumFeed={}", clientNum, checksumFeed);
    }

    private void parseCommandString (Q3HuffmanCoder.Decoder decoder) {
//        logger.debug("command = {}", En_SVC.SERVERCOMMAND);
        int sequence = decoder.readLong();
        String command = decoder.readString();

        logger.debug("server cmd, seq={}, cmd={}", sequence, command);

        if ( clc.serverCommandSequence >= sequence ) {
            return;
        }

        clc.serverCommandSequence = sequence;

        int index = sequence & (Const.MAX_RELIABLE_COMMANDS-1);
        clc.serverCommands[index] = command;
    }


    // CL_ParseSnapshot
    private void parseSnapshot (Q3HuffmanCoder.Decoder decoder) {
//        logger.debug("parse snapshot");
        CLSnapshot snapshot = new CLSnapshot();

        snapshot.serverTime = decoder.readLong();

        int deltaNum = decoder.readByte();

        if ( deltaNum == 0) {
            snapshot.deltaNum = -1;
        } else {
            snapshot.deltaNum = snapshot.messageNum - deltaNum;
        }

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


    @Override
    public boolean parse(DemoMessage message) {
        //
        //  CL_ParseServerMessage
        Q3HuffmanCoder.Decoder decoder = Q3HuffmanCoder.decoder(message);

        clc.reliableAcknowledge = decoder.readLong();

        if ( clc.reliableAcknowledge < clc.reliableSequence - Const.MAX_RELIABLE_COMMANDS ) {
            clc.reliableAcknowledge = clc.reliableSequence;
        }

//        logger.debug("r-ack = {}", reliableAcknowledge);

        while (!decoder.isEOD()) {

            int cmdId = decoder.readByte();
            En_SVC cmd = En_SVC.find(cmdId);
            if (cmd == null) {
//                logger.warn("unknown command = {}", cmdId);
                return true;
            }

            if (cmd == En_SVC.EOF) {
                logger.debug("end-of-message command");
                return true;
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
//                    logger.debug("command = {}", cmd);
                    parseSnapshot(decoder);
                    break;
                //   case DOWNLOAD:
                default:
                    logger.warn("wrong command {}", cmd);
                    return true;
            }
        }

        logger.debug("end of message packet");
        return true;
    }
}
