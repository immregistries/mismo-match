package org.immregistries.mismo.match.model;

import java.io.Serializable;
import java.util.Date;

public class MatchItem implements Serializable
{

  public static final String POSSIBLE_MATCH = "Possible Match";
  public static final String NOT_A_MATCH = "Not a Match";
  public static final String MATCH = "Match";
  public static final String RESEARCH = "Research";
  public static final String NOT_SURE = "Not Sure";
  

  /**
   * 
   */
  private static final long serialVersionUID = -9120730575027352153L;
  private int matchItemId = 0;
  private String label = "";
  private String description = "";
  private MatchSet matchSet = null;
  private String patientDataA = "";
  private String patientDataB = "";
  private Patient patientA = null;
  private Patient patientB = null;
  private String expectStatus = "";
  private User user = null;
  private Date updateDate = null;
  private String dataSource = "";
  transient private boolean tested = false;
  transient private boolean pass = false;
  transient private String actualStatus = "";

  public boolean isTested() {
    return tested;
  }

  public void setTested(boolean tested) {
    this.tested = tested;
  }

  public String getActualStatus() {
    return actualStatus;
  }

  public void setActualStatus(String actualStatus) {
    this.actualStatus = actualStatus;
  }

  public boolean isPass() {
    return pass;
  }

  public void setPass(boolean pass) {
    this.pass = pass;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public int getMatchItemId() {
    return matchItemId;
  }

  public void setMatchItemId(int matchItemId) {
    this.matchItemId = matchItemId;
  }

  public MatchSet getMatchSet() {
    return matchSet;
  }

  public void setMatchSet(MatchSet matchSet) {
    this.matchSet = matchSet;
  }

  public String getPatientDataA() {
    return patientDataA;
  }

  public void setPatientDataA(String patientDataA) {
    this.patientDataA = patientDataA;
    this.patientA = new Patient(patientDataA);
  }

  public String getPatientDataB() {
    return patientDataB;
  }

  public void setPatientDataB(String patientDataB) {
    this.patientDataB = patientDataB;
    this.patientB = new Patient(patientDataB);
  }

  public Patient getPatientA() {
    return patientA;
  }

  public void setPatientA(Patient patientA) {
    this.patientA = patientA;
  }

  public Patient getPatientB() {
    return patientB;
  }

  public void setPatientB(Patient patientB) {
    this.patientB = patientB;
  }

  public String getExpectStatus() {
    return expectStatus;
  }

  public boolean isExpectedStatusSet() {
    return expectStatus != null
        && (expectStatus.equals(POSSIBLE_MATCH) || expectStatus.equals(MATCH) || expectStatus.equals(NOT_A_MATCH));
  }

  public void setExpectStatus(String expectStatus) {
    this.expectStatus = expectStatus;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getDataSource() {
    return dataSource;
  }

  public void setDataSource(String dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MatchItem && this.getMatchItemId() > 0) {
      MatchItem mi = (MatchItem) obj;
      return mi.getMatchItemId() == this.getMatchItemId();
    }
    return super.equals(obj);
  }
}
