package org.q3df.test.demo;


import org.q3df.demo.BaseDemoMessageParser;
import org.q3df.demo.DemoParsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by Mike on 06.06.2017.
 */
public class MainScratch {


    public static String test_file = "lucy-vchrkn[df.vq3]00.44.048(MichaelRD.Russia).dm_68";

    private static Logger logger = LoggerFactory.getLogger(MainScratch.class);

    public static void main (String argv[]) {

        URL url = MainScratch.class.getResource("/demos/" + test_file);

        System.out.println(url);
        logger.debug("logger is working");

        try {
            DemoParsers.parse(url, new BaseDemoMessageParser());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
