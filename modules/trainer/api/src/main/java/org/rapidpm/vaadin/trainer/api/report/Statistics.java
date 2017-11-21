package org.rapidpm.vaadin.trainer.api.report;

import org.rapidpm.frp.model.serial.Quad;

import java.time.LocalDateTime;

/**
 *
 */
public class Statistics extends Quad<Long, String, String, LocalDateTime> {
  public Statistics(Long id, String foreName, String familyName, LocalDateTime lastLogin) {
    super(id, foreName, familyName, lastLogin);
  }

  public Long id() {return getT1();}

  public String foreName() {return getT2();}

  public String familyName() {return getT3();}

  public LocalDateTime lastLogin() {return getT4();}
}
