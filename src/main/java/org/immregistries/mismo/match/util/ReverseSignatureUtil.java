package org.immregistries.mismo.match.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ReverseSignatureUtil {

  private ReverseSignatureUtil() {}

  public static List<String> reverseSignatureIntoHexScores(String signature) {
    String[] parts = signature.split("\\:");

    List<String> binaries = new ArrayList<>();

    // first part of the signature is the configuration hash, skip
    for (int i = 1; i < parts.length; i++) {
      String part = parts[i];

      // base64 decoded text represents the first, second, etc. most significant
      // bits of all the binary scores concatenated
      String verticalBinary = decodeBase64(part);

      // recreate the original score binary values by appending their
      // zeroes or ones as we process the base64 text
      rebuildScoreBinaries(binaries, verticalBinary);
    }

    // at this point we have the scores represented in binary, convert to hex
    List<String> hexScores = convertBinaryValuesToHexScores(binaries);

    return hexScores;
  }

  protected static String decodeBase64(String signaturePart) {
    String decoded = "";

    for (char c : signaturePart.toCharArray()) {
      // pad out base64
      String preDecode = "AAA" + c;

      // decode base64 to string
      String decodedCharacter = new String(Base64.getDecoder().decode(preDecode.getBytes()));

      // convert the final character to binary
      String unpaddedBinary = Integer.toBinaryString(decodedCharacter.charAt(2));

      // pad out the left side with zeroes
      String paddedBinary = ("000000" + unpaddedBinary).substring(unpaddedBinary.length());

      decoded += paddedBinary;
    }
    return decoded;
  }

  protected static void rebuildScoreBinaries(List<String> binaries, String verticalBinary) {
    for (int j = 0; j < verticalBinary.length(); j++) {
      char c = verticalBinary.charAt(j);

      if (binaries.size() < j + 1) {
        binaries.add(String.valueOf(c));
      } else {
        binaries.set(j, binaries.get(j) + c);
      }
    }
  }
  
  protected static List<String> convertBinaryValuesToHexScores(List<String> binaries) {
    List<String> hexScores = new ArrayList<>();
    for (String binary : binaries) {
      int integerValue = Integer.parseInt(binary, 2);
      hexScores.add(Integer.toHexString(integerValue));
    }
    return hexScores;
  }
}
