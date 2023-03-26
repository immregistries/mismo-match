package org.immregistries.mismo.match.matchers;

import java.util.Random;
import org.immregistries.mismo.match.PatientCompare;
import org.immregistries.mismo.match.model.Patient;


/**
 * Represents a basic match criteria and is the parent class for all 
 * Match Nodes. 
 * @author Nathan Bunker
 *
 */
public abstract class MatchNode {
  private String matchName = "";

  private double maxScore = 0.0;
  private double minScore = 1.0;
  private boolean enabled = true;
  protected boolean not = false;

  /**
   * Compiles a string version of this match node that
   * can be used as part of the script defining this match node.
   * @return script for documenting and reconstructing this match node
   */
  public abstract String makeScript();

  /**
   * Creates basic script that all match nodes start off when making their script.
   * This method is called when makeScript() is implemented by the 
   * extended classes. The script is made of 5 fields, separated by colons. 
   * (Colons can not be used otherwise as data in the script.) The fields
   * are defined to contain the following:
   * <ul>
   *   <li>Match Name: The defined name of this match.</li>
   *   <li>Max Score: The maximum score that can be given for this node.</li>
   *   <li>Min Score: The minimum score that can be given for this node.</li>
   *   <li>Option List: A list of comma separated options:</li>
   *   <ul>
   *     <li>disabled: indicates that this node will not be evaluated. (Cascades to all subordinate nodes)</li>
   *     <li>not: indicates that this node will operate as a <em>not</em> node. </li>
   *   </ul>
   *   <li>Additional subordinate nodes as needed</li>
   * </ul>
   * Example: <code>L-match:1.0:0.0::</code>
   * <p>
   * Example with subordinate nodes: <code>Last Name:0.4:0.0::{L-match:1.0:0.0::}{L-similar:0.4:0.0::}{L-hyphenated:0.6:0.0::}</code>
   * <p>
   * The subordinate notes are attached by the AggregateMatchNode. All other nodes 
   * do not use the 5th field and leave it empty. 
   * @return
   */
  protected String makeBasicScript() {
    String optionList = "";
    if (!enabled) {
      optionList = "disabled";
    }
    if (not) {
      if (optionList.length() > 0) {
        optionList += ",not";
      } else {
        optionList = "not";
      }
    }
    return matchName + ":" + maxScore + ":" + minScore + ":" + optionList + ":";
  }

  /**
   * The reverse of the makeScript method, this reads the script and 
   * populates the current node tree with the weights and settings.
   * Please note that this script does not delete or add nodes, 
   * it only updates those that are mentioned in the script. 
   * @param script that was generated by makeScript() method
   * @param pos within the script that is currently being parsed
   * @return next pos to read within script
   */
  public abstract int readScript(String script, int pos);

  /**
   * This method provides primary support for reading standard node. 
   * The format of this script is specified in the makeBasicScript() method.
   * 
   * @param script that was generated by makeScript() method
   * @param pos within the script that is currently being parsed
   * @return next pos to read within script
   */
  public int readBasicScript(String script, int pos) {
    int colPos = nextColon(script, pos);
    matchName = script.substring(pos, colPos);
    pos = colPos + 1;
    if (pos < script.length()) {
      colPos = nextColon(script, pos);
      maxScore = Double.parseDouble(script.substring(pos, colPos));
      pos = colPos + 1;
      if (pos < script.length()) {
        colPos = nextColon(script, pos);
        minScore = Double.parseDouble(script.substring(pos, colPos));
        pos = colPos + 1;
        if (pos < script.length()) {
          colPos = nextColon(script, pos);
          String[] parts = script.substring(pos, colPos).split("\\,");
          for (int i = 0; i < parts.length; i++) {
            if (parts[i] != null) {
              if (parts[i].equals("disabled")) {
                enabled = false;
              } else if (parts[i].equals("not")) {
                not = true;
              }
            }
          }
          pos = colPos + 1;
        }
      }
    }
    return pos;
  }

