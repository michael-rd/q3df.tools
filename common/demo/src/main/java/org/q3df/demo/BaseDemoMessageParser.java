package org.q3df.demo;

import org.q3df.common.Const;
import org.q3df.common.msg.En_SVC;
import org.q3df.common.msg.Q3HuffmanCoder;
import org.q3df.common.struct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.q3df.common.Const.MAX_GENTITIES;

/**
 * Created by michael on 20.06.17.
 */
public class BaseDemoMessageParser implements DemoMessageParser {

    private static Logger logger = LoggerFactory.getLogger(BaseDemoMessageParser.class);


    private ClientState client;
    private ClientConnection clc;

    private int snapshots;
    String demoTimeCmd;
//    private int frames;

    public BaseDemoMessageParser() {
        this.clc = new ClientConnection();
        this.client = new ClientState();
    }


    private void parseGameState(Q3HuffmanCoder.Decoder decoder) {

        clc.connectPacketCount = 0;

        // wipe local client state
        // it sets all fields of client state to zero-value (memset)
//        CL_ClearState();


        clc.serverCommandSequence = decoder.readLong();

        while (true) {
            int cmdId = decoder.readByte();

            En_SVC cmd = En_SVC.find(cmdId);

            if (cmd == null) {
//                logger.error("unknown command {}", cmdId);
                return;
            }

            if (cmd == En_SVC.EOF) {
//                logger.debug("EOF gamestate");
                break;
            }

            if (cmd == En_SVC.CONFIGSTRING) {
                int key = decoder.readShort();
                if (key < 0 || key > Const.MAX_CONFIGSTRINGS) {
//                    logger.debug("wrong config string key {}", key);
                    return;
                }

                String configString = decoder.readBigString();
               // logger.debug("config string [{}] = {}", key, configString);

//                if (key == 0) {
//                    if (!configString.contains("version\\iodfe_dfwc2017"))
//                        System.out.print(" - !invalid client engine");
//                    else
//                        System.out.print(".. ok");
//                }

                client.gameState.put(key, configString);
            } else if (cmd == En_SVC.BASELINE) {
                int newnum = decoder.readBits(Const.GENTITYNUM_BITS);

                if (newnum < 0 || newnum >= MAX_GENTITIES) {
//                    logger.error("Baseline number out of range: {}", newnum);
                    return;
                }

                EntityState es = client.entityBaselines[newnum];
                if (es == null) {
                    es = new EntityState();
                    client.entityBaselines[newnum] = es;
                }

                if (!decoder.readDeltaEntity(es, newnum)) {
//                    logger.error("unable to parse delta-entity state");
                    return;
                }
//                else
//                    logger.debug("delta-entity parsed: {}", newnum);
            } else {
//                logger.error("Parse GameState: bad command {} (byte : {})", cmd, cmdId);
                return;
            }
        }

        clc.clientNum = decoder.readLong();
        clc.checksumFeed = decoder.readLong();

//        logger.debug("Parse GameState end: clientNum={}, checksumFeed={}", clc.clientNum, clc.checksumFeed);
    }

//    Pattern
    private void parseCommandString(Q3HuffmanCoder.Decoder decoder) {
//        logger.debug("command = {}", En_SVC.SERVERCOMMAND);
        int sequence = decoder.readLong();
        String command = decoder.readString();

        //logger.debug("server cmd, seq={}, cmd={}", sequence, command);

        if (command.startsWith("print \"Time performed")) {
            demoTimeCmd = command;
        }

        if (clc.serverCommandSequence >= sequence) {
            return;
        }

        clc.serverCommandSequence = sequence;

        int index = sequence & (Const.MAX_RELIABLE_COMMANDS - 1);
        clc.serverCommands[index] = command;
    }


