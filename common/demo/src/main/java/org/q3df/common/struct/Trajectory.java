package org.q3df.common.struct;

public class Trajectory {
    public TrType trType;
    public int trTime;
    public int trDuration;
    public Vect3 trBase;
    public Vect3 trDelta;

    public Trajectory() {
        this.trBase = new Vect3();
        this.trDelta = new Vect3();
        trType = TrType.TR_STATIONARY;
    }

    public Trajectory (Trajectory x) {
        this.trType = x.trType;
        this.trTime = x.trTime;
        this.trDuration = x.trDuration;
        this.trBase = new Vect3(x.trBase);
        this.trDelta = new Vect3(x.trDelta);
    }


}