  /**
   * Safely finds and returns the position of the next colon. If end brace '{' or 
   * end of string is
   * found before the colon is found then that position is returned. 
   * @param script that is being read
   * @param position within current script
   * @return position of the next colon
   */
  protected int nextColon(String script, int pos) {
    int endBracket = script.indexOf("}", pos);
    pos = script.indexOf(':', pos);
    if (pos > endBracket)
    {
      return endBracket;
    }
    if (pos == -1) {
      pos = script.length();
    }
    return pos;
  }

  @Override
  public int hashCode() {
    return ("" + maxScore + minScore + enabled).hashCode();
  }

  /**
   * @return <code>true</code> if match node and any subordinated nodes 
   * will be evaluated and included
   * in scoring. 
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * @param enabled indicates if match node and any subordinated nodes
   * should be evaluated and included in the matching process. 
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * This convenience method sets not = <code>true</code> and returns
   * this object to allow for setting of more options. Not indicates
   * that this node should operate in reverse. For example, if 
   * a regular node returns 0.8 for a match score, the not in most cases 
   * return 0.2. In other cases a score of 0.0 may indicate that
   * information was missing so no scoring was done, which in this case,
   * the not score may also return 0.0 instead of 0.1. (This semantically
   * means that for a particular condition two fields may not be the same
   * and the may not be different, consider these strings '' and 'Henry', they 
   * neither match or not-match because information is missing.)
   * @return this object
   */
  public MatchNode setNot() {
    this.not = true;
    return this;
  }

  /**
   * @return maximum score that this node can return
   */
  public double getMaxScore() {
    return maxScore;
  }

  /**
   * @return minimum score that this node can return
   */
  public double getMinScore() {
    return minScore;
  }

  /**
   * @param maximum score that this node can return
   */
  public void setMaxScore(double maxScore) {
    this.maxScore = maxScore;
  }

  /**
   * @param mininum score that this node can return
   */
  public void setMinScore(double minScore) {
    this.minScore = minScore;
  }

  public MatchNode() {
    // default
  }

  /**
   * Convenience constructor for sub classes.
   * @param match name, specifically identifying this match.
   * @param mininum score that this node can return (must be between 0.0 and 1.0 inclusively)
   * @param maximum score that this node can return (must be between 0.0 and 1.0 inclusively and not less than mininum score)
   */
  protected MatchNode(String matchName, double minScore, double maxScore) {
    this.matchName = matchName;
    this.maxScore = maxScore;
    this.minScore = minScore;
    if (maxScore > 1.0 || maxScore < 0.0) {
      throw new IllegalArgumentException("Max score must be between 0.0 and 1.0");
    }
    if (minScore > 1.0 || minScore < 0.0) {
      throw new IllegalArgumentException("Min score must be between 0.0 and 1.0");
    }
    if (maxScore < minScore) {
      throw new IllegalArgumentException("Max score must greater than or equal to min score");
    }
  }

  /**
   * @return unique and human readable name for this match node
   */
  public String getMatchName() {
    return matchName;
  }

  /**
   * Scores the match between two patients for this match node. The value 
   * returned here is the unweighted score and will vary between 0.0 and
   * 1.0. At this point the min and max score have not been used
   * to adjust the score. 
   * @param patientA the target of the comparison
   * @param patientB the other patient to compare to the target for scoring
   * @return a score between 0.0 and 1.0 inclusively indicating the if this node matches.
   */
  public double score(Patient patientA, Patient patientB) {
    return 0.0;
  }

  /**
   * This is a convenience method for scoring two patients that are included in a PatientCompare object.
   * @param patientCompare the set of patients that need to be scored
   * @return a score between 0.0 and 1.0 inclusively indicating if this node matches.
   */
  public double score(PatientCompare patientCompare) {
    return score(patientCompare.getPatientA(), patientCompare.getPatientB());
  }

