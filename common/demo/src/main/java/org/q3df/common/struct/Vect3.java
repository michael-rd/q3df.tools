package org.q3df.common.struct;

public class Vect3 {
    public float vect[] = new float[3];

    public Vect3() {
    }

    public Vect3 (Vect3 x) {
        this.vect[0] = x.vect[0];
        this.vect[1] = x.vect[1];
        this.vect[2] = x.vect[2];
    }
}
