package org.q3df.common.serialize;

import org.q3df.common.Const;
import org.q3df.common.struct.EntityState;
import org.q3df.common.struct.PlayerState;
import org.q3df.common.struct.TrType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 14.06.17.
 */
public class FieldMapperFactory {


    static List<FieldMapper<EntityState>> estmap;

    static List<FieldMapper<PlayerState>> plstmap;

    static {
        // re-worked from msg.c, netField_t      entityStateFields[]
        estmap = new ArrayList<>(51);
        estmap.add(int32("pos.trTime", (state, value) -> state.pos.trTime = value ));
        estmap.add(vect("pos.trBase[0]", (state, value) -> state.pos.trBase.vect[0] = value ));
        estmap.add(vect("pos.trBase[1]", (state, value) -> state.pos.trBase.vect[1] = value ));
        estmap.add(vect("pos.trDelta[0]", (state, value) -> state.pos.trDelta.vect[0] = value ));
        estmap.add(vect("pos.trDelta[1]", (state, value) -> state.pos.trDelta.vect[1] = value ));
        estmap.add(vect("pos.trBase[2]", (state, value) -> state.pos.trBase.vect[2] = value ));
        estmap.add(vect("apos.trBase[1]", (state, value) -> state.apos.trBase.vect[1] = value ));
        estmap.add(vect("pos.trDelta[2]", (state, value) -> state.pos.trDelta.vect[2] = value ));
        estmap.add(vect("apos.trBase[0]", (state, value) -> state.apos.trBase.vect[0] = value ));
        estmap.add(bits("event", 10, (state, value) -> state.event = value ));
        estmap.add(vect("angles2[1]", (state, value) -> state.angles2.vect[1] = value ));
        estmap.add(byteInt("eType", (state, value) -> state.eType = value ));
        estmap.add(byteInt("torsoAnim", (state, value) -> state.torsoAnim = value ));
        estmap.add(byteInt("eventParm", (state, value) -> state.eventParm = value ));
        estmap.add(byteInt("legsAnim", (state, value) -> state.legsAnim = value ));
        estmap.add(bits("groundEntityNum", Const.GENTITYNUM_BITS, (state, value) -> state.groundEntityNum = value ));
        estmap.add(trType("pos.trType", (state, value) -> state.pos.trType = value ));
        estmap.add(bits("eFlags", 19, (state, value) -> state.eFlags = value ));
        estmap.add(bits("otherEntityNum", Const.GENTITYNUM_BITS, (state, value) -> state.otherEntityNum = value ));
        estmap.add(byteInt("weapon", (state, value) -> state.weapon = value ));
        estmap.add(byteInt("clientNum", (state, value) -> state.clientNum = value ));
        estmap.add(vect("angles[1]", (state, value) -> state.angles.vect[1] = value ));
        estmap.add(int32("pos.trDuration", (state, value) -> state.pos.trDuration = value ));
        estmap.add(trType("apos.trType", (state, value) -> state.apos.trType = value ));
        estmap.add(vect("origin[0]", (state, value) -> state.origin.vect[0] = value ));
        estmap.add(vect("origin[1]", (state, value) -> state.origin.vect[1] = value ));
        estmap.add(vect("origin[2]", (state, value) -> state.origin.vect[2] = value ));
        estmap.add(bits("solid", 24, (state, value) -> state.solid = value ));
        estmap.add(bits("powerups", Const.MAX_POWERUPS, (state, value) -> state.powerups = value ));
        estmap.add(byteInt("modelindex", (state, value) -> state.modelindex = value ));
        estmap.add(bits("otherEntityNum2", Const.GENTITYNUM_BITS, (state, value) -> state.otherEntityNum2 = value ));
        estmap.add(byteInt("loopSound", (state, value) -> state.loopSound = value ));
        estmap.add(byteInt("generic1", (state, value) -> state.generic1 = value ));
        estmap.add(vect("origin2[2]", (state, value) -> state.origin2.vect[2] = value ));
        estmap.add(vect("origin2[0]", (state, value) -> state.origin2.vect[0] = value ));
        estmap.add(vect("origin2[1]", (state, value) -> state.origin2.vect[1] = value ));
        estmap.add(byteInt("modelindex2", (state, value) -> state.modelindex2 = value ));
        estmap.add(vect("angles[0]", (state, value) -> state.angles.vect[0] = value ));
        estmap.add(int32("time", (state, value) -> state.time = value ));
        estmap.add(int32("apos.trTime", (state, value) -> state.apos.trTime = value ));
        estmap.add(int32("apos.trDuration", (state, value) -> state.apos.trDuration = value ));
        estmap.add(vect("apos.trBase[2]", (state, value) -> state.apos.trBase.vect[2] = value ));
        estmap.add(vect("apos.trDelta[0]", (state, value) -> state.apos.trDelta.vect[0] = value ));
        estmap.add(vect("apos.trDelta[1]", (state, value) -> state.apos.trDelta.vect[1] = value ));
        estmap.add(vect("apos.trDelta[2]", (state, value) -> state.apos.trDelta.vect[2] = value ));
        estmap.add(int32("time2", (state, value) -> state.time2 = value ));
        estmap.add(vect("angles[2]", (state, value) -> state.angles.vect[2] = value ));
        estmap.add(vect("angles2[0]", (state, value) -> state.angles2.vect[0] = value ));
        estmap.add(vect("angles2[2]", (state, value) -> state.angles2.vect[2] = value ));
        estmap.add(int32("constantLight", (state, value) -> state.constantLight = value ));
        estmap.add(bits("frame", 16, (state, value) -> state.frame = value ));


        // re-worked from msg.c, netField_t      playerStateFields[]
        plstmap = new ArrayList<>(48);
        plstmap.add(int32("commandTime", (state, value) -> state.commandTime = value));
        plstmap.add(vect("origin[0]", (state, value) -> state.origin.vect[0] = value));
        plstmap.add(vect("origin[1]", (state, value) -> state.origin.vect[1] = value));
        plstmap.add(byteInt("bobCycle", (state, value) -> state.bobCycle = value));
        plstmap.add(vect("velocity[0]", (state, value) -> state.velocity.vect[0] = value));
        plstmap.add(vect("velocity[1]", (state, value) -> state.velocity.vect[1] = value));
        plstmap.add(vect("viewangles[1]", (state, value) -> state.viewangles.vect[1] = value));
        plstmap.add(vect("viewangles[0]", (state, value) -> state.viewangles.vect[0] = value));
        plstmap.add(bits("weaponTime", -16, (state, value) -> state.weaponTime = value));
        plstmap.add(vect("origin[2]", (state, value) -> state.origin.vect[2] = value));
        plstmap.add(vect("velocity[2]", (state, value) -> state.velocity.vect[2] = value));
        plstmap.add(byteInt("legsTimer", (state, value) -> state.legsTimer = value));
        plstmap.add(bits("pm_time", -16, (state, value) -> state.pm_time = value));
        plstmap.add(bits("eventSequence", 16, (state, value) -> state.eventSequence = value));
        plstmap.add(byteInt("torsoAnim", (state, value) -> state.torsoAnim = value));
        plstmap.add(bits("movementDir", 4, (state, value) -> state.movementDir = value));
        plstmap.add(byteInt("events[0]", (state, value) -> state.events[0] = value));
        plstmap.add(byteInt("legsAnim", (state, value) -> state.legsAnim = value));
        plstmap.add(byteInt("events[1]", (state, value) -> state.events[1] = value));
        plstmap.add(bits("pm_flags", 16, (state, value) -> state.pm_flags = value));
        plstmap.add(bits("groundEntityNum", Const.GENTITYNUM_BITS, (state, value) -> state.groundEntityNum = value));
        plstmap.add(bits("weaponstate", 4, (state, value) -> state.weaponstate = value));
        plstmap.add(bits("eFlags", 16, (state, value) -> state.eFlags = value));
        plstmap.add(bits("externalEvent", 10, (state, value) -> state.externalEvent = value));
        plstmap.add(bits("gravity", 16, (state, value) -> state.gravity = value));
        plstmap.add(bits("speed", 16, (state, value) -> state.speed = value));
        plstmap.add(bits("delta_angles[1]", 16, (state, value) -> state.delta_angles[1] = value));
        plstmap.add(byteInt("externalEventParm", (state, value) -> state.externalEventParm = value));
        plstmap.add(bits("viewheight", -8, (state, value) -> state.viewheight = value));
        plstmap.add(byteInt("damageEvent", (state, value) -> state.damageEvent = value));
        plstmap.add(byteInt("damageYaw", (state, value) -> state.damageYaw = value));
        plstmap.add(byteInt("damagePitch", (state, value) -> state.damagePitch = value));
        plstmap.add(byteInt("damageCount", (state, value) -> state.damageCount = value));
        plstmap.add(byteInt("generic1", (state, value) -> state.generic1 = value));
        plstmap.add(byteInt("pm_type", (state, value) -> state.pm_type = value));
        plstmap.add(bits("delta_angles[0]", 16, (state, value) -> state.delta_angles[0] = value));
        plstmap.add(bits("delta_angles[2]", 16, (state, value) -> state.delta_angles[2] = value));
        plstmap.add(bits("torsoTimer", 12, (state, value) -> state.torsoTimer = value));
        plstmap.add(byteInt("eventParms[0]", (state, value) -> state.eventParms[0] = value));
        plstmap.add(byteInt("eventParms[1]", (state, value) -> state.eventParms[1] = value));
        plstmap.add(byteInt("clientNum", (state, value) -> state.clientNum = value));
        plstmap.add(bits("weapon", 5, (state, value) -> state.weapon = value));
        plstmap.add(vect("viewangles[2]", (state, value) -> state.viewangles.vect[2] = value));
        plstmap.add(vect("grapplePoint[0]", (state, value) -> state.grapplePoint.vect[0] = value));
        plstmap.add(vect("grapplePoint[1]", (state, value) -> state.grapplePoint.vect[1] = value));
        plstmap.add(vect("grapplePoint[2]", (state, value) -> state.grapplePoint.vect[2] = value));
        plstmap.add(bits("jumppad_ent", Const.GENTITYNUM_BITS, (state, value) -> state.jumppad_ent = value));
        plstmap.add(bits("loopSound", 16, (state, value) -> state.loopSound = value));
    }