  /**
   * Scores the match between two patients for this match node. The value
   * returned here is the weight score and will be between the minScore
   * and maxScore for this node. 
   * @param patientCompare the two patients to be compared
   * @return a score between the minScore and maxScore inclusively indicating if this node matches.
   */
  public double weightScore(PatientCompare patientCompare) {
    return weightScore(patientCompare.getPatientA(), patientCompare.getPatientB());
  }

  /**
   * Scores a match between patient A and patient B and returns 
   * final weight score between minScore and maxScore for this node. 
   * @param patientA the target of the comparison
   * @param patientB the other patient to compare to the target for scoring
   * @return a score between minScore and maxScore inclusively indicating the if this node matches.
   */
  public double weightScore(Patient patientA, Patient patientB) {
    return score(patientA, patientB) * (maxScore - minScore) + minScore;
  }

  /**
   * Indicates if the node has a signal, which means the weightScore
   * for this node is 0.5 or greater. 
   * @param patientCompare the two patients to be compared
   * @return <code>true</code> if this node signals that condition exists. 
   */
  public boolean hasSignal(PatientCompare patientCompare) {
    return hasSignal(patientCompare.getPatientA(), patientCompare.getPatientB());
  }

  /**
   * Indicates if the node has a signal, which means the weightScore
   * for this node is 0.5 or greater. 
   * @param patientA the target of the comparison
   * @param patientB the other patient to compare to the target for scoring
   * @return <code>true</code> if this node signals that condition exists. 
   */
  public boolean hasSignal(Patient patientA, Patient patientB) {
    return weightScore(patientA, patientB) >= 0.5;
  }

