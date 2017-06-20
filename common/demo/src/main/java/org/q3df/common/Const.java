package org.q3df.common;

/**
 * Created by michael on 06.06.17.
 */
public class Const {
    public static final int MESSAGE_MAX_SIZE = 0x4000;
    public static final int MAX_STRING_CHARS = 1024;
    public static final int BIG_INFO_STRING = 8192;

    public static final int MAX_CONFIGSTRINGS = 1024;

    public static final int GENTITYNUM_BITS = 10;
    public static final int MAX_GENTITIES = 1<<GENTITYNUM_BITS;

    public static final int FLOAT_INT_BITS = 13;
    public static final int FLOAT_INT_BIAS = (1<<(FLOAT_INT_BITS-1));

    public static final int PACKET_BACKUP = 32;
    public static final int PACKET_MASK = PACKET_BACKUP-1;

    public static final int MAX_RELIABLE_COMMANDS = 64;

    // q_shared.h
    public static final int MAX_POWERUPS = 16;
    public static final int MAX_WEAPONS = 16;
    public static final int MAX_STATS = 16;
    public static final int MAX_PERSISTANT = 16;
    public static final int PS_PMOVEFRAMECOUNTBITS = 6;
    public static final int MAX_PS_EVENTS = 2;
    public static final int MAX_MAP_AREA_BYTES = 16;

    // cg_public.h
    public static final int CMD_BACKUP = 64;
    public static final int CMD_MASK = CMD_BACKUP-1;


    // client.h
    public static final int MAX_PARSE_ENTITIES = 2048;
}