    public static int getEntityStateFieldNum() {
        return estmap.size();
    }

    public static int getPlayerStateFieldNum() {
        return plstmap.size();
    }


    public static FieldMapper<EntityState> entStateFieldMapper(int id) {
        return id >= 0 && id < estmap.size() ? estmap.get(id) : null;
    }

    public static FieldMapper<PlayerState> playerStateFieldMapper(int id) {
        return id >= 0 && id < plstmap.size() ? plstmap.get(id) : null;
    }


    private static <E> FieldMapper<E> vect (String name, ValueSetter<E, Float> setter) {
        return new FieldMapperImpl<>(name, ReaderFactory.vectValueReader(), setter, 0.0f);
    }

    private static <E> FieldMapper<E> int32 (String name, ValueSetter<E, Integer> setter) {
        return new FieldMapperImpl<>(name, ReaderFactory.longReader(), setter, 0);
    }

    private static <E> FieldMapper<E> trType (String name, ValueSetter<E, TrType> setter) {
        return new FieldMapperImpl<>(name, ReaderFactory.trTypeValueReader(), setter, TrType.TR_STATIONARY);
    }

    private static <E> FieldMapper<E> bits (String name, int bits, ValueSetter<E, Integer> setter) {
        return new FieldMapperImpl<>(name, ReaderFactory.intBitsReader(bits), setter, 0);
    }

    private static <E> FieldMapper<E> byteInt (String name, ValueSetter<E, Integer> setter) {
        return new FieldMapperImpl<>(name, ReaderFactory.byteReader(), setter, 0);
    }



    public static void main(String[] args) {
        System.out.println(estmap.size());
        System.out.println(plstmap.size());
    }
}