  /**
   * Initialization method that creates base <b>Match</b> node with all possible nodes and 
   * default working values. The values indicated in this method are meant to represent the
   * current recommended starting points for values but not the values that have been
   * optimized for specific installations. 
   * @return base Match node
   */
  public static AggregateMatchNode createPatientMatchNode() {
    AggregateMatchNode match = new AggregateMatchNode("Match", 0.0, 1.0);
    {
      AggregateMatchNode household = match.add(new AggregateMatchNode("Household", 0.0, 0.4));
      {
        AggregateMatchNode lastName = household.add(new AggregateMatchNode("Last Name", 0.0, 0.4));
        {
          lastName.add(new ExactMatchNode("L-match", 0, 1.0, Patient.NAME_LAST));
          lastName.add(new SimilarMatchNode("L-similar", 0, 0.4, Patient.NAME_LAST));
          lastName.add(new HyphenMatchNode("L-hyphenated", 0, 0.6, Patient.NAME_LAST));
        }
        AggregateMatchNode guardian = household.add(new AggregateMatchNode("Guardian", 0.0, 0.3));
        {
          guardian.add(new ExactMatchNode("GF-match", 0, 0.5, Patient.GUARDIAN_NAME_FIRST));
          guardian.add(new ExactMatchNode("GL-match", 0, 0.5, Patient.GUARDIAN_NAME_LAST));
          guardian.add(new ExactMatchNode("GF-GL-match", 0, 0.5, Patient.GUARDIAN_NAME_FIRST, Patient.GUARDIAN_NAME_LAST));
          guardian.add(new ExactMatchNode("MM-match", 0, 0.5, Patient.MOTHER_MAIDEN_NAME));
          guardian.add(new ExactMatchNode("GL-MM-match", 0, 0.5, Patient.MOTHER_MAIDEN_NAME, Patient.GUARDIAN_NAME_LAST));
        }
        AggregateMatchNode location = household.add(new AggregateMatchNode("Location", 0.0, 0.3));
        {
          location.add(new ExactMatchNode("PN-match", 0, 0.5, Patient.PHONE));
          location.add(new AddressMatchNode("AD-match", 0, 0.7));
        }
      }
      AggregateMatchNode person = match.add(new AggregateMatchNode("Person", 0.0, 0.6));
      {
        AggregateMatchNode patientId = person.add(new AggregateMatchNode("Patient Id", 0.0, 0.8));
        {
          patientId.add(new AtLeastOneExactMatchNode("MRN-match", 0.0, 0.9, Patient.MRNS,"|"));
        	//patientId.add(new ExactMatchNode("MRN-match", 0.0, 0.9, Patient.MRNS));
          patientId.add(new ExactMatchNode("SSN-match", 0.0, 0.7, Patient.SSN));
          patientId.add(new ExactMatchNode("MA-match", 0.0, 0.9, Patient.MEDICAID));
        }
        AggregateMatchNode firstName = person.add(new AggregateMatchNode("First Name", 0.0, 0.3));
        {
          firstName.add(new ExactMatchNode("F-match", 0.0, 0.9, Patient.NAME_FIRST));
          firstName.add(new SimilarMatchNode("F-similar", 0.0, 0.5, Patient.NAME_FIRST));
          firstName.add(new ExactMatchNode("F-middle", 0.0, 0.4, Patient.NAME_FIRST, Patient.NAME_MIDDLE));
          firstName.add(new ExactMatchNode("A-match", 0.0, 0.4, Patient.NAME_FIRST, Patient.NAME_ALIAS));
          firstName.add(new ExactMatchNode("G-match", 0.0, 0.1, Patient.GENDER));
        }
        AggregateMatchNode birthOrder = person.add(new AggregateMatchNode("Birth Order", 0.0, 0.1));
        {
          birthOrder.add(new BirthOrderMatchNode("BO-matches", 0.0, 1.0));
          birthOrder.add(new BirthNotIndicatedMatchNode("MBS-no", 0.0, 0.5));
        }
        AggregateMatchNode dob = person.add(new AggregateMatchNode("DOB", 0.0, 0.4));
        {
          dob.add(new ExactMatchNode("DOB-match", 0.0, 1.0, Patient.BIRTH_DATE));
          dob.add(new SimilarDateMatchNode("DOB-similar", 0.0, 0.65, Patient.BIRTH_DATE));
        }
        AggregateMatchNode middleName = person.add(new AggregateMatchNode("Middle Name", 0.0, 0.3));
        {
          middleName.add(new ExactMatchNode("M-match", 0.0, 1.0, Patient.NAME_MIDDLE));
          middleName.add(new SameInitialMatchNode("M-initial", 0.0, 0.6, Patient.NAME_MIDDLE));
          middleName.add(new SimilarMatchNode("M-similar", 0.0, 0.6, Patient.NAME_MIDDLE));
          middleName.add(new DoNotConflictMatchNode("S-match", 0.0, 0.1, Patient.NAME_SUFFIX));
        }
        AggregateMatchNode shotHistory = person.add(new AggregateMatchNode("Shot History", 0.0, 0.2));
        {
          shotHistory.add(new HistoryMatchNode("SH-match", 0.0, 0.1, Patient.SHOT_HISTORY));
        }
      }
    }
    return match;
  }

