package org.rapidpm.vaadin.trainer.api.security.user;

import java.util.Optional;

/**
 *
 */
public interface UserService {
  Optional<User> loadUser(String login);
}
