package org.rapidpm.microservice.startup;

import org.rapidpm.microservice.Main;
import org.rapidpm.microservice.MainUndertow;

import java.util.Optional;

import static java.lang.System.setProperty;
import static org.rapidpm.microservice.MainUndertow.SHIRO_ACTIVE_PROPERTY;

/**
 *
 */
public class VaadinStartupAction implements Main.MainStartupAction {
  @Override
  public void execute(Optional<String[]> args) {
    setProperty(SHIRO_ACTIVE_PROPERTY, "true");
  }
}
