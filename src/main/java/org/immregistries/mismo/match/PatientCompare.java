package org.immregistries.mismo.match;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.immregistries.mismo.match.matchers.AggregateMatchNode;
import org.immregistries.mismo.match.matchers.MatchNode;
import org.immregistries.mismo.match.model.Configuration;
import org.immregistries.mismo.match.model.MatchItem;
import org.immregistries.mismo.match.model.Patient;


/**
 * Holds all comparison nodes and provides support to give final result.
 * @author Nathan Bunker
 *
 */
public class PatientCompare {

  private String ancestry = "";
  private Date born = new Date();
  private AggregateMatchNode match;
  private AggregateMatchNode notMatch;
  private AggregateMatchNode twin;
  private AggregateMatchNode missing;
  private String version = "";
  private String lastModifiedBy = "";
  private Date lastModifiedDate = new Date();
  private List<String> changeLogList = new ArrayList<>();
  Configuration configuration = new Configuration();

  public Configuration getConfiguration()
  {
    return configuration;
  }

  protected List<Double> getScoreList()
  {
    if (patientA == null || patientB == null) {
      throw new IllegalArgumentException("Unable to get list of match notes and score map because patient A and patient B were not set");
    }
    List<Double> scoreList = new ArrayList<>();
    match.populateScoreList(patientA, patientB, scoreList);
    notMatch.populateScoreList(patientA, patientB, scoreList);
    twin.populateScoreList(patientA, patientB, scoreList);
    missing.populateScoreList(patientA, patientB, scoreList);
    return scoreList;
  }

  public void populateMatchNodeListAndScoreMap(List<MatchNode> matchNodeList, Map<MatchNode, Double> scoreMap) {
    if (patientA == null || patientB == null) {
      throw new IllegalArgumentException("Unable to get list of match notes and score map because patient A and patient B were not set");
    }
    match.populateMatchNodeListAndScoreMap(patientA, patientB, matchNodeList, scoreMap);
    notMatch.populateMatchNodeListAndScoreMap(patientA, patientB, matchNodeList, scoreMap);
    twin.populateMatchNodeListAndScoreMap(patientA, patientB, matchNodeList, scoreMap);
    missing.populateMatchNodeListAndScoreMap(patientA, patientB, matchNodeList, scoreMap);
  }



  public String getSignature()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(configuration.getHashForSignature());
    List<Double> scoreList = getScoreList();
    String signature1 = "";
    String signature2 = "";
    String signature3 = "";
    String signature4 = "";
    
