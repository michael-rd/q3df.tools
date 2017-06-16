package org.q3df.test.demo;

import org.junit.Assert;
import org.junit.Test;
import org.q3df.common.msg.Q3HuffmanCoder;

/**
 * Created by michael on 08.06.17.
 */
public class HuffmanTest {

    @Test
    public void test001 () {
        Q3HuffmanCoder.Encoder encoder = Q3HuffmanCoder.encoder(16);

        int bytesToWrite[] = {0, 1, 2, 3, 4, 20, 255, 80, 0, 5};

        for (int b : bytesToWrite) {
            Assert.assertTrue(encoder.writeByte(b));
        }

        // compare with bytes taken within q3-source original implementation
        Assert.assertEquals("6E243BB43B92A5110000000000000000", encoder.dumpBuffer());

        System.out.println(encoder.dumpBuffer());

        Q3HuffmanCoder.Decoder decoder = Q3HuffmanCoder.decoder(encoder.buffer());

        for (int b : bytesToWrite) {
            Assert.assertEquals(b, decoder.readByte());
        }
    }

    @Test
    public void test002 () {
        Q3HuffmanCoder.Encoder encoder = Q3HuffmanCoder.encoder(16);

        // data same as in test001, but write with other methods
        encoder.writeInt(0x03020100); // 0,1,2,3
        encoder.writeInt(0x50FF1404); // 4,20,255,80
        encoder.writeShort(0x0500); // 0, 5

        Assert.assertEquals("6E243BB43B92A5110000000000000000", encoder.dumpBuffer());

        Q3HuffmanCoder.Decoder decoder = Q3HuffmanCoder.decoder(encoder.buffer());

        //System.out.println (Integer.toHexString(decoder.readInt()));
        Assert.assertEquals(0x03020100, decoder.readInt());
        Assert.assertEquals(0x50FF1404, decoder.readInt());
        Assert.assertEquals(0x0500, decoder.readShort());
    }


    @Test
    public void test003 () {
        Q3HuffmanCoder.Encoder encoder = Q3HuffmanCoder.encoder(16);

        encoder.writeString("Hello World!");

        Assert.assertEquals("3D619898B3F78CB3479897A611000000", encoder.dumpBuffer());

        Q3HuffmanCoder.Decoder decoder = Q3HuffmanCoder.decoder(encoder.buffer());

        Assert.assertEquals("Hello World!", decoder.readString());
    }

    @Test
    public void test004() {
        Q3HuffmanCoder.Encoder encoder = Q3HuffmanCoder.encoder(16);

        encoder.writeFload(3.14f);
        encoder.writeFload(89.20170609f);
        encoder.writeAngle16(49.2f);

        Assert.assertEquals("20EA3D9F013DED919F2B110000000000", encoder.dumpBuffer());

        Q3HuffmanCoder.Decoder decoder = Q3HuffmanCoder.decoder(encoder.buffer());

       // System.out.println (decoder.readFloat());

        Assert.assertEquals(3.14f, decoder.readFloat(), 0f);
        Assert.assertEquals(89.20170609f, decoder.readFloat(), 0f);
    }

    @Test
    public void test005() {
        Q3HuffmanCoder.Encoder encoder = Q3HuffmanCoder.encoder(16);

        encoder.writeBits(3251, 12);
        encoder.writeBits(823, 10);
        encoder.writeBits(21972, 15);

        Assert.assertEquals("33C0C5518B0600000000000000000000", encoder.dumpBuffer());

        Q3HuffmanCoder.Decoder decoder = Q3HuffmanCoder.decoder(encoder.buffer());

        Assert.assertEquals(3251, decoder.readBits(12));
        Assert.assertEquals(823, decoder.readBits(10));
        Assert.assertEquals(21972, decoder.readBits(15));
    }
}
