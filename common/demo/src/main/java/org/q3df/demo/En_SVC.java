package org.q3df.demo;

public enum En_SVC {

    BAD(0),  // not used in demos
    NOP(1),  // not used in demos
    GAMESTATE(2),
    CONFIGSTRING(3), // only inside gamestate
    BASELINE(4),     // only insede gamestate
    SERVERCOMMAND(5),
    DOWNLOAD(6),    // not used in demos
    SNAPSHOT(7),
    EOF(8)
    ;

    En_SVC (int id) {
      this.id = id;
    }

    private final int id;

    public int getId() {
        return id;
    }

    public static En_SVC find (int id) {
        for (En_SVC it : En_SVC.values()) {
            if (it.id == id)
                return it;
        }

        return null;
    }
}
