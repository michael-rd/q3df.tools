package org.q3df.common.struct;

public enum TrType {
    TR_STATIONARY,
    TR_INTERPOLATE,                         // non-parametric, but interpolate between snapshots
    TR_LINEAR,
    TR_LINEAR_STOP,
    TR_SINE,                                        // value = base + sin( time / duration ) * delta
    TR_GRAVITY;
}
