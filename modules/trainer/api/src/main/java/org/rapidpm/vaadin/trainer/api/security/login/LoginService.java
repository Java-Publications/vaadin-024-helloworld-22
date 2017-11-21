package org.rapidpm.vaadin.trainer.api.security.login;

/**
 *
 */
public interface LoginService {
  boolean check(String login, String password);
}
