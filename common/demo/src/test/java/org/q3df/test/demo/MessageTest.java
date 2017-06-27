package org.q3df.test.demo;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.q3df.common.Const;
import org.q3df.common.Utils;
import org.q3df.common.msg.MessageDataReader;
import org.q3df.common.msg.MsgStreamFactory;
import org.q3df.common.msg.Q3HuffmanCoder;
import org.q3df.common.struct.EntityState;

/**
 * Created by michael on 16.06.17.
 */
public class MessageTest {


    static byte [] testData;

    @BeforeClass
    public static void init () {
        StringBuilder sb = new StringBuilder();

        sb.append("E9E5E19F3F8D3A6BC758CDF7EE96C19DEF69CA59207A10759CF33D4D79B3CF3F");
        sb.append("D62493F91E69B4BBE74FFF246ABE19B8981A79D8F91F377BE4D3DB23DF93DFC5");
        sb.append("151DECD2ECE373D86CC887E0F6C83FE5CD8E861C6C7145CF762CEBFBC941D4AF");
        sb.append("81BE03800E431F07003E74FC8BDF316046B6CA0506D3116FC3AB7E1938C3CFE7");
        sb.append("95C67DFEB126073D9F57FA871E57C8DB389BDF5E6A3275505C21EE0D00000000");
        sb.append("0000000000000000000000000000000000000000000000000000000000000000");

        testData = Utils.fromHex(sb.toString());
    }

    @Test
    public void testReadDeltaEntity01 () {

        Q3HuffmanCoder.Decoder decoder = Q3HuffmanCoder.decoder(testData);
        EntityState est = new EntityState();

        int newnumber = decoder.readBits(Const.GENTITYNUM_BITS);

        Assert.assertTrue(decoder.readDeltaEntity(est, newnumber));

        System.out.println(est.pos.trBase.vect[0]);

        Assert.assertEquals(0x45678912, est.pos.trTime);
        Assert.assertEquals(0x78EFABCD, est.pos.trDuration);
        Assert.assertEquals(0xBBCCDDEE, est.apos.trTime);

//        apos.trDelta[1] = 7.33f;

        Assert.assertEquals(0.56f, est.pos.trBase.vect[0], 0.001);
        Assert.assertEquals(7.33f, est.apos.trDelta.vect[1], 0.001);
    }

    @Test
    public void testReadDeltaEntity02 () {

        MessageDataReader msgReader = MsgStreamFactory.msgReader(testData);
        EntityState est = new EntityState();

        int newnumber = msgReader.readBits(Const.GENTITYNUM_BITS);

        Assert.assertTrue(msgReader.readDeltaEntity(est, newnumber));

        System.out.println(est.pos.trBase.vect[0]);

        Assert.assertEquals(0x45678912, est.pos.trTime);
        Assert.assertEquals(0x78EFABCD, est.pos.trDuration);
        Assert.assertEquals(0xBBCCDDEE, est.apos.trTime);

//        apos.trDelta[1] = 7.33f;

        Assert.assertEquals(0.56f, est.pos.trBase.vect[0], 0.001);
        Assert.assertEquals(7.33f, est.apos.trDelta.vect[1], 0.001);
    }

}
