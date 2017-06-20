package org.q3df.demo;

import java.io.Closeable;

public interface DemoFileParser extends Closeable, AutoCloseable {

    boolean parseNextMessage (DemoMessageParser parser);

    void close ();
}
