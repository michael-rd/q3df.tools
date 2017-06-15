package org.q3df.common.serialize;

import org.q3df.common.Const;
import org.q3df.common.struct.TrType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 14.06.17.
 */
public class FieldMapperFactory {


    static List<FieldMapper> entStateMappers;

    // re-worked from msg.c, netField_t      entityStateFields[]
    static {
        entStateMappers = new ArrayList<>(51);
        entStateMappers.add(int32("pos.trTime", (state, value) -> state.pos.trTime = value ));
        entStateMappers.add(vect("pos.trBase[0]", (state, value) -> state.pos.trBase.vect[0] = value ));
        entStateMappers.add(vect("pos.trBase[1]", (state, value) -> state.pos.trBase.vect[1] = value ));
        entStateMappers.add(vect("pos.trDelta[0]", (state, value) -> state.pos.trDelta.vect[0] = value ));
        entStateMappers.add(vect("pos.trDelta[1]", (state, value) -> state.pos.trDelta.vect[1] = value ));
        entStateMappers.add(vect("pos.trBase[2]", (state, value) -> state.pos.trBase.vect[2] = value ));
        entStateMappers.add(vect("apos.trBase[1]", (state, value) -> state.apos.trBase.vect[1] = value ));
        entStateMappers.add(vect("pos.trDelta[2]", (state, value) -> state.pos.trDelta.vect[2] = value ));
        entStateMappers.add(vect("apos.trBase[0]", (state, value) -> state.apos.trBase.vect[0] = value ));
        entStateMappers.add(bits("event", 10, (state, value) -> state.event = value ));
        entStateMappers.add(vect("angles2[1]", (state, value) -> state.angles2.vect[1] = value ));
        entStateMappers.add(byteInt("eType", (state, value) -> state.eType = value ));
        entStateMappers.add(byteInt("torsoAnim", (state, value) -> state.torsoAnim = value ));
        entStateMappers.add(byteInt("eventParm", (state, value) -> state.eventParm = value ));
        entStateMappers.add(byteInt("legsAnim", (state, value) -> state.legsAnim = value ));
        entStateMappers.add(bits("groundEntityNum", Const.GENTITYNUM_BITS, (state, value) -> state.groundEntityNum = value ));
        entStateMappers.add(trType("pos.trType", (state, value) -> state.pos.trType = value ));
        entStateMappers.add(bits("eFlags", 19, (state, value) -> state.eFlags = value ));
        entStateMappers.add(bits("otherEntityNum", Const.GENTITYNUM_BITS, (state, value) -> state.otherEntityNum = value ));
        entStateMappers.add(byteInt("weapon", (state, value) -> state.weapon = value ));
        entStateMappers.add(byteInt("clientNum", (state, value) -> state.clientNum = value ));
        entStateMappers.add(vect("angles[1]", (state, value) -> state.angles.vect[1] = value ));
        entStateMappers.add(int32("pos.trDuration", (state, value) -> state.pos.trDuration = value ));
        entStateMappers.add(trType("apos.trType", (state, value) -> state.apos.trType = value ));
        entStateMappers.add(vect("origin[0]", (state, value) -> state.origin.vect[0] = value ));
        entStateMappers.add(vect("origin[1]", (state, value) -> state.origin.vect[1] = value ));
        entStateMappers.add(vect("origin[2]", (state, value) -> state.origin.vect[2] = value ));
        entStateMappers.add(bits("solid", 24, (state, value) -> state.solid = value ));
        entStateMappers.add(bits("powerups", Const.MAX_POWERUPS, (state, value) -> state.powerups = value ));
        entStateMappers.add(byteInt("modelindex", (state, value) -> state.modelindex = value ));
        entStateMappers.add(bits("otherEntityNum2", Const.GENTITYNUM_BITS, (state, value) -> state.otherEntityNum2 = value ));
        entStateMappers.add(byteInt("loopSound", (state, value) -> state.loopSound = value ));
        entStateMappers.add(byteInt("generic1", (state, value) -> state.generic1 = value ));
        entStateMappers.add(vect("origin2[2]", (state, value) -> state.origin2.vect[2] = value ));
        entStateMappers.add(vect("origin2[0]", (state, value) -> state.origin2.vect[0] = value ));
        entStateMappers.add(vect("origin2[1]", (state, value) -> state.origin2.vect[1] = value ));
        entStateMappers.add(byteInt("modelindex2", (state, value) -> state.modelindex2 = value ));
        entStateMappers.add(vect("angles[0]", (state, value) -> state.angles.vect[0] = value ));
        entStateMappers.add(int32("time", (state, value) -> state.time = value ));
        entStateMappers.add(int32("apos.trTime", (state, value) -> state.apos.trTime = value ));
        entStateMappers.add(int32("apos.trDuration", (state, value) -> state.apos.trDuration = value ));
        entStateMappers.add(vect("apos.trBase[2]", (state, value) -> state.apos.trBase.vect[2] = value ));
        entStateMappers.add(vect("apos.trDelta[0]", (state, value) -> state.apos.trDelta.vect[0] = value ));
        entStateMappers.add(vect("apos.trDelta[1]", (state, value) -> state.apos.trDelta.vect[1] = value ));
        entStateMappers.add(vect("apos.trDelta[2]", (state, value) -> state.apos.trDelta.vect[2] = value ));
        entStateMappers.add(int32("time2", (state, value) -> state.time2 = value ));
        entStateMappers.add(vect("angles[2]", (state, value) -> state.angles.vect[2] = value ));
        entStateMappers.add(vect("angles2[0]", (state, value) -> state.angles2.vect[0] = value ));
        entStateMappers.add(vect("angles2[2]", (state, value) -> state.angles2.vect[2] = value ));
        entStateMappers.add(int32("constantLight", (state, value) -> state.constantLight = value ));
        entStateMappers.add(bits("frame", 16, (state, value) -> state.frame = value ));
    }

    private static FieldMapper vect (String name, ValueSetter<Float> setter) {
        return new FieldMapperImpl<>(entStateMappers.size(), name, ReaderFactory.vectValueReader(), setter);
    }

    private static FieldMapper int32 (String name, ValueSetter<Integer> setter) {
        return new FieldMapperImpl<>(entStateMappers.size(), name, ReaderFactory.longReader(), setter);
    }

    private static FieldMapper trType (String name, ValueSetter<TrType> setter) {
        return new FieldMapperImpl<>(entStateMappers.size(), name, ReaderFactory.trTypeValueReader(), setter);
    }

    private static FieldMapper bits (String name, int bits, ValueSetter<Integer> setter) {
        return new FieldMapperImpl<>(entStateMappers.size(), name, ReaderFactory.intBitsReader(bits), setter);
    }

    private static FieldMapper byteInt (String name, ValueSetter<Integer> setter) {
        return new FieldMapperImpl<>(entStateMappers.size(), name, ReaderFactory.byteReader(), setter);
    }

    public static void main(String[] args) {
        System.out.println(entStateMappers.size());
    }
}
