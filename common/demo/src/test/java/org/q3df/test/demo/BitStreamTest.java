package org.q3df.test.demo;

import org.junit.Assert;
import org.junit.Test;
import org.q3df.common.msg.BitStream;
import org.q3df.common.msg.BitStreamWriter;

public class BitStreamTest {

    @Test
    public void testBitOrder () {
        BitStream stream = new BitStream(new byte[]{0}, 8);

        int testBits[] = {1,0,1,0,1,1,1,1};

        for (int b : testBits) {
            Assert.assertTrue(stream.writeBit(b));
        }
        System.out.println(stream.getByte(0));

        Assert.assertEquals(0b11110101, stream.getByte(0));
        Assert.assertTrue(stream.isEOWR());

        for (int b : testBits)
            Assert.assertEquals(b, stream.readBit());

        Assert.assertTrue(stream.isEORD());
    }

    @Test
    public void testValues () {
        BitStreamWriter stream = new BitStream(new byte[8]);
        stream.writeBits(0, 4);    // 0000
        stream.writeBits(0b1100, 4);  // 1100
        stream.writeBits(0xAB, 8);

        Assert.assertEquals(0b11000000, stream.getByte(0));
        Assert.assertEquals(0xAB, stream.getByte(1));

        Assert.assertTrue(!stream.isEOWR());
    }
}
