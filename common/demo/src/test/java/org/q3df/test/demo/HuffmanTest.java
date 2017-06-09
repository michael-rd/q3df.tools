package org.q3df.test.demo;

import org.junit.Assert;
import org.junit.Test;
import org.q3df.demo.Q3HuffmanCoder;

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
    }

    @Test
    public void test002 () {
        Q3HuffmanCoder.Encoder encoder = Q3HuffmanCoder.encoder(16);

        // data same as in test001, but write with other methods
        encoder.writeInt(0x03020100); // 0,1,2,3
        encoder.writeInt(0x50FF1404); // 4,20,255,80
        encoder.writeShort(0x0500); // 0, 5

        Assert.assertEquals("6E243BB43B92A5110000000000000000", encoder.dumpBuffer());
    }


    @Test
    public void test003 () {
        Q3HuffmanCoder.Encoder encoder = Q3HuffmanCoder.encoder(16);

        encoder.writeString("Hello World!");

        Assert.assertEquals("3D619898B3F78CB3479897A611000000", encoder.dumpBuffer());
    }
}
