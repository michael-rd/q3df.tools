package org.q3df.test.demo;

import org.junit.Assert;
import org.junit.Test;
import org.q3df.common.BitStream;

public class BitStreamTest {

    @Test
    public void testBitOrder () {
        BitStream stream = new BitStream(new byte[]{0}, 8);

        stream.write(1);
        stream.write(0);
        stream.write(1);
        stream.write(0);
        stream.write(1);
        stream.write(1);
        stream.write(1);
        Assert.assertTrue(stream.write(1));

        System.out.println(stream.asInt(0));

        Assert.assertEquals(0b11110101, stream.asInt(0));

        Assert.assertTrue(stream.isEOWR());
    }

    @Test
    public void testValues () {
        BitStream stream = new BitStream(new byte[8]);
        stream.write(0, 4);    // 0000
        stream.write(0b1100, 4);  // 1100
        stream.write(0xAB, 8);

        Assert.assertEquals(0b11000000, stream.asInt(0));
        Assert.assertEquals(0xAB, stream.asInt(1));

        Assert.assertTrue(!stream.isEOWR());
    }
}
