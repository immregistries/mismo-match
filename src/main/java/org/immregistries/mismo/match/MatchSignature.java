package org.immregistries.mismo.match;

public class MatchSignature {
  private MatchSignatureType type;
  private String value;

  protected MatchSignature(String value, MatchSignatureType type) {
    this.value = value;
    this.type = type;
  }

  public MatchSignatureType getType() {
    return type;
  }

  public String toString() {
    return value;
  }
}
