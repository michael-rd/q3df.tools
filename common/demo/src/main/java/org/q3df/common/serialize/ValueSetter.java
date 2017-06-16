package org.q3df.common.serialize;

public interface ValueSetter<E,T> {
    void setValue (E state, T value);
}
