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
}
