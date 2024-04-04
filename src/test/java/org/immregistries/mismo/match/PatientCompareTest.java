package org.immregistries.mismo.match;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PatientCompareTest {
    @Test
    public void testConvertToHex()
    {
      assertEquals("0", PatientCompare.collapse("0"));
      assertEquals("0", PatientCompare.collapse("00"));
      assertEquals("0", PatientCompare.collapse("000"));
      assertEquals("0", PatientCompare.collapse("0000"));
      assertEquals("0", PatientCompare.collapse("00000"));
      assertEquals("00", PatientCompare.collapse("000000"));
      assertEquals("G", PatientCompare.collapse("1"));
      assertEquals("8", PatientCompare.collapse("01"));
      assertEquals("4", PatientCompare.collapse("001"));
      assertEquals("2", PatientCompare.collapse("0001"));
      assertEquals("10", PatientCompare.collapse("000010"));
      assertEquals("0", PatientCompare.collapse("00000"));
      assertEquals("1", PatientCompare.collapse("00001"));
      assertEquals("2", PatientCompare.collapse("00010"));
      assertEquals("3", PatientCompare.collapse("00011"));
      assertEquals("4", PatientCompare.collapse("00100"));
      assertEquals("5", PatientCompare.collapse("00101"));
      assertEquals("6", PatientCompare.collapse("00110"));
      assertEquals("7", PatientCompare.collapse("00111"));
      assertEquals("8", PatientCompare.collapse("01000"));
      assertEquals("9", PatientCompare.collapse("01001"));
      assertEquals("A", PatientCompare.collapse("01010"));
      assertEquals("B", PatientCompare.collapse("01011"));
      assertEquals("C", PatientCompare.collapse("01100"));
      assertEquals("D", PatientCompare.collapse("01101"));
      assertEquals("E", PatientCompare.collapse("01110"));
      assertEquals("F", PatientCompare.collapse("01111"));
      assertEquals("G", PatientCompare.collapse("10000"));
      assertEquals("H", PatientCompare.collapse("10001"));
      assertEquals("J", PatientCompare.collapse("10010"));
      assertEquals("K", PatientCompare.collapse("10011"));
      assertEquals("M", PatientCompare.collapse("10100"));
      assertEquals("N", PatientCompare.collapse("10101"));
      assertEquals("P", PatientCompare.collapse("10110"));
      assertEquals("Q", PatientCompare.collapse("10111"));
      assertEquals("R", PatientCompare.collapse("11000"));
      assertEquals("S", PatientCompare.collapse("11001"));
      assertEquals("T", PatientCompare.collapse("11010"));
      assertEquals("U", PatientCompare.collapse("11011"));
      assertEquals("V", PatientCompare.collapse("11100"));
      assertEquals("X", PatientCompare.collapse("11101"));
      assertEquals("Y", PatientCompare.collapse("11110"));
      assertEquals("Z", PatientCompare.collapse("11111"));
      assertEquals("Z0", PatientCompare.collapse("111110"));
      assertEquals("ZG", PatientCompare.collapse("111111"));
      assertEquals("Z8", PatientCompare.collapse("1111101"));
      assertEquals("Z4", PatientCompare.collapse("11111001"));
      assertEquals("Z2", PatientCompare.collapse("111110001"));
      assertEquals("Z1", PatientCompare.collapse("1111100001"));
      assertEquals("Z0", PatientCompare.collapse("1111100000"));
      assertEquals("Z00", PatientCompare.collapse("11111000000"));
    }
}
