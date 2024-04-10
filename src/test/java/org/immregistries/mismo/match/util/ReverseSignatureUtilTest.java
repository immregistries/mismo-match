package org.immregistries.mismo.match.util;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;

public class ReverseSignatureUtilTest {

  @Test
  public void reverseSignatureIntoScores() {
    assertHex("07000060f0006f70fff800f08ff8f0f8f000", "hash:AITy+4:Qo7iao:Qo7iao:QIbiao");

    assertHex("f8ffff9f0fff908f0007ff0f70070f070f00", "hash:/3sNBE:vXEdlU:vXEdlU:v3kdlU");
  }

  private void assertHex(String expected, String input) {
    List<String> hex = ReverseSignatureUtil.reverseSignatureIntoHexScores(input);

    assertEquals(expected, String.join("", hex));
  }
}
