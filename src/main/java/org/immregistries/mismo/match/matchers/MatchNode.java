package org.immregistries.mismo.match.matchers;

import java.util.Random;
import org.immregistries.mismo.match.PatientCompare;
import org.immregistries.mismo.match.model.Patient;

/**
 * Represents a basic match criteria and is the parent class for all
 * Match Nodes.
 * 
 * @author Nathan Bunker
 *
 */
public abstract class MatchNode {

  private String matchName = "";
  private String matchNameFull = "";
  private String matchLabel = "";
  private double maxScore = 0.0;
  private double minScore = 1.0;
  private boolean enabled = true;
  protected boolean not = false;
  protected String fieldName = "";
  protected String fieldName2 = "";
  protected String fieldName3 = "";
  protected String fieldNameOther = null;
  protected String splitParameter = "";
  protected double scoreFromSignature = 0.0;

  public double getScoreFromSignature() {
    return scoreFromSignature;
  }

  public void setScoreFromSignature(double signatureScore) {
    this.scoreFromSignature = signatureScore;
  }

  public void printOut(Patient patientA, Patient patientB, String pad) {
    System.out.println(pad + matchName + ": " + score(patientA, patientB));
  }

  public String getSplitParameter() {
    return splitParameter;
  }

  public void setSplitParameter(String splitParameter) {
    this.splitParameter = splitParameter;
  }

  public static String ADDRESS = "Address";

  public String getMatchLabel() {
    return matchLabel;
  }

  public void setMatchLabel(String matchLabel) {
    this.matchLabel = matchLabel;
  }

  public boolean isNot() {
    return not;
  }