    // CL_ParseSnapshot
    private void parseSnapshot(Q3HuffmanCoder.Decoder decoder) {
//        logger.debug("parse snapshot");
        snapshots++;
        CLSnapshot newSnap = new CLSnapshot();
        CLSnapshot old = null;
        newSnap.serverCommandNum = clc.serverCommandSequence;
        newSnap.serverTime = decoder.readLong();

        newSnap.messageNum = clc.serverMessageSequence;

        int deltaNum = decoder.readByte();

        if (deltaNum == 0) {
            newSnap.deltaNum = -1;
        } else {
            newSnap.deltaNum = newSnap.messageNum - deltaNum;
        }

        newSnap.snapFlags = decoder.readByte();

        // If the frame is delta compressed from data that we
        // no longer have available, we must suck up the rest of
        // the frame, but not use it, then ask for a non-compressed
        // message
        if (newSnap.deltaNum <= 0) {
            newSnap.valid = true;          // uncompressed frame
            old = null;
            clc.demowaiting = false;       // we can start recording now
        } else {
            old = client.snapshots[newSnap.deltaNum & Const.PACKET_MASK];
            if (old == null || !old.valid) {
                // should never happen
//                logger.error("Delta from invalid frame (not supposed to happen!)");
            } else if (old.messageNum != newSnap.deltaNum) {
                // The frame that the server did the delta from
                // is too old, so we can't reconstruct it properly.
//                logger.error("Delta frame too old.");
            } else if ((client.parseEntitiesNum - old.parseEntitiesNum) > (Const.MAX_PARSE_ENTITIES - 128)) {
//                logger.error("Delta parseEntitiesNum too old");
            } else {
                newSnap.valid = true;  // valid delta parse
            }
        }

        // read areamask
        int len = decoder.readByte();

        if (len > newSnap.areamask.length) {
//            logger.error("CL_ParseSnapshot: Invalid size {} for areamask", len);
            return;
        }

        decoder.readData(newSnap.areamask, len);

// ORIGINAL CODE
//        if ( old ) {
//            MSG_ReadDeltaPlayerstate( msg, &old->ps, &newSnap.ps );
//        } else {
//            MSG_ReadDeltaPlayerstate( msg, NULL, &newSnap.ps );
//        }

        // translated code
        // we really don't need to translate delta-state for the player
        decoder.readDeltaPlayerState(newSnap.ps);

        //CL_ParsePacketEntities( msg, old, &newSnap );
        parsePacketEntities(decoder, old, newSnap);

        // if not valid, dump the entire thing now that it has
        // been properly read
        if ( !newSnap.valid ) {
            return;
        }

        // clear the valid flags of any snapshots between the last
        // received and this one, so if there was a dropped packet
        // it won't look like something valid to delta from next
        // time we wrap around in the buffer
        int oldMessageNum = client.snap.messageNum + 1;

        if ( newSnap.messageNum - oldMessageNum >= Const.PACKET_BACKUP ) {
            oldMessageNum = newSnap.messageNum - ( Const.PACKET_BACKUP - 1 );
        }
        for ( ; oldMessageNum < newSnap.messageNum ; oldMessageNum++ ) {
            client.snapshots[oldMessageNum & Const.PACKET_MASK].valid = false;
        }

        // copy to the current good spot
        client.snap = newSnap;
    // skip ping calculations
        client.snap.ping = 0;

        // save the frame off in the backup array for later delta comparisons
        client.snapshots[client.snap.messageNum & Const.PACKET_MASK] = client.snap;

        //logger.debug("CL-snapshot: {}, delta: {}, ping: {}", snapshot.messageNum, snapshot.deltaNum, snapshot.ping);

        client.newSnapshots = true;
    }

