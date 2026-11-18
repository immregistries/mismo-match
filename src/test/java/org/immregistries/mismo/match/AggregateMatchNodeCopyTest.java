package org.immregistries.mismo.match;

import static org.junit.Assert.*;

import java.util.List;
import org.immregistries.mismo.match.matchers.AggregateMatchNode;
import org.immregistries.mismo.match.matchers.ExactMatchNode;
import org.immregistries.mismo.match.matchers.MatchNode;
import org.junit.Test;

public class AggregateMatchNodeCopyTest {
  private static AggregateMatchNode tree() {
    AggregateMatchNode root = new AggregateMatchNode("Root", 0.1, 0.9);
    AggregateMatchNode group = new AggregateMatchNode("Group", 0.2, 0.8);
    group.add(new ExactMatchNode("First", 0.3, 0.7, "nameFirst"));
    group.add(new ExactMatchNode("DisabledLeaf", 0.0, 0.0, "nameLast"));
    root.add(group);
    AggregateMatchNode disabled = new AggregateMatchNode("DisabledGroup", 0.0, 0.0);
    disabled.add(new ExactMatchNode("Inner", 0.4, 0.6, "nameMiddle"));
    root.add(disabled);
    return root;
  }

  private static void assertCopy(MatchNode source, MatchNode target) {
    assertNotSame(source, target);
    assertEquals(source.getMinScore(), target.getMinScore(), 0.0);
    assertEquals(source.getMaxScore(), target.getMaxScore(), 0.0);
    assertEquals(source.isEnabled(), target.isEnabled());
    if (source instanceof AggregateMatchNode) {
      List<MatchNode> a = ((AggregateMatchNode) source).getMatchNodeList();
      List<MatchNode> b = ((AggregateMatchNode) target).getMatchNodeList();
      assertEquals(a.size(), b.size());
      for (int i = 0; i < a.size(); i++) assertCopy(a.get(i), b.get(i));
    }
  }

  @Test
  public void copiesNestedValuesAndDisabledScopeWithoutAliasing() {
    AggregateMatchNode source = tree();
    AggregateMatchNode target = tree();
    target.makeRandom();
    target.clone(source);
    assertCopy(source, target);
    assertTrue(source.isSame(target));
    target.getMatchNodeList().get(0).setMinScore(0.123);
    assertEquals(0.2, source.getMatchNodeList().get(0).getMinScore(), 0.0);
    assertFalse(source.isSame(target));
  }

  @Test
  public void equalityChecksLeavesInsideDisabledGroupsAndEnabledFlags() {
    AggregateMatchNode a = tree();
    AggregateMatchNode b = tree();
    assertTrue(a.isSame(b));
    MatchNode leaf = ((AggregateMatchNode) b.getMatchNodeList().get(1)).getMatchNodeList().get(0);
    leaf.setMaxScore(0.65);
    assertFalse(a.isSame(b));
    b = tree();
    ((AggregateMatchNode) b.getMatchNodeList().get(0)).getMatchNodeList().get(1).setEnabled(true);
    assertFalse(a.isSame(b));
    assertFalse(a.isSame((AggregateMatchNode) null));
  }

  @Test
  public void incompatibleTreeIsRejectedBeforePartialCopy() {
    AggregateMatchNode source = tree();
    AggregateMatchNode target = tree();
    target.setMinScore(0.05);
    ((AggregateMatchNode) target.getMatchNodeList().get(1)).getMatchNodeList().clear();
    assertFalse(source.isSame(target));
    try {
      target.clone(source);
      fail("Expected incompatible tree rejection");
    } catch (IllegalArgumentException expected) {
      assertEquals(0.05, target.getMinScore(), 0.0);
    }
  }

  @Test
  public void childTypeAndOrderMismatchAreNotEqual() {
    AggregateMatchNode source = tree();
    AggregateMatchNode target = tree();
    target.getMatchNodeList().set(0, new ExactMatchNode("Other", 0.2, 0.8, "nameFirst"));
    assertFalse(source.isSame(target));
    try {
      target.clone(source);
      fail("Expected incompatible node type rejection");
    } catch (IllegalArgumentException expected) {
      assertTrue(expected.getMessage().contains("incompatible"));
    }
  }
}
