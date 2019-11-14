package org.q3df.demo;

import org.q3df.common.Const;
import org.q3df.common.struct.GameState;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DemoDataFacade {

//    public static Pattern DEMO_TIME_PATTERN = Pattern.compile("^Time performed by [^:]+ : ([0-9:]+) .*$");

    Map<String, String> demoClientConfig;
    Map<String, String> demoPlayerConfig;
    Map<String, String> demoGameConfig;

    int snapshots;
    String demoTime;

    public DemoDataFacade (GameState state, int snapshots, String demoTimeCmd) {

        unpackCfgString(state.get(Const.DEMO_CFG_FIELD_CLIENT), demoClientConfig = new HashMap<>());
        unpackCfgString(state.get(Const.DEMO_CFG_FIELD_GAME), demoGameConfig = new HashMap<>());
        unpackCfgString(state.get(Const.DEMO_CFG_FIELD_PLAYER), demoPlayerConfig = new HashMap<>());

        this.snapshots = snapshots;

        demoTimeCmd = demoTimeCmd.replaceAll("\\^[0-9]","");


        demoTimeCmd = demoTimeCmd.substring(demoTimeCmd.indexOf(':')+2);
        demoTime = demoTimeCmd.substring(0, demoTimeCmd.indexOf(' ')).trim();

//        System.out.println(demoTimeCmd);

//        Matcher m = DEMO_TIME_PATTERN.matcher(demoTimeCmd);
//        if (m.matches())
//            demoTime = m.group(1);
    }

    private static void unpackCfgString (String str, Map<String,String> map) {
        if (str == null || str.isEmpty())
            return;

        int begin = str.charAt(0) == '\\' ? 1 : 0;

        String[] cfg = str.split("\\\\");

        for (int i = begin; i < cfg.length; i+=2)
            map.put(cfg[i].toLowerCase(), cfg[i+1]);
    }

    public Map<String, String> getDemoPlayerConfig() {
        return demoPlayerConfig;
    }

    public Map<String, String> getDemoGameConfig() {
        return demoGameConfig;
    }

    public Map<String, String> getDemoClientConfig() {
        return demoClientConfig;
    }

    public String getDemoTime() {
        return demoTime;
    }

    public int getSnapshots () {
        return snapshots;
    }

    public String getEngineString () {
        return demoClientConfig.get("version");
    }


    private static Pattern _pat = Pattern.compile("^.*?([0-9]{2})\\.([0-9]{2})\\.([0-9]{3}).*$");

    private static long _time (String min, String sec, String msec) {
        return Long.parseLong(min,10)*60*1000 + Long.parseLong(sec,10)*1000 + Long.parseLong(msec,10);
    }

//    public static long getTimeFromFileName (String fname) {
//
//    }

    public boolean matchesTimeInFileName (String filename) {
        Matcher m = _pat.matcher(filename);
        if (m.matches()) {
            long ftime = _time (m.group(1), m.group(2), m.group(3));

            String[] dtime_t = demoTime.split(":");
            long dtime = dtime_t.length == 2 ? _time("0", dtime_t[0], dtime_t[1]) : _time(dtime_t[0], dtime_t[1], dtime_t[2]);

            return ftime == dtime;
        }
        else {
            System.out.println("unable to parse time from file name");
            return false;
        }
    }
}
