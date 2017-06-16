package org.q3df.common.struct;

import org.q3df.common.Const;

public class CLSnapshot {

    public boolean valid;                  // cleared if delta parsing was invalid
    public int snapFlags;              // rate delayed and dropped commands

    public int serverTime;             // server time the message is valid for (in msec)

    public int messageNum;             // copied from netchan->incoming_sequence
    public int deltaNum;               // messageNum the delta is from
    public int ping;                   // time from when cmdNum-1 was sent to time packet was reeceived
    public byte areamask[] = new byte[Const.MAX_MAP_AREA_BYTES];           // portalarea visibility bits

    public int cmdNum;                 // the next cmdNum the server is expecting
    public PlayerState ps = new PlayerState();  // complete information about the current player at this time

    public int numEntities;                    // all of the entities that need to be presented
    public int parseEntitiesNum;               // at the time of this snapshot

    // execute all commands up to this before
    // making the snapshot current
    public int serverCommandNum;

}
