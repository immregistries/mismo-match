package org.immregistries.mismo.match;

public class PatientMatchResult {

  private PatientMatchDetermination determination = null;
  private String signatureLevel0 = "";
  private String signatureLevel1 = "";
  private String signatureLevel2 = "";

  public PatientMatchDetermination getDetermination() {
    return determination;
  }

  protected void setDetermination(PatientMatchDetermination determination) {
    this.determination = determination;
  }

  public String getSignatureLevel0() {
    return signatureLevel0;
  }

  protected void setSignatureLevel0(String signatureLevel0) {
    this.signatureLevel0 = signatureLevel0;
  }

  public String getSignatureLevel1() {
    return signatureLevel1;
  }

  protected void setSignatureLevel1(String signatureLevel1) {
    this.signatureLevel1 = signatureLevel1;
  }

  public String getSignatureLevel2() {
    return signatureLevel2;
  }

  protected void setSignatureLevel2(String signatureLevel2) {
    this.signatureLevel2 = signatureLevel2;
  }

}