    for (Double score : scoreList) {
      int scoreInt = (int) (score * 15);
      signature4 += scoreInt % 2;
      scoreInt = scoreInt >> 1;
      signature3 += scoreInt % 2;
      scoreInt = scoreInt >> 1;
      signature2 += scoreInt % 2;
      scoreInt = scoreInt >> 1;
      signature1 += scoreInt % 2;
    }
    sb.append(":" + collapse(signature1));
    sb.append(":" + collapse(signature2));
    sb.append(":" + collapse(signature3));
    sb.append(":" + collapse(signature4));
    return sb.toString();
  }

  protected static String collapse(String s)
  {
    String collapse = "";
     while (s.length() > 0)
     {
      if (s.length() < 6)
      {
        s = s + "000000";
        s = s.substring(0, 6);
      }

      // pad out to eight bits
      String encode = String.format("00%s", (char) Integer.parseInt(s.substring(0, 6), 2));

      // encode to base64
      String encoded = new String(Base64.getEncoder().encode(encode.getBytes()));

      // we only care about the final character (of four) after padding out
      collapse += encoded.charAt(encoded.length() - 1);
      
      s = s.substring(6);
     } 
     return collapse;
  }

  /**
   * Not yet used, but could be used for recording ancestry information.
   * @return
   */
  public String getAncestry() {
    return ancestry;
  }

  /**
   * Not yet used, but could be used for recording ancestry information.
   * @param ancestry
   */
  public void setAncestry(String ancestry) {
    this.ancestry = ancestry;
  }

  /**
   * Creates default patient compare based off of the default script.    
   */
  public PatientCompare() {
    match = configuration.getMatch();
    notMatch = configuration.getNotMatch();
    twin = configuration.getTwin();
    missing = configuration.getTwin();
  }

  public PatientCompare(InputStream in)
  {
    configuration = new Configuration(in);
    match = configuration.getMatch();
    notMatch = configuration.getNotMatch();
    twin = configuration.getTwin();
    missing = configuration.getTwin();
  }

  public PatientCompare(String configurationText) {
    configuration = new Configuration(configurationText);
    match = configuration.getMatch();
    notMatch = configuration.getNotMatch();
    twin = configuration.getTwin();
    missing = configuration.getTwin();
  }

  public void disableMatchNodes(Map<String, Boolean> matchNodeMap) {
    match.disableMatchNodes(matchNodeMap);
    notMatch.disableMatchNodes(matchNodeMap);
    twin.disableMatchNodes(matchNodeMap);
    missing.disableMatchNodes(matchNodeMap);
  }

  /**
   * Sets the matchTestCase that contains the patient data that will be compared.
   * @param matchItem
   */
  public void setMatchItem(MatchItem matchItem) {
    patientA = new Patient(matchItem.getPatientDataA());
    patientB = new Patient(matchItem.getPatientDataB());
    result = null;
  }

  /**
   * Clears previously calculated results and matchTestCase.   
   */
  public void clear() {
    patientA = null;
    patientB = null;
    result = null;
  }

  private Patient patientA = null;

  /**
   * @return base match node
   */
  public AggregateMatchNode getMatch() {
    return match;
  }

  /**
   * @param match the base match node
   */
  public void setMatch(AggregateMatchNode match) {
    this.match = match;
  }

  /**
   * @return base not match node
   */
  public AggregateMatchNode getNotMatch() {
    return notMatch;
  }

  /**
   * @param notMatch base not match node
   */
  public void setNotMatch(AggregateMatchNode notMatch) {
    this.notMatch = notMatch;
  }

  /**
   * 
   * @return base twin node
  */
  public AggregateMatchNode getTwin() {
    return twin;
  }

  /**
   * @param twin base twin node
   */
  public void setTwin(AggregateMatchNode twin) {
    this.twin = twin;
  }

  /**
   * Evaluates the match set and returns verdict. 
   * @return "Match", "Possible Match" or "Not a Match"
   */
  public String getResult() {
    if (result == null) {
      boolean matchSignal = match.hasSignal(patientA, patientB);
      boolean notMatchSignal = notMatch.hasSignal(patientA, patientB);
      boolean missingSignal = missing.hasSignal(patientA, patientB);
      boolean twinSignal = twin.hasSignal(patientA, patientB);
      if (matchSignal) {
        if (notMatchSignal || missingSignal || twinSignal) {
          result = "Possible Match";
        } else {
          result = "Match";
        }
      } else {
        result = "Not a Match";
      }
    }
    return result;
  }

  /**
   * @return missing the missing node
   */
  public AggregateMatchNode getMissing() {
    return missing;
  }

  /**
   * @param missing  the missing node
   */
  public void setMissing(AggregateMatchNode missing) {
    this.missing = missing;
  }

  private String result = null;

  /**
   * @return patient A from match set
   */
  public Patient getPatientA() {
    return patientA;
  }

  /**
   * @param patientA for matching
   */
  public void setPatientA(Patient patientA) {
    this.patientA = patientA;
  }

  /**
   * @return patient B from match set
   */
  public Patient getPatientB() {
    return patientB;
  }

  /**
   * @param patientB for matching
   */
  public void setPatientB(Patient patientB) {
    this.patientB = patientB;
  }

  private Patient patientB = null;

  public Date getBorn() {
    return born;
  }

  public void setBorn(Date born) {
    this.born = born;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public List<String> getChangeLogList() {
    return changeLogList;
  }

  public void setChangeLogList(List<String> changeLogList) {
    this.changeLogList = changeLogList;
  }
  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }
}
