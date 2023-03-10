package org.immregistries.mismo.match.model;

import java.io.Serializable;

public class User implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 8622635977449272576L;
  
  private int userId = 0;
  private String name = "";
  private String email = "";
  private String password = "";

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
