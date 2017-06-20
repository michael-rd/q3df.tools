package org.q3df.common.struct;

import org.q3df.common.Const;

import java.net.InetAddress;

/**
 * Simplified version of clientConnection_t struct, see client.h
 */
public class ClientConnection {
    int                     clientNum;
    int                     lastPacketSentTime;                     // for retransmits during connection
    int                     lastPacketTime;                         // for timeouts

    String       servername;         // name of server from original connect (used by reconnect)
    InetAddress  serverAddress;
    int          connectTime;                            // for connection retransmits
    int          connectPacketCount;                     // for display on connection dialog
    String       serverMessage;       // for display on connection dialog

    int                     challenge;                                      // from the server to use for connecting
    int                     checksumFeed;                           // from the server for checksum calculations

    // these are our reliable messages that go to the server
    int                     reliableSequence;
    int                     reliableAcknowledge;            // the last one the server has executed
    String            reliableCommands[] = new String[Const.MAX_RELIABLE_COMMANDS];

    // server message (unreliable) and command (reliable) sequence
    // numbers are NOT cleared at level changes, but continue to
    // increase as long as the connection is valid

    // message sequence is used by both the network layer and the
    // delta compression layer
    int                     serverMessageSequence;

    // reliable messages received from server
    int                     serverCommandSequence;
    int                     lastExecutedServerCommand;              // last server command grabbed or executed with CL_GetServerCommand
    String            serverCommands[] = new String [Const.MAX_RELIABLE_COMMANDS];

    // file transfer from server

    String          demoName;
    boolean        spDemoRecording;
    boolean        demorecording;
    boolean        demoplaying;
    boolean        demowaiting;    // don't record until a non-delta message is received
    boolean        firstDemoFrameSkipped;
    //fileHandle_t    demofile;

    int                     timeDemoFrames;         // counter of rendered frames
    int                     timeDemoStart;          // cls.realtime before first frame
    int                     timeDemoBaseTime;       // each frame will be at this time + frameNum * 50
    int                     timeDemoLastFrame;// time the last frame was rendered
    int                     timeDemoMinDuration;    // minimum frame duration
    int                     timeDemoMaxDuration;    // maximum frame duration
//    unsigned char   timeDemoDurations[ MAX_TIMEDEMO_DURATIONS ];    // log of frame durations

}
