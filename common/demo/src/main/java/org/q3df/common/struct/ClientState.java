package org.q3df.common.struct;

import org.q3df.common.Const;

/**
 * simplified version of clientActive_t struct
 */
public class ClientState {

    // it requres several frames in a timeout condition
    // to disconnect, preventing debugging breaks from
    // causing immediate disconnects on continue
    public int timeoutcount;

    // latest received from server
    public CLSnapshot snap;

    public int serverTime;                     // may be paused during play
    public int oldServerTime;          // to prevent time from flowing bakcwards
    public int oldFrameServerTime;     // to check tournament restarts

    // cl.serverTime = cls.realtime + cl.serverTimeDelta
    // this value changes as net lag varies
    public int serverTimeDelta;


    // set if any cgame frame has been forced to extrapolate
    // cleared when CL_AdjustTimeDelta looks at it
    public boolean extrapolatedSnapshot;
    public boolean newSnapshots;           // set on parse of any valid packet

    public GameState gameState = new GameState();                      // configstrings
    public String mapname;     // extracted from CS_SERVERINFO

    public int parseEntitiesNum;       // index (not anded off) into cl_parse_entities[]

//    int                     mouseDx[2], mouseDy[2]; // added to by mouse events
//    int                     mouseIndex;
//    int                     joystickAxis[MAX_JOYSTICK_AXIS];        // set by joystick events

    // cgame communicates a few values to the client system
    public int cgameUserCmdValue;      // current weapon to add to usercmd_t
    public float cgameSensitivity;

    // cmds[cmdNumber] is the predicted command, [cmdNumber-1] is the last
    // properly generated command
    // each mesage will send several old cmds
    public UserCmd cmds[] = new UserCmd[Const.CMD_BACKUP];
    // incremented each frame, because multiple
    // frames may need to be packed into a single packet
    public int cmdNumber;

//    outPacket_t     outPackets[PACKET_BACKUP];      // information about each packet we have sent out

    // the client maintains its own idea of view angles, which are
    // sent to the server each frame.  It is cleared to 0 upon entering each level.
    // the server sends a delta each frame which is added to the locally
    // tracked view angles to account for standing on rotating objects,
    // and teleport direction changes
//    vec3_t          viewangles;

//    snappingHud_t   snappinghud;

    public int serverId;                       // included in each client message so the server
    // can tell if it is for a prior map_restart
    // big stuff at end of structure so most offsets are 15 bits or less
    public CLSnapshot snapshots[] = new CLSnapshot[Const.PACKET_BACKUP];

    public EntityState entityBaselines[] = new EntityState[Const.MAX_GENTITIES]; // for delta compression when not in previous frame

    public EntityState parseEntities[] = new EntityState[Const.MAX_PARSE_ENTITIES];
}