  /**
   * Initialization method that creates base <b>Missing</b> node with all possible nodes and 
   * default working values. The values indicated in this method are meant to represent the
   * current recommended starting points for values but not the values that have been
   * optimized for specific installations. 
   * @return base Missing node
   */
  public static AggregateMatchNode createMissingMatchNode() {
    AggregateMatchNode missing = new AggregateMatchNode("Missing", 0.0, 1.0);
    {
      AggregateMatchNode household = missing.add(new AggregateMatchNode("Household", 0.0, 0.4));
      {
        household.add(new MissingMatchNode("L-missing", 0, 0.5, Patient.NAME_LAST));
        AggregateMatchNode guardian = household.add(new AggregateMatchNode("Household", 0.0, 0.4));
        {
          guardian.add(new MissingMatchNode("GFN-missing", 0, 0.3, Patient.GUARDIAN_NAME_FIRST));
          guardian.add(new MissingMatchNode("GLN-missing", 0, 0.3, Patient.GUARDIAN_NAME_LAST));
          guardian.add(new MissingMatchNode("MMN-missing", 0, 0.3, Patient.MOTHER_MAIDEN_NAME));
        }
        AggregateMatchNode location = household.add(new AggregateMatchNode("Location", 0.0, 0.4));
        {
          location.add(new MissingMatchNode("PN-missing", 0, 0.2, Patient.PHONE));
          location.add(new MissingMatchNode("AS1-missing", 0, 0.4, Patient.ADDRESS_STREET1, Patient.ADDRESS_2_STREET1, Patient.ADDRESS_3_STREET1));
          location.add(new MissingMatchNode("AS2-missing", 0, 0, Patient.ADDRESS_STREET2, Patient.ADDRESS_2_STREET2, Patient.ADDRESS_3_STREET2));
          location.add(new MissingMatchNode("AC-missing", 0, 0.05, Patient.ADDRESS_CITY, Patient.ADDRESS_2_CITY, Patient.ADDRESS_3_CITY));
          location.add(new MissingMatchNode("AS-missing", 0, 0.05, Patient.ADDRESS_STATE, Patient.ADDRESS_2_STATE, Patient.ADDRESS_3_STATE));
          location.add(new MissingMatchNode("AZ-missing", 0, 0.05, Patient.ADDRESS_ZIP, Patient.ADDRESS_2_ZIP, Patient.ADDRESS_3_ZIP));
        }
      }
      AggregateMatchNode person = missing.add(new AggregateMatchNode("Person", 0.0, 0.6));
      {
        AggregateMatchNode patientId = person.add(new AggregateMatchNode("Patient Id", 0.0, 0.3));
        {
          patientId.add(new MissingMatchNode("MRN-missing", 0, 1, Patient.MRNS));
          patientId.add(new MissingMatchNode("SSN-missing", 0, 0.4, Patient.SSN));
          patientId.add(new MissingMatchNode("MA-missing", 0, 0.4, Patient.MEDICAID));
        }
        AggregateMatchNode firstName = person.add(new AggregateMatchNode("First Name", 0.0, 0.3));
        {
          firstName.add(new MissingMatchNode("F-missing", 0, 1, Patient.NAME_FIRST));
          firstName.add(new MissingMatchNode("A-missing", 0, 0, Patient.NAME_ALIAS));
          firstName.add(new MissingMatchNode("S-missing", 0, 0, Patient.NAME_SUFFIX));
          firstName.add(new MissingMatchNode("G-missing", 0, 0.6, Patient.GENDER));
        }
        AggregateMatchNode birthOrder = person.add(new AggregateMatchNode("Birth Order", 0.0, 0.1));
        {
          birthOrder.add(new MissingMatchNode("MBS-missing", 0, 1, Patient.BIRTH_STATUS));
          birthOrder.add(new MissingMatchNode("BM-missing", 0, 0.5, Patient.BIRTH_TYPE));
          birthOrder.add(new MissingMatchNode("BO-missing", 0, 0.5, Patient.BIRTH_ORDER));
        }
        person.add(new MissingMatchNode("DOB-missing", 0, 0.4, Patient.BIRTH_DATE));
        person.add(new MissingMatchNode("M-missing", 0, 0.2, Patient.NAME_MIDDLE));
        person.add(new MissingMatchNode("SH-missing", 0, 0.2, Patient.SHOT_HISTORY));
      }
    }
    return missing;
  }

  /**
   * Initialization method that creates base <b>Suspect Twin</b> node with all possible nodes and 
   * default working values. The values indicated in this method are meant to represent the
   * current recommended starting points for values but not the values that have been
   * optimized for specific installations. 
   * @return base Suspect Twin node
   */
  public static AggregateMatchNode createTwinMatchNode() {
    AggregateMatchNode twin = new AggregateMatchNode("Suspect Twin", 0.0, 1.0);
    {
      AggregateMatchNode nameDifferent = twin.add(new AggregateMatchNode("Name Different", 0.0, 0.2));
      {
        nameDifferent.add(new ExactMatchNode("F-not-match", 0.0, 0.5, Patient.NAME_FIRST).setNot());
        nameDifferent.add(new SimilarMatchNode("F-not-similar", 0.0, 0.6, Patient.NAME_FIRST).setNot());
        nameDifferent.add(new ExactMatchNode("M-not-match", 0.0, 0.5, Patient.NAME_MIDDLE).setNot());
        nameDifferent.add(new ExactMatchNode("G-not-match", 0.0, 0.5, Patient.GENDER).setNot());
      }
      AggregateMatchNode birthDate = twin.add(new AggregateMatchNode("Birth Date", 0.0, 0.2));
      {
        birthDate.add(new ExactMatchNode("DOB-match", 0.0, 1.0, Patient.BIRTH_DATE));
      }
      AggregateMatchNode birthStatus = twin.add(new AggregateMatchNode("Birth Status", 0.0, 0.6));
      {
        birthStatus.add(new BirthMayBeTwinMatchNode("MBS-maybe", 0.0, 0.5));
        birthStatus.add(new BirthHasTwinMatchNode("MBS-yes", 0.0, 0.1));
      }
    }
    return twin;
  }

