package org.rapidpm.vaadin.server.bootstrap;

import org.rapidpm.microservice.Main;
import org.rapidpm.microservice.MainUndertow;

import java.util.Optional;

/**
 *
 */
public class TrainerStartupAction implements Main.MainStartupAction {
  @Override
  public void execute(Optional<String[]> args) {
    System.setProperty(MainUndertow.SHIRO_ACTIVE_PROPERTY, "true");
  }
}
