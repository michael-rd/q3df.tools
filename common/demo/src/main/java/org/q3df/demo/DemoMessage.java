package org.q3df.demo;

import org.q3df.common.Const;

import java.util.Arrays;

/**
 * Created by michael on 06.06.17.
 */
public class DemoMessage {
    private byte[] data;
    private int len;

    public DemoMessage() {
        this (new byte[Const.MESSAGE_MAX_SIZE], Const.MESSAGE_MAX_SIZE);
    }

    public DemoMessage(byte[] data, int len) {
        this.data = data;
        this.len = Math.max(data.length,len);
    }

    public byte[] data () {
        return this.data;
    }

    public int length () {
        return this.len;
    }

    public DemoMessage resize (int len) {
        if (this.len > data.length)
            throw new RuntimeException("length param <" + len + "> exceeds limit");
        this.len = len;
        return this;
    }

    public DemoMessage clear () {
        Arrays.fill(this.data, 0, len, (byte)0);
        return this;
    }
}
