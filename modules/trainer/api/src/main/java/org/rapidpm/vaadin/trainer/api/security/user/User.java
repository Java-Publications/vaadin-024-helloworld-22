package org.rapidpm.vaadin.trainer.api.security.user;

import org.rapidpm.frp.model.serial.Tripel;

/**
 *
 */
public class User extends Tripel<String, String, String> {

  private String login;
  private String foreName;
  private String familyName;

  public User(String s , String s2 , String s3) {
    super(s , s2 , s3);
  }


  public String login() {
    return getT1();
  }

  public String foreName() {
    return getT2();
  }

  public String familyName() {
    return getT3();
  }
}
