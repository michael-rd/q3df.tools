package org.q3df.test.demo;


import org.q3df.demo.DemoDataFacade;
import org.q3df.demo.DemoParsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by Mike on 06.06.2017.
 */
public class MainScratch {


    public static String test_file = "dfwc2019-1[df.vq3]00.25.488(lokki.Russia)_8883.dm_68";

    private static Logger logger = LoggerFactory.getLogger(MainScratch.class);

    public static void main (String argv[]) {

        URL url = MainScratch.class.getResource("/demos/" + test_file);

        System.out.println(url);
        logger.debug("logger is working");

        try {
            //DemoParsers.parse(url, new BaseDemoMessageParser());
            DemoDataFacade dataFacade = DemoParsers.getDemoData(url.openStream());
            System.out.println("Client config:" + dataFacade.getDemoClientConfig().toString());

            System.out.println("Player config:" + dataFacade.getDemoPlayerConfig().toString());

            System.out.println("Game config:" + dataFacade.getDemoGameConfig().toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
