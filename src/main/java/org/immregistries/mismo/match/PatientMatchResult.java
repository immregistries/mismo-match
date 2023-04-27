package org.immregistries.mismo.match;

import java.util.ArrayList;
import java.util.List;

public class PatientMatchResult {

  private PatientMatchDetermination determination = null;
  private List<MatchSignature> matchSignatureList = null;

  public PatientMatchDetermination getDetermination() {
    return determination;
  }

  protected void setDetermination(PatientMatchDetermination determination) {
    this.determination = determination;
  }

  protected void addMatchSignature(MatchSignature matchSignature) {
    if (matchSignatureList == null) {
      matchSignatureList = new ArrayList<>();
    }
    matchSignatureList.add(matchSignature);
  }

  /**
   * @param type The signature that is wanted
   * @return
   */
  public MatchSignature getMatchSignature(MatchSignatureType type) {
    if (matchSignatureList != null) {
      for (MatchSignature matchSignature : matchSignatureList) {
        if (matchSignature.getType() == type) {
          return matchSignature;
        }
      }
    }
    return null;
  }

  /**
   * Returns the list of match signatures generated as part of the match request. 
   * 
   * @return A list of signatures, may be empty but will not be null
   */
  public List<MatchSignature> getMatchSignatureList() {
    if (matchSignatureList == null) {
      matchSignatureList = new ArrayList<>();
    }
    return matchSignatureList;
  }



}
