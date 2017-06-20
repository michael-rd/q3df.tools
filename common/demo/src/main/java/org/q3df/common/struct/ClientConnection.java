package org.q3df.common.struct;

import org.q3df.common.Const;

import java.net.InetAddress;

/**
 * Simplified version of clientConnection_t struct, see client.h
 */
public class ClientConnection {
    public int                     clientNum;
    public int                     lastPacketSentTime;                     // for retransmits during connection
    public int                     lastPacketTime;                         // for timeouts

    public String       servername;         // name of server from original connect (used by reconnect)
    public InetAddress  serverAddress;
    public int          connectTime;                            // for connection retransmits
    public int          connectPacketCount;                     // for display on connection dialog
    public String       serverMessage;       // for display on connection dialog

    public int                     challenge;                                      // from the server to use for connecting
    public int                     checksumFeed;                           // from the server for checksum calculations

    // these are our reliable messages that go to the server
    public int                     reliableSequence;
    public int                     reliableAcknowledge;            // the last one the server has executed
    public String            reliableCommands[] = new String[Const.MAX_RELIABLE_COMMANDS];

    // server message (unreliable) and command (reliable) sequence
    // numbers are NOT cleared at level changes, but continue to
    // increase as long as the connection is valid

    // message sequence is used by both the network layer and the
    // delta compression layer
    public int                     serverMessageSequence;

    // reliable messages received from server
    public int                     serverCommandSequence;
    public int                     lastExecutedServerCommand;              // last server command grabbed or executed with CL_GetServerCommand
    public String            serverCommands[] = new String [Const.MAX_RELIABLE_COMMANDS];

    // file transfer from server

    public String          demoName;
    public boolean        spDemoRecording;
    public boolean        demorecording;
    public boolean        demoplaying;
    public boolean        demowaiting;    // don't record until a non-delta message is received
    public boolean        firstDemoFrameSkipped;
    //fileHandle_t    demofile;

    public int                     timeDemoFrames;         // counter of rendered frames
    public int                     timeDemoStart;          // cls.realtime before first frame
    public int                     timeDemoBaseTime;       // each frame will be at this time + frameNum * 50
    public int                     timeDemoLastFrame;// time the last frame was rendered
    public int                     timeDemoMinDuration;    // minimum frame duration
    public int                     timeDemoMaxDuration;    // maximum frame duration
//    unsigned char   timeDemoDurations[ MAX_TIMEDEMO_DURATIONS ];    // log of frame durations

}
