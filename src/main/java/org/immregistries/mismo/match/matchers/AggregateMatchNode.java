package org.immregistries.mismo.match.matchers;

import java.util.ArrayList;
import java.util.List;
import org.immregistries.mismo.match.model.Patient;

/**
 * This is a special sub class of MatchNode that is actually a collection
 * of other MatchNodes. It has all the same methods as a regular 
 * MatchNode but these aggregate the actions of all the subordinate
 * MatchNodes. 
 * @author Nathan Bunker
 *
 */
public class AggregateMatchNode extends MatchNode {
  private List<MatchNode> matchNodeList = new ArrayList<MatchNode>();
  private int level = 1;

  protected int getLevel() {
    return level;
  }

  public String makeScript() {
    StringBuilder sb = new StringBuilder(super.makeBasicScript());
    for (MatchNode matcher : matchNodeList) {
      sb.append("{");
      if (matcher instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matcher;
        sb.append(agg.makeScript());
      } else {
        sb.append(matcher.makeBasicScript());
      }
      sb.append("}");
    }
    return sb.toString();
  }

  public void readScript(String script) {
    readScript(script, 0);
  }

  public int readScript(String script, int pos) {
    pos = super.readBasicScript(script, pos);
    while (pos < script.length()) {
      char nextChar = script.charAt(pos);
      if (nextChar == '{') {
        pos++;
        int posColon = script.indexOf(':', pos);
        if (posColon != -1) {
          MatchNode selectedMatchNode = null;
          String matchName = script.substring(pos, posColon);
          for (MatchNode matchNode : matchNodeList) {
            if (matchNode.getMatchName().equals(matchName)) {
              selectedMatchNode = matchNode;
              break;
            }
          }
          if (selectedMatchNode != null) {
            if (selectedMatchNode instanceof AggregateMatchNode) {
              AggregateMatchNode agg = (AggregateMatchNode) selectedMatchNode;
              pos = agg.readScript(script, pos);
            } else {
              pos = selectedMatchNode.readScript(script, pos);
              int openBrace = 1;
              while (openBrace > 0 && pos < script.length()) {
                char c = script.charAt(pos);
                if (c == '}') {
                  openBrace--;
                } else if (c == '{') {
                  openBrace++;
                }
                pos++;
              }
            }
          }
        }
      } else if (nextChar == '}') {
        pos++;
        return pos;
      } else {
        pos++;
      }
    }
    return pos;
  }

  public AggregateMatchNode(String fieldName, double minScore, double maxScore) {
    super(fieldName, minScore, maxScore);
  }

  public List<MatchNode> getMatchNodeList() {
    return matchNodeList;
  }

  public MatchNode add(MatchNode matchNode) {
    matchNodeList.add(matchNode);
    return matchNode;
  }

  public AggregateMatchNode add(AggregateMatchNode matchNode) {
    if (level <= matchNode.getLevel()) {
      level = matchNode.getLevel() + 1;
    }
    matchNodeList.add(matchNode);
    return matchNode;

  }

  @Override
  public int hashCode() {
    String hash = "";
    for (MatchNode matcher : matchNodeList) {
      if (matcher instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matcher;
        hash += agg.hashCode();
      } else {
        hash += matcher.hashCode();
      }
    }
    return (hash + super.hashCode()).hashCode();
  }

  @Override
  public String getSignature(Patient patientA, Patient patientB) {
    String signature = "(";
    for (MatchNode matcher : matchNodeList) {
      if (matcher instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matcher;
        signature += agg.getSignature(patientA, patientB);
      } else {
        signature += matcher.getSignature(patientA, patientB);
      }
    }
    return signature + ")";
  }

  public String getSignature(Patient patientA, Patient patientB, int level) {
    if (level == 0) {
      return getSignature(patientA, patientB);
    }
    if (this.level <= level) {
      double score = score(patientA, patientB);
      if (score >= 0.9) {
        return "A";
      }
      if (score >= 0.7) {
        return "B";
      }
      if (score >= 0.5) {
        return "C";
      }
      if (score >= 0.3) {
        return "D";
      }
      return "E";
    } else {
      String signature = "";
      for (MatchNode matcher : matchNodeList) {
        if (matcher instanceof AggregateMatchNode) {
          AggregateMatchNode agg = (AggregateMatchNode) matcher;
          signature += agg.getSignature(patientA, patientB, level);
        }
      }
      return signature;
    }
  }

  @Override
  public double score(Patient patientA, Patient patientB) {
    double score = 0.0;
    for (MatchNode matcher : matchNodeList) {
      if (matcher instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matcher;
        if (agg.isEnabled()) {
          score += agg.weightScore(patientA, patientB);
        }
      } else {
        if (matcher.isEnabled()) {
          score += matcher.weightScore(patientA, patientB);
        }
      }
    }
    if (score > 1.0) {
      score = 1.0;
    }
    if (score < 0.5) {
      score = 0.0;
    }
    return score;
  }

  @Override
  public String getDescription(Patient patientA, Patient patientB) {
    return "";
  }

  public void makeRandom() {
    super.makeRandom();
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matchNode;
        agg.makeRandom();
      } else {
        matchNode.makeRandom();
      }
    }
  }

  public void mate(AggregateMatchNode parentA, AggregateMatchNode parentB) {
    super.mate(parentA, parentB);
    List<MatchNode> matchNodeListA = parentA.getMatchNodeList();
    List<MatchNode> matchNodeListB = parentB.getMatchNodeList();
    for (int i = 0; i < matchNodeList.size(); i++) {
      MatchNode matchNode = matchNodeList.get(i);
      MatchNode matchNodeA = matchNodeListA.get(i);
      MatchNode matchNodeB = matchNodeListB.get(i);
      if (matchNode instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matchNode;
        AggregateMatchNode aggA = (AggregateMatchNode) matchNodeA;
        AggregateMatchNode aggB = (AggregateMatchNode) matchNodeB;
        agg.mate(aggA, aggB);
      } else {
        matchNode.mate(matchNodeA, matchNodeB);
      }
    }
  }

  public void clone(AggregateMatchNode clone) {
    super.clone(clone);
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matchNode;
        agg.clone(clone);
      } else {
        matchNode.clone(clone);
      }
    }
  }

  public boolean isSame(AggregateMatchNode potentialMate) {
    if (!super.isSame(potentialMate)) {
      return false;
    }
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matchNode;
        if (!agg.isSame(potentialMate)) {
          return false;
        }
      }
      if (!matchNode.isSame(potentialMate)) {
        return false;
      }
    }
    return true;
  }

  public void mutate(int generation) {
    super.mutate(generation);
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matchNode;
        agg.mutate(generation);
      } else {
        matchNode.mutate(generation);
      }
    }
  }

  public void tweak() {
    super.tweak();
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matchNode;
        agg.tweak();
      } else {
        matchNode.tweak();
      }
    }
  }

  @Override
  public String toString() {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append("[");
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matchNode;
        sbuf.append(agg.toString());
      } else {
        sbuf.append(matchNode.toString());
      }
    }
    sbuf.append("]");

    return sbuf.toString();
  }

}