    private void parsePacketEntities(Q3HuffmanCoder.Decoder decoder, CLSnapshot oldframe, CLSnapshot newframe) {
        newframe.parseEntitiesNum = client.parseEntitiesNum;
        newframe.numEntities = 0;
        int newnum = 0;
        int oldindex = 0;
        int oldnum = 0;
        EntityState oldstate = null;

        if (oldframe == null) {
            oldnum = 99999;
        } else {
            if (oldindex >= oldframe.numEntities) {
                oldnum = 99999;
            } else {
                oldstate = client.parseEntities[
                        (oldframe.parseEntitiesNum + oldindex) & (Const.MAX_PARSE_ENTITIES - 1)];
                oldnum = oldstate.number;
            }
        }

        while (true) {
            newnum = decoder.readBits(Const.GENTITYNUM_BITS);

            if ( newnum == (MAX_GENTITIES-1) ) {
                break;
            }

            if (decoder.isEOD()) {
//                logger.error ("ERR_DROP, CL_ParsePacketEntities: end of message");
                return;
            }

            while ( oldnum < newnum ) {
                // one or more entities from the old packet are unchanged
                CL_DeltaEntity( decoder, newframe, oldnum, oldstate, true );

                oldindex++;

                if ( oldindex >= oldframe.numEntities ) {
                    oldnum = 99999;
                } else {
                    oldstate = client.parseEntities[
                            (oldframe.parseEntitiesNum + oldindex) & (Const.MAX_PARSE_ENTITIES-1)];
                    oldnum = oldstate.number;
                }
            }

            if (oldnum == newnum) {
                // delta from previous state
                CL_DeltaEntity( decoder, newframe, newnum, oldstate, false );

                oldindex++;

                if ( oldindex >= oldframe.numEntities ) {
                    oldnum = 99999;
                } else {
                    oldstate = client.parseEntities[
                            (oldframe.parseEntitiesNum + oldindex) & (Const.MAX_PARSE_ENTITIES-1)];
                    oldnum = oldstate.number;
                }
                continue;
            }

            if ( oldnum > newnum ) {
                // delta from baseline
                CL_DeltaEntity( decoder, newframe, newnum, client.entityBaselines[newnum], false );
                continue;
            }
        }

        // any remaining entities in the old frame are copied over
        while ( oldnum != 99999 ) {
            // one or more entities from the old packet are unchanged
            CL_DeltaEntity( decoder, newframe, oldnum, oldstate, true );

            oldindex++;

            if ( oldindex >= oldframe.numEntities ) {
                oldnum = 99999;
            } else {
                oldstate = client.parseEntities[
                        (oldframe.parseEntitiesNum + oldindex) & (Const.MAX_PARSE_ENTITIES-1)];
                oldnum = oldstate.number;
            }
        }
    }

    private void CL_DeltaEntity (Q3HuffmanCoder.Decoder decoder, CLSnapshot frame, int newnum, EntityState old,
                         boolean unchanged) {
        EntityState   state;

        // save the parsed entity state into the big circular buffer so
        // it can be used as the source for a later delta
        state = client.parseEntities[client.parseEntitiesNum & (Const.MAX_PARSE_ENTITIES-1)];

        if ( unchanged ) {
            state.copy(old);
        } else {
            decoder.readDeltaEntity(state, newnum );
        }

        if ( state.number == (MAX_GENTITIES-1) ) {
            return;         // entity was delta removed
        }
        client.parseEntitiesNum++;
        frame.numEntities++;
    }


    @Override
    public boolean parse(DemoMessage message) {
        //
        //  CL_ParseServerMessage
        Q3HuffmanCoder.Decoder decoder = Q3HuffmanCoder.decoder(message);

        clc.reliableAcknowledge = decoder.readLong();

        if (clc.reliableAcknowledge < clc.reliableSequence - Const.MAX_RELIABLE_COMMANDS) {
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
//                logger.debug("end-of-message command");
                return true;
            }

            switch (cmd) {
                // case NOP:
                case SERVERCOMMAND:
                    parseCommandString(decoder);
                    break;
                case GAMESTATE:
//                    logger.debug("command = {}", cmd);
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

//        logger.debug("end of message packet");
        return true;
    }

    public DemoDataFacade getDemoData () {
        return new DemoDataFacade(client.gameState, snapshots, demoTimeCmd);
    }
}
