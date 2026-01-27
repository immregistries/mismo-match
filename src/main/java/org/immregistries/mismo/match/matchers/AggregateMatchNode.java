package org.immregistries.mismo.match.matchers;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.immregistries.mismo.match.StringUtils;
import org.immregistries.mismo.match.model.Patient;

/**
 * This is a special sub class of MatchNode that is actually a collection
 * of other MatchNodes. It has all the same methods as a regular
 * MatchNode but these aggregate the actions of all the subordinate
 * MatchNodes.
 * 
 * @author Nathan Bunker
 *
 */
public class AggregateMatchNode extends MatchNode {
  private List<MatchNode> matchNodeList = new ArrayList<MatchNode>();
  private int level = 1;

  protected int getLevel() {
    return level;
  }

  public AggregateMatchNode(String fieldName, double minScore, double maxScore) {
    super(fieldName, minScore, maxScore);
    setMatchNameFull(fieldName);
  }

  public void disableMatchNodes(Map<String, Boolean> matchNodeMap) {
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matchNode;
        agg.disableMatchNodes(matchNodeMap);
      } else {
        matchNodeMap.get(matchNode.getMatchNameFull());
        Boolean enabled = matchNodeMap.get(matchNode.getMatchNameFull());
        boolean disabled = true;
        if (enabled != null) {
          disabled = !enabled;
        }
        if (disabled) {
          matchNode.setEnabled(false);
        }
      }
    }
  }

  public void populateFieldSet(Set<String> patientFieldSet) {
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode.isEnabled()) {
        if (matchNode instanceof AggregateMatchNode) {
          AggregateMatchNode agg = (AggregateMatchNode) matchNode;
          agg.populateFieldSet(patientFieldSet);
        } else {
          if (StringUtils.isNotEmpty(matchNode.getFieldName())) {
            patientFieldSet.add(matchNode.getFieldName());
          }
          if (StringUtils.isNotEmpty(matchNode.getFieldName2())) {
            patientFieldSet.add(matchNode.getFieldName2());
          }
          if (StringUtils.isNotEmpty(matchNode.getFieldName3())) {
            patientFieldSet.add(matchNode.getFieldName3());
          }
          if (StringUtils.isNotEmpty(matchNode.getFieldNameOther())) {
            patientFieldSet.add(matchNode.getFieldNameOther());
          }
        }
      }
    }
  }

  public List<MatchNode> getMatchNodeList() {
    return matchNodeList;
  }

  public MatchNode add(MatchNode matchNode) {
    matchNodeList.add(matchNode);
    matchNode.setMatchNameFull(this.getMatchNameFull() + "." + matchNode.getMatchName());
    return matchNode;
  }

  public AggregateMatchNode add(AggregateMatchNode matchNode) {
    if (level <= matchNode.getLevel()) {
      level = matchNode.getLevel() + 1;
    }
    matchNodeList.add(matchNode);
    matchNode.setMatchNameFull(this.getMatchNameFull() + "." + matchNode.getMatchName());
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

  public String makeSetupYml() {
    StringBuffer sbuf = new StringBuffer();
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode instanceof AggregateMatchNode) {
        AggregateMatchNode agg = (AggregateMatchNode) matchNode;
        sbuf.append(agg.makeSetupYml());
      } else {
        sbuf.append("  " + matchNode.makeSetupYml() + "\n");
      }
    }

    return sbuf.toString();
  }

  public void populateMatchNodeListAndScoreMap(Patient patientA, Patient patientB, List<MatchNode> matchNodeList,
      Map<MatchNode, Double> scoreMap) {
    for (MatchNode matchNode : this.matchNodeList) {
      if (matchNode.isEnabled()) {
        if (matchNode instanceof AggregateMatchNode) {
          AggregateMatchNode agg = (AggregateMatchNode) matchNode;
          agg.populateMatchNodeListAndScoreMap(patientA, patientB, matchNodeList, scoreMap);
        } else {
          matchNodeList.add(matchNode);
          scoreMap.put(matchNode, matchNode.score(patientA, patientB));
        }
      }
    }
  }

  public void populateScoreList(Patient patientA, Patient patientB, List<Double> scoreList) {
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode.isEnabled()) {
        if (matchNode instanceof AggregateMatchNode) {
          AggregateMatchNode agg = (AggregateMatchNode) matchNode;
          agg.populateScoreList(patientA, patientB, scoreList);
        } else {
          scoreList.add(matchNode.score(patientA, patientB));
        }
      }
    }
  }

  public void populateScoreFromSignature(Deque<Double> scoreFromSignatureDequeue) {
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode.isEnabled()) {
        if (matchNode instanceof AggregateMatchNode) {
          AggregateMatchNode agg = (AggregateMatchNode) matchNode;
          agg.populateScoreFromSignature(scoreFromSignatureDequeue);
        } else {
          if (!scoreFromSignatureDequeue.isEmpty()) {
            matchNode.setScoreFromSignature(scoreFromSignatureDequeue.removeFirst());
          }
        }
      }
    }
  }

  public void printOut(Patient patientA, Patient patientB, String pad) {
//    System.out.println(pad + matchName + ": " + score(patientA, patientB));
    for (MatchNode matchNode : matchNodeList) {
      if (matchNode.isEnabled()) {
        if (matchNode instanceof AggregateMatchNode) {
          AggregateMatchNode agg = (AggregateMatchNode) matchNode;
          agg.printOut(patientA, patientB, pad + "  ");
        } else {
          matchNode.printOut(patientA, patientB, pad + "  ");
        }
      }
    }
  }

}
