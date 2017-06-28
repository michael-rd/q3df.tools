package org.q3df.test.demo;

import org.junit.Assert;
import org.junit.Test;
import org.q3df.common.msg.BitStream;
import org.q3df.common.msg.BitStreamWriter;

import java.util.BitSet;

public class BitStreamTest {


    @Test
    public void perf002 () {
        long sum = 0;
        for (int i = 0; i < 10; i++)
            sum += doStreamWritePerfTest();

        System.out.println("avg write = " + (sum/10));

        sum = 0;
        for (int i = 0; i < 10; i++)
            sum += doStreamReadPerfTest();

        System.out.println("avg read = " + (sum/10));
    }

    private long doStreamReadPerfTest() {
        BitStream bitStream = new BitStream(4096*8);

        long st = System.currentTimeMillis();
        int summ = 0;
        for (int i = 0; i < 15*1024*1024*8; i++) {
            summ += bitStream.readBit();

            if (bitStream.isEORD())
                bitStream.rewindRead();
        }

        long r = System.currentTimeMillis() - st;

        System.out.println("run, " + summ);

        return r;
    }


    private long doStreamWritePerfTest() {
        BitStream bitStream = new BitStream(4096*8);

        long st = System.currentTimeMillis();
        for (int i = 0; i < 15*1024*1024*8; i++) {
            bitStream.writeBit(true);

            if (bitStream.isEOWR())
                bitStream.rewindWrite();
        }

        long r = System.currentTimeMillis() - st;

        System.out.println("run, " + bitStream.getByte(0));

        return r;
    }

    @Test
    public void perf001 () {

        long sum = 0;
        for (int i = 0; i < 10; i++)
            sum += doPerfTestBitSet();

        System.out.println("avg = " + (sum/10));
    }

    private long doPerfTestBitSet() {
        BitSet bitSet = new BitSet(4096*8);

        long st = System.currentTimeMillis();
        for (int i = 0; i < 15*1024*1024*8; i++) {
            bitSet.set(i & 0x7FFF);
        }

        long r = System.currentTimeMillis() - st;
        System.out.println("run, " + bitSet.get(4));

        return r;
//        System.out.println(System.currentTimeMillis() - st);
//        System.out.println(bitSet.cardinality());
    }


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