  /**
   * Initialization method that creates base <b>Not Match</b> node with all possible nodes and 
   * default working values. The values indicated in this method are meant to represent the
   * current recommended starting points for values but not the values that have been
   * optimized for specific installations. 
   * @return base Not Match node
   */
  public static AggregateMatchNode createPatientNotMatchNode() {
    AggregateMatchNode notmatch = new AggregateMatchNode("Not Match", 0.0, 1.0);
    {
      AggregateMatchNode household = notmatch.add(new AggregateMatchNode("Household", 0.0, 0.4));
      {
        AggregateMatchNode lastName = household.add(new AggregateMatchNode("Last Name", 0.0, 0.4));
        {
          lastName.add(new ExactMatchNode("L-not-match", 0, 1.0, Patient.NAME_LAST).setNot());
          lastName.add(new SimilarMatchNode("L-not-similar", 0, 0.4, Patient.NAME_LAST).setNot());
          lastName.add(new HyphenMatchNode("L-not-hyphenated", 0, 0.6, Patient.NAME_LAST).setNot());
        }
        AggregateMatchNode guardian = household.add(new AggregateMatchNode("Guardian", 0.0, 0.3));
        {
          guardian.add(new ExactMatchNode("GF-not-match", 0, 0.5, Patient.GUARDIAN_NAME_FIRST).setNot());
          guardian.add(new ExactMatchNode("GL-not-match", 0, 0.5, Patient.GUARDIAN_NAME_LAST).setNot());
          guardian.add(new ExactMatchNode("MM-not-match", 0, 0.5, Patient.MOTHER_MAIDEN_NAME).setNot());
          guardian.add(new SimilarMatchNode("MM-not-similar", 0, 0.5, Patient.MOTHER_MAIDEN_NAME).setNot());
        }
        AggregateMatchNode location = household.add(new AggregateMatchNode("Location", 0.0, 0.3));
        {
          location.add(new ExactMatchNode("PN-not-match", 0, 0.4, Patient.PHONE).setNot());
          location.add(new AddressMatchNode("AD-not-match", 0, 0.9).setNot());
        }
      }
      AggregateMatchNode person = notmatch.add(new AggregateMatchNode("Person", 0.0, 0.6));
      {
        AggregateMatchNode patientId = person.add(new AggregateMatchNode("Patient Id", 0.0, 0.8));
        {
          patientId.add(new AtLeastOneExactMatchNode("MRN-not-match", 0.0, 0.05, Patient.MRNS,"|").setNot());
          patientId.add(new ExactMatchNode("SSN-not-match", 0.0, 0.9, Patient.SSN).setNot());
          patientId.add(new ExactMatchNode("MA-not-match", 0.0, 0.6, Patient.MEDICAID).setNot());
        }
        AggregateMatchNode firstName = person.add(new AggregateMatchNode("First Name", 0.0, 0.3));
        {
          firstName.add(new ExactMatchNode("F-not-match", 0.0, 0.3, Patient.NAME_FIRST).setNot());
          firstName.add(new SimilarMatchNode("F-not-similar", 0.0, 0.3, Patient.NAME_FIRST).setNot());
          firstName.add(new ExactMatchNode("F-not-middle", 0.0, 0.0, Patient.NAME_FIRST, Patient.NAME_MIDDLE).setNot());
          firstName.add(new ExactMatchNode("A-not-match", 0.0, 0.0, Patient.NAME_FIRST, Patient.NAME_ALIAS).setNot());
          firstName.add(new ExactMatchNode("G-not-match", 0.0, 0.1, Patient.GENDER).setNot());
        }
        AggregateMatchNode birthOrder = person.add(new AggregateMatchNode("Birth Order", 0.0, 0.1));
        {
          // TODO
        }
        AggregateMatchNode gender = person.add(new AggregateMatchNode("Gender", 0.0, 0.4));
        {
          gender.add(new ExactMatchNode("G-not-match", 0.0, 0.0, Patient.GENDER).setNot());
        }
        AggregateMatchNode dob = person.add(new AggregateMatchNode("DOB", 0.0, 0.4));
        {
          dob.add(new ExactMatchNode("DOB-not-match", 0.0, 1.0, Patient.BIRTH_DATE).setNot());
          dob.add(new SimilarDateMatchNode("DOB-not-similar", 0.0, 0.65, Patient.BIRTH_DATE).setNot());
        }
        AggregateMatchNode middleName = person.add(new AggregateMatchNode("Middle Name", 0.0, 0.3));
        {
          middleName.add(new ExactMatchNode("M-not-match", 0.0, 1.0, Patient.NAME_MIDDLE).setNot());
          middleName.add(new SameInitialMatchNode("M-not-initial", 0.0, 0.6, Patient.NAME_MIDDLE).setNot());
          middleName.add(new SimilarMatchNode("M-not-similar", 0.0, 0.6, Patient.NAME_MIDDLE).setNot());
          middleName.add(new DoNotConflictMatchNode("S-not-match", 0.0, 0.1, Patient.NAME_SUFFIX).setNot());
        }
        AggregateMatchNode shotHistory = person.add(new AggregateMatchNode("Shot History", 0.0, 0.2));
        {
          shotHistory.add(new HistoryMatchNode("SH-not-match", 0.0, 0.1, Patient.SHOT_HISTORY).setNot());
        }
      }
    }
    return notmatch;
  }

