package org.q3df.test.demo;

import org.junit.Assert;
import org.junit.Test;
import org.q3df.common.msg.Q3Huffman;

public class HuffTest {

    @Test
    public void testSymTable () {

        for (int i = 0; i < 256; i++) {

            Q3Huffman.HuffSymbol sym = Q3Huffman.byteSymbol(i);
            Assert.assertTrue(sym.isValid());

            System.out.println(String.format("char=%d, len=%d, val=%d", i, sym.getBitsLen(), sym.getHuffBits()));

            Q3Huffman.HuffSymbol revSym = Q3Huffman.findSymbol(sym.getHuffBits(), sym.getBitsLen());

            Assert.assertSame(sym, revSym);
        }
    }
}
