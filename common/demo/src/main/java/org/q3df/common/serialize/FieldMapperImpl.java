package org.q3df.common.serialize;

import org.q3df.common.struct.EntityState;
import org.q3df.demo.Q3HuffmanCoder;

class FieldMapperImpl<T> implements FieldMapper {
    ValueReader<T> valueReader;
    ValueSetter<T> setter;
    int myId;
    String name;

    public FieldMapperImpl(int myId, String name, ValueReader<T> reader, ValueSetter<T> setter) {
        this.myId = myId;
        this.valueReader = reader;
        this.setter = setter;
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public int id() {
        return this.myId;
    }

    @Override
    public void read(Q3HuffmanCoder.Decoder decoder, EntityState state) {
        setter.setValue(state, valueReader.read(decoder));
    }
}