  /**
   * Used by sub class implementations to easily indicate a value for regular and not match nodes. This allows
   * for simple development of a single match node that may be used in normal form or in NOT form. This method
   * takes a boolean positive assert that if true indicates that the condition exists and a value to assign
   * if so. Otherwise, if the positive assert is false then 1.0 - value is assigned as the value. In addition,
   * if this is a NOT node then the value is again inverted before returning.   
   * @param positiveAssert indicates if the condition exists for the node to be true
   * @param value the value to assign when the condition is true
   * @return
   */
  protected double ifTrueOrNot(boolean positiveAssert, double value) {
    if (positiveAssert) {
      if (not) {
        return 1.0 - value;
      } else {
        return value;
      }
    } else {
      if (not) {
        return value;
      } else {
        return 1.0 - value;
      }
    }
  }

  /**
   * Used by the sub class implementations to quickly change positive match scores into the reverse
   * NOT scores if this node is a NOT node. The score value is the score that would be given if this
   * was a normal node. This value is returned unchanged for normal nodes, and flipped (1.0 - value) 
   * for NOT nodes. 
   * @param score the match score for normal node
   * @return the actual score for normal mode or NOT mode
   */
  protected double ifTrue(double score) {
    if (not) {
      return 1.0 - score;
    } else {
      return score;
    }
  }

  /**
   * Returns human readable description of how these two values are being compared by this
   * match node. This is used by optimization user interface to allow super users to inspect
   * how the matching algorithm is working.
   * @param patientA the target of the comparison
   * @param patientB the other patient to compare to the target for scoring
   * @return  a human readable description of what match is being done (e.g. 'Henry' == 'Henry')
   */
  public abstract String getDescription(Patient patientA, Patient patientB);
  
