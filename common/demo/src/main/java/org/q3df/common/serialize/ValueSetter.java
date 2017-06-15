package org.q3df.common.serialize;

import org.q3df.common.struct.EntityState;

public interface ValueSetter<T> {
    void setValue (EntityState state, T value);
}
