package org.q3df.common.struct;

import org.q3df.common.Const;

/**
 * Simplified version of clientConnection_t struct, see client.h
 */
public class ClientConnection {
    public int clientNum;
    public int connectPacketCount;                     // for display on connection dialog

    public int checksumFeed;                           // from the server for checksum calculations

    // these are our reliable messages that go to the server
    public int reliableSequence;
    public int reliableAcknowledge;            // the last one the server has executed
//    public String reliableCommands[] = new String[Const.MAX_RELIABLE_COMMANDS];

    // server message (unreliable) and command (reliable) sequence
    // numbers are NOT cleared at level changes, but continue to
    // increase as long as the connection is valid

    // message sequence is used by both the network layer and the
    // delta compression layer
    public int serverMessageSequence;

    // reliable messages received from server
    public int serverCommandSequence;
    public int lastExecutedServerCommand;              // last server command grabbed or executed with CL_GetServerCommand
    public String serverCommands[] = new String[Const.MAX_RELIABLE_COMMANDS];


    public boolean demowaiting;    // don't record until a non-delta message is received
}
