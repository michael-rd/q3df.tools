package org.q3df.common.struct;

/**
 * Created by michael on 14.06.17.
 *
 * re-worked from q_shared.h, typedef struct entityState_s
 *
 */
public class EntityState {
    public int number;  // entity index
    public int eType;   // entityType_t
    public int eFlags;

    public Trajectory pos;  // for calculating position
    public Trajectory apos; // for calculating angles

    public int time;
    public int time2;

    public Vect3 origin;
    public Vect3 origin2;
    public Vect3 angles;
    public Vect3 angles2;

    public int             otherEntityNum; // shotgun sources, etc
    public int             otherEntityNum2;

    public int             groundEntityNum;        // -1 = in air

    public int             constantLight;  // r + (g<<8) + (b<<16) + (intensity<<24)
    public int             loopSound;              // constantly loop this sound

    public int             modelindex;
    public int             modelindex2;
    public int             clientNum;              // 0 to (MAX_CLIENTS - 1), for players and corpses
    public int             frame;

    public int             solid;                  // for client side prediction, trap_linkentity sets this properly

    public int             event;                  // impulse events -- muzzle flashes, footsteps, etc
    public int             eventParm;

    // for players
    public int             powerups;               // bit flags
    public int             weapon;                 // determines weapon and flash model, etc
    public int             legsAnim;               // mask off ANIM_TOGGLEBIT
    public int             torsoAnim;              // mask off ANIM_TOGGLEBIT

    public int             generic1;

    public EntityState() {
        pos = new Trajectory();
        apos = new Trajectory();
        origin = new Vect3();
        origin2 = new Vect3();
        angles = new Vect3();
        angles2 = new Vect3();
    }

    public void copy (EntityState x) {
        this.number = x.number;
        this.eType = x.eType;
        this.eFlags = x.eFlags;
        this.pos = new Trajectory(x.pos);
        this.apos = new Trajectory(x.apos);
        this.time = x.time;
        this.time2 = x.time2;
        this.origin = new Vect3(x.origin);
        this.origin2 = new Vect3(x.origin2);
        this.angles = new Vect3(x.angles);
        this.angles2 = new Vect3(x.angles2);
        this.otherEntityNum = x.otherEntityNum;
        this.otherEntityNum2 = x.otherEntityNum2;
        this.groundEntityNum = x.groundEntityNum;
        this.constantLight = x.constantLight;
        this.loopSound = x.loopSound;
        this.modelindex = x.modelindex;
        this.modelindex2 = x.modelindex2;
        this.clientNum = x.clientNum;
        this.frame = x.frame;
        this.solid = x.solid;
        this.event = x.event;
        this.eventParm = x.eventParm;
        this.powerups = x.powerups;
        this.weapon = x.weapon;
        this.legsAnim = x.legsAnim;
        this.torsoAnim = x.torsoAnim;
        this.generic1 = x.generic1;
    }
}