  /**
   * Returns a signature or summary of how his comparison looks. The signature simplifies the match
   * characteristics between two patients being matched. This allows for lumping similar matches
   * together for comparison. 
   * @param patientA the target of the comparison
   * @param patientB the other patient to compare to the target for scoring
   * @return a string displaying how this node is matching
   */
  public abstract String getSignature(Patient patientA, Patient patientB);

  protected static Random random = new Random();

  /**
   * Randomizes the minScore and maxScore to a set of values between 0.0 and 1.0 inclusively where
   * the minScore <= maxScore. This method is used during optimization.  
   */
  public void makeRandom() {
    minScore = -1;
    while (minScore == -1 || minScore > maxScore) {
      minScore = random.nextDouble();
      maxScore = random.nextDouble();
    }
  }

  /**
   * Supports a quick check to find out of the potential mate has the same DNA as this one. There is
   * no benefit to mating the same creature to another as the result is the exactly the same as 
   * both parents. This check is to prevent that from happening. 
   * @param potentialMate MatchNode that may be mated with this match node.
   * @return
   */
  public boolean isSame(MatchNode potentialMate) {
    return potentialMate.minScore == this.minScore && potentialMate.maxScore == this.maxScore && potentialMate.enabled == this.enabled;
  }

  /**
   * In biology random mutations sometimes occur. Not sure how useful this is in biology or here as most
   * variety comes from the recombination of different values. But it's here anyways. The logic
   * is built so that initially mutations occur more often and then taper off as the generations increase.
   * Mutation occurs randomly 1 out of (generation + 3) times.  
   * @param generation
   */
  public void mutate(int generation) {
    if (random.nextInt(generation + 3) == 1) {
      makeRandom();
    }
  }

  /**
   * Method adds variation to creature by "tweaking" the weight +/-0.10. This method was chosen because
   * the optimal solution is likely to be close to solutions we have already selected as good. This
   * concentrates the variation around the current weights we already have. 
   *    
   */
  public void tweak() {
    double tweakedMinScore = -1;
    double tweakedMaxScore = -1;
    while (tweakedMinScore == -1 || tweakedMinScore > tweakedMaxScore) {
      tweakedMinScore = random.nextGaussian() * 0.20 + minScore;
      if (tweakedMinScore < 0) {
        tweakedMinScore = 0;
      } else if (tweakedMinScore > 1) {
        tweakedMinScore = 1;
      }
      tweakedMaxScore = random.nextGaussian() * 0.20 + minScore;
      if (tweakedMaxScore < 0) {
        tweakedMaxScore = 0;
      } else if (tweakedMaxScore > 1) {
        tweakedMaxScore = 1;
      }
    }
    minScore = tweakedMinScore;
    maxScore = tweakedMaxScore;
  }

  /**
   * Sets the minScore, maxScore and enabled randomly based on the values
   * from either parentA or parentB. All three values are chosen
   * independently with the only restriction that the final
   * minScore can not be greater than the maxScore.  
   * @param parentA the first parent
   * @param parentB the second parent
   */
  public void mate(MatchNode parentA, MatchNode parentB) {
    minScore = -1;
    if (random.nextInt(10000) == 1) {
      while (minScore == -1 || minScore > maxScore) {
        minScore = random.nextDouble();
        maxScore = random.nextDouble();
      }
    } else {
      while (minScore == -1 || minScore > maxScore) {
        minScore = random.nextBoolean() ? parentA.minScore : parentB.minScore;
        maxScore = random.nextBoolean() ? parentA.maxScore : parentB.maxScore;
        enabled = random.nextBoolean() ? parentA.enabled : parentB.enabled;
      }
    }
  }

  /**
   * Creates an exact copy of another clone. Sets the minScore, maxScore, and enabled flag. 
   * @param clone node to copy
   */
  public void clone(MatchNode clone) {
    minScore = clone.minScore;
    maxScore = clone.maxScore;
    enabled = clone.enabled;
  }

  public String toString() {
    return "[" + minScore + "," + maxScore + "]";
  }

}