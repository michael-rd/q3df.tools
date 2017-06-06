package org.q3df.demo;

import java.io.InputStream;

/**
 * Created by michael on 06.06.17.
 */
public interface DemoParserFactory {

    DemoParser create (InputStream inputStream);

}