  public void setNot(boolean not) {
    this.not = not;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName2() {
    return fieldName2;
  }

  public void setFieldName2(String fieldName2) {
    this.fieldName2 = fieldName2;
  }

  public String getFieldName3() {
    return fieldName3;
  }

  public void setFieldName3(String fieldName3) {
    this.fieldName3 = fieldName3;
  }

  public String getFieldNameOther() {
    return fieldNameOther;
  }

  public void setFieldNameOther(String fieldNameOther) {
    this.fieldNameOther = fieldNameOther;
  }

  @Override
  public int hashCode() {
    return ("" + maxScore + minScore + enabled).hashCode();
  }

  public String getMatchNameFull() {
    return matchNameFull;
  }

  public void setMatchNameFull(String matchNameFull) {
    this.matchNameFull = matchNameFull;
  }

  public boolean isEnabled() {
    return enabled;
  }

  /**
   * @param enabled indicates if match node and any subordinated nodes
   *                should be evaluated and included in the matching process.
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    if (!enabled) {
      this.minScore = 0.0;
      this.maxScore = 0.0;
    }
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
   * 
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
   * 
   * @param match   name, specifically identifying this match.
   * @param mininum score that this node can return (must be between 0.0 and 1.0
   *                inclusively)
   * @param maximum score that this node can return (must be between 0.0 and 1.0
   *                inclusively and not less than mininum score)
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
    if (minScore == 0.0 && maxScore == 0.0) {
      enabled = false;
    }
  }

  /**
   * @return unique and human readable name for this match node
   */
  public String getMatchName() {
    return matchName;
  }

  public void setMatchName(String matchName) {
    this.matchName = matchName;
  }

  /**
   * Scores the match between two patients for this match node. The value
   * returned here is the unweighted score and will vary between 0.0 and
   * 1.0. At this point the min and max score have not been used
   * to adjust the score.
   * 
   * @param patientA the target of the comparison
   * @param patientB the other patient to compare to the target for scoring
   * @return a score between 0.0 and 1.0 inclusively indicating the if this node
   *         matches.
   */
  public double score(Patient patientA, Patient patientB) {
    return 0.0;
  }

  /**
   * This is a convenience method for scoring two patients that are included in a
   * PatientCompare object.
   * 
   * @param patientCompare the set of patients that need to be scored
   * @return a score between 0.0 and 1.0 inclusively indicating if this node
   *         matches.
   */
  public double score(PatientCompare patientCompare) {
    return score(patientCompare.getPatientA(), patientCompare.getPatientB());
  }

  /**
   * Scores the match between two patients for this match node. The value
   * returned here is the weight score and will be between the minScore
   * and maxScore for this node.
   * 
   * @param patientCompare the two patients to be compared
   * @return a score between the minScore and maxScore inclusively indicating if
   *         this node matches.
   */
  public double weightScore(PatientCompare patientCompare) {
    return weightScore(patientCompare.getPatientA(), patientCompare.getPatientB());
  }

  /**
   * Scores a match between patient A and patient B and returns
   * final weight score between minScore and maxScore for this node.
   * 
   * @param patientA the target of the comparison
   * @param patientB the other patient to compare to the target for scoring
   * @return a score between minScore and maxScore inclusively indicating the if
   *         this node matches.
   */
  public double weightScore(Patient patientA, Patient patientB) {
    return score(patientA, patientB) * (maxScore - minScore) + minScore;
  }

  public double weightScoreFromSignaturer() {
    return scoreFromSignature * (maxScore - minScore) + minScore;

  }

  /**
   * Indicates if the node has a signal, which means the weightScore
   * for this node is 0.5 or greater.
   * 
   * @param patientCompare the two patients to be compared
   * @return <code>true</code> if this node signals that condition exists.
   */
  public boolean hasSignal(PatientCompare patientCompare) {
    return hasSignal(patientCompare.getPatientA(), patientCompare.getPatientB());
  }

  /**
   * Indicates if the node has a signal, which means the weightScore
   * for this node is 0.5 or greater.
   * 
   * @param patientA the target of the comparison
   * @param patientB the other patient to compare to the target for scoring
   * @return <code>true</code> if this node signals that condition exists.
   */
  public boolean hasSignal(Patient patientA, Patient patientB) {
    return weightScore(patientA, patientB) >= 0.5;
  }

  /**
   * Used by sub class implementations to easily indicate a value for regular and
   * not match nodes. This allows
   * for simple development of a single match node that may be used in normal form
   * or in NOT form. This method
   * takes a boolean positive assert that if true indicates that the condition
   * exists and a value to assign
   * if so. Otherwise, if the positive assert is false then 1.0 - value is
   * assigned as the value. In addition,
   * if this is a NOT node then the value is again inverted before returning.
   * 
   * @param positiveAssert indicates if the condition exists for the node to be
   *                       true
   * @param value          the value to assign when the condition is true
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
   * Used by the sub class implementations to quickly change positive match scores
   * into the reverse
   * NOT scores if this node is a NOT node. The score value is the score that
   * would be given if this
   * was a normal node. This value is returned unchanged for normal nodes, and
   * flipped (1.0 - value)
   * for NOT nodes.
   * 
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
   * Returns human readable description of how these two values are being compared
   * by this
   * match node. This is used by optimization user interface to allow super users
   * to inspect
   * how the matching algorithm is working.
   * 
   * @param patientA the target of the comparison
   * @param patientB the other patient to compare to the target for scoring
   * @return a human readable description of what match is being done (e.g.
   *         'Henry' == 'Henry')
   */
  public abstract String getDescription(Patient patientA, Patient patientB);

  protected static Random random = new Random();

  /**
   * Randomizes the minScore and maxScore to a set of values between 0.0 and 1.0
   * inclusively where
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
   * Supports a quick check to find out of the potential mate has the same DNA as
   * this one. There is
   * no benefit to mating the same creature to another as the result is the
   * exactly the same as
   * both parents. This check is to prevent that from happening.
   * 
   * @param potentialMate MatchNode that may be mated with this match node.
   * @return
   */
  public boolean isSame(MatchNode potentialMate) {
    return potentialMate.minScore == this.minScore && potentialMate.maxScore == this.maxScore
        && potentialMate.enabled == this.enabled;
  }

  /**
   * In biology random mutations sometimes occur. Not sure how useful this is in
   * biology or here as most
   * variety comes from the recombination of different values. But it's here
   * anyways. The logic
   * is built so that initially mutations occur more often and then taper off as
   * the generations increase.
   * Mutation occurs randomly 1 out of (generation + 3) times.
   * 
   * @param generation
   */
  public void mutate(int generation) {
    if (random.nextInt(generation + 3) == 1) {
      makeRandom();
    }
  }

  /**
   * Method adds variation to creature by "tweaking" the weight +/-0.10. This
   * method was chosen because
   * the optimal solution is likely to be close to solutions we have already
   * selected as good. This
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
   * 
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
   * Creates an exact copy of another clone. Sets the minScore, maxScore, and
   * enabled flag.
   * 
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

  public String makeSetupYml() {
    return getMatchNameFull() + ": " + isEnabled();
  }

}
