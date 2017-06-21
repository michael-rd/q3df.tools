package org.q3df.common.struct;

import org.q3df.common.Const;

/**
 * simplified version of clientActive_t struct
 */
public class ClientState {

    // latest received from server
    public CLSnapshot snap;
    public boolean newSnapshots;           // set on parse of any valid packet

    public GameState gameState = new GameState();                      // configstrings

    public int parseEntitiesNum;       // index (not anded off) into cl_parse_entities[]

    // can tell if it is for a prior map_restart
    // big stuff at end of structure so most offsets are 15 bits or less
    public CLSnapshot snapshots[] = new CLSnapshot[Const.PACKET_BACKUP];

    public EntityState entityBaselines[] = new EntityState[Const.MAX_GENTITIES]; // for delta compression when not in previous frame

    public EntityState parseEntities[] = new EntityState[Const.MAX_PARSE_ENTITIES];


    public ClientState() {

        snap = new CLSnapshot();

        for (int i = 0; i < snapshots.length; i++) {
            snapshots[i] = new CLSnapshot();
        }

        for (int i = 0; i < entityBaselines.length; i++)
            entityBaselines[i] = new EntityState();

        for (int i = 0; i < parseEntities.length; i++)
            parseEntities[i] = new EntityState();
    }
}
