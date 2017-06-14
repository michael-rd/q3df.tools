package org.q3df.demo;

import org.q3df.common.struct.EntityState;

/**
 * Created by michael on 14.06.17.
 */
public class EntityStateField {

    interface ValueReader<T> {
        T read (Q3HuffmanCoder.Decoder decoder);
    }

    interface ValueSetter<T> {
        void setValue (EntityState state, T value);
    }

    interface FieldMapper {
        int id ();
        String name ();
        void read (Q3HuffmanCoder.Decoder decoder, EntityState state);
    }

    class IntValueReader implements ValueReader<Integer> {

        private byte size;

        @Override
        public Integer read(Q3HuffmanCoder.Decoder decoder) {
            return decoder.readBits(size);
        }
    }


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

}
