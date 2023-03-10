package org.immregistries.mismo.match.model;

import java.io.Serializable;
import java.util.Date;

public class MatchSet implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -1924139808210439694L;

  private int matchSetId = 0;
  private String label = "";
  private Date updateDate = null;

  public int getMatchSetId() {
    return matchSetId;
  }

  public void setMatchSetId(int matchSetId) {
    this.matchSetId = matchSetId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MatchSet && this.getMatchSetId() > 0) {
      return ((MatchSet) obj).getMatchSetId() == this.getMatchSetId();
    }
    return super.equals(obj);
  }
}
