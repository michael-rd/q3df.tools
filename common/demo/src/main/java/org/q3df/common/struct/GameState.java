package org.q3df.common.struct;

import org.q3df.common.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GameState {

    private static Logger logger = LoggerFactory.getLogger(GameState.class);

    private Map<Integer, String> configStrings;

    public GameState () {
        configStrings = new HashMap<>(Const.MAX_CONFIGSTRINGS);
    }

    public String put (int id, String data) {
        if (id < 0 || id >= Const.MAX_CONFIGSTRINGS) {
            logger.error ("config string key {} is out of range", id);
            throw new RuntimeException("config string key <" + id +"> is out of range");
        }
        return configStrings.put(id, data);
    }

    public String get (int id) {
        return configStrings.get(id);
    }
}
