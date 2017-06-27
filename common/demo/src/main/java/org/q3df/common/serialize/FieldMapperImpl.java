package org.q3df.common.serialize;

import org.q3df.common.msg.MessageDataReader;
import org.q3df.common.msg.Q3HuffmanCoder;

class FieldMapperImpl<E,T> implements FieldMapper<E> {
    ValueReader<T> valueReader;
    ValueSetter<E, T> setter;
    T defaultValue;
    String name;

    public FieldMapperImpl(String name, ValueReader<T> reader, ValueSetter<E, T> setter, T defaultValue) {
        this.valueReader = reader;
        this.setter = setter;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void reset(E state) {
        setter.setValue(state, defaultValue);
    }

    @Override
    public void read(Q3HuffmanCoder.Decoder decoder, E state) {
        setter.setValue(state, valueReader.read(decoder));
    }

    @Override
    public void read(MessageDataReader reader, E state) {
        setter.setValue(state, valueReader.read(reader));
    }
}
