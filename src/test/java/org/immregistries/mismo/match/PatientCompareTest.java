package org.immregistries.mismo.match;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class PatientCompareTest {
    @Test
    public void testConvertToHex()
    {
      assertEquals("A", PatientCompare.collapse("0"));
      assertEquals("A", PatientCompare.collapse("00"));
      assertEquals("A", PatientCompare.collapse("000"));
      assertEquals("A", PatientCompare.collapse("0000"));
      assertEquals("A", PatientCompare.collapse("00000"));
      assertEquals("A", PatientCompare.collapse("000000"));
      assertEquals("AA", PatientCompare.collapse("0000000"));
      assertEquals("g", PatientCompare.collapse("1"));
      assertEquals("Q", PatientCompare.collapse("01"));
      assertEquals("I", PatientCompare.collapse("001"));
      assertEquals("E", PatientCompare.collapse("0001"));
      assertEquals("BA", PatientCompare.collapse("0000010"));
      assertEquals("A", PatientCompare.collapse("000000"));
      assertEquals("B", PatientCompare.collapse("000001"));
      assertEquals("C", PatientCompare.collapse("000010"));
      assertEquals("D", PatientCompare.collapse("000011"));
      assertEquals("E", PatientCompare.collapse("000100"));
      assertEquals("F", PatientCompare.collapse("000101"));
      assertEquals("G", PatientCompare.collapse("000110"));
      assertEquals("H", PatientCompare.collapse("000111"));
      assertEquals("I", PatientCompare.collapse("001000"));
      assertEquals("J", PatientCompare.collapse("001001"));
      assertEquals("K", PatientCompare.collapse("001010"));
      assertEquals("L", PatientCompare.collapse("001011"));
      assertEquals("M", PatientCompare.collapse("001100"));
      assertEquals("N", PatientCompare.collapse("001101"));
      assertEquals("O", PatientCompare.collapse("001110"));
      assertEquals("P", PatientCompare.collapse("001111"));
      assertEquals("Q", PatientCompare.collapse("010000"));
      assertEquals("R", PatientCompare.collapse("010001"));
      assertEquals("S", PatientCompare.collapse("010010"));
      assertEquals("T", PatientCompare.collapse("010011"));
      assertEquals("U", PatientCompare.collapse("010100"));
      assertEquals("V", PatientCompare.collapse("010101"));
      assertEquals("W", PatientCompare.collapse("010110"));
      assertEquals("X", PatientCompare.collapse("010111"));
      assertEquals("Y", PatientCompare.collapse("011000"));
      assertEquals("Z", PatientCompare.collapse("011001"));
      assertEquals("a", PatientCompare.collapse("011010"));
      assertEquals("b", PatientCompare.collapse("011011"));
      assertEquals("c", PatientCompare.collapse("011100"));
      assertEquals("d", PatientCompare.collapse("011101"));
      assertEquals("e", PatientCompare.collapse("011110"));
      assertEquals("f", PatientCompare.collapse("011111"));
      assertEquals("g", PatientCompare.collapse("100000"));
      assertEquals("h", PatientCompare.collapse("100001"));
      assertEquals("i", PatientCompare.collapse("100010"));
      assertEquals("j", PatientCompare.collapse("100011"));
      assertEquals("k", PatientCompare.collapse("100100"));
      assertEquals("l", PatientCompare.collapse("100101"));
      assertEquals("m", PatientCompare.collapse("100110"));
      assertEquals("n", PatientCompare.collapse("100111"));
      assertEquals("o", PatientCompare.collapse("101000"));
      assertEquals("p", PatientCompare.collapse("101001"));
      assertEquals("q", PatientCompare.collapse("101010"));
      assertEquals("r", PatientCompare.collapse("101011"));
      assertEquals("s", PatientCompare.collapse("101100"));
      assertEquals("t", PatientCompare.collapse("101101"));
      assertEquals("u", PatientCompare.collapse("101110"));
      assertEquals("v", PatientCompare.collapse("101111"));
      assertEquals("w", PatientCompare.collapse("110000"));
      assertEquals("x", PatientCompare.collapse("110001"));
      assertEquals("y", PatientCompare.collapse("110010"));
      assertEquals("z", PatientCompare.collapse("110011"));
      assertEquals("0", PatientCompare.collapse("110100"));
      assertEquals("1", PatientCompare.collapse("110101"));
      assertEquals("2", PatientCompare.collapse("110110"));
      assertEquals("3", PatientCompare.collapse("110111"));
      assertEquals("4", PatientCompare.collapse("111000"));
      assertEquals("5", PatientCompare.collapse("111001"));
      assertEquals("6", PatientCompare.collapse("111010"));
      assertEquals("7", PatientCompare.collapse("111011"));
      assertEquals("8", PatientCompare.collapse("111100"));
      assertEquals("9", PatientCompare.collapse("111101"));
      assertEquals("+", PatientCompare.collapse("111110"));
      assertEquals("/", PatientCompare.collapse("111111"));
      assertEquals("/A", PatientCompare.collapse("1111110"));
      assertEquals("/g", PatientCompare.collapse("1111111"));
      assertEquals("/Q", PatientCompare.collapse("11111101"));
      assertEquals("/I", PatientCompare.collapse("111111001"));
      assertEquals("/A", PatientCompare.collapse("11111100000"));
      assertEquals("/AA", PatientCompare.collapse("1111110000000"));
    }

    @Test
    public void signature() {
      PatientCompare compare = new PatientCompare() {
        @Override
        protected List<Double> getScoreList() {
          return Arrays.asList(0.00, 0.47, 0.00, 0.00, 0.00, 0.00, 0.44, 0.00, 1.00, 0.00, 0.00,
              0.00, 0.46, 1.00, 0.48, 0.00, 1.00, 1.00, 1.00, 0.56, 0.00, 0.00, 1.00, 0.00, 0.54,
              1.00, 1.00, 0.56, 1.00, 0.00, 1.00, 0.56, 1.00, 0.00);
        }
      };
      assertEquals("FgDgzTConRk3azU:AITy+4:Qo7iao:Qo7iao:QIbiao", compare.getSignature());

      compare = new PatientCompare() {
        @Override
        protected List<Double> getScoreList() {
          return Arrays.asList(1.00, 0.55, 1.00, 1.00, 1.00, 1.00, 0.60, 1.00, 0.00, 1.00, 1.00,
              1.00, 0.60, 0.00, 0.55, 1.00, 0.00, 0.00, 0.00, 0.50, 1.00, 1.00, 0.00, 1.00, 0.50,
              0.00, 0.00, 0.50, 0.00, 1.00, 0.00, 0.50, 0.00, 1.00);
        }
      };
      assertEquals("FgDgzTConRk3azU:/3sNBE:vXEdlU:vXEdlU:v3kdlU", compare.getSignature());

      compare = new PatientCompare() {
        @Override
        protected List<Double> getScoreList() {
          return Arrays.asList(1.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.55, 0.00, 1.00, 0.00, 0.00,
              1.00, 0.75, 0.00, 0.00, 0.00, 0.00, 1.00, 1.00, 0.45, 0.00, 0.00, 1.00, 0.00, 0.20,
              1.00, 1.00, 0.45, 1.00, 0.00, 1.00, 0.45, 1.00, 0.00);
        }
      };
      assertEquals("FgDgzTConRk3azU:gphiao:gJBye4:gJhy+4:gJhi6o", compare.getSignature());
    }
}
