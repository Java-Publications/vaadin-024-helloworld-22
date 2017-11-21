package org.rapidpm.vaadin.trainer.backend.security.user;

import java.util.Optional;

import org.rapidpm.vaadin.trainer.api.security.user.User;
import org.rapidpm.vaadin.trainer.api.security.user.UserService;

/**
 *
 */
public class UserServiceInMemory implements UserService {

  //How to hold in sync with Shiro.ini ??? next part
  @Override
  public Optional<User> loadUser(String login) {
    if (login.equals("admin")) return Optional.of(new User("admin" , "Admin" , "Secure"));
    if (login.equals("max")) return Optional.of(new User("max" , "Max" , "Rimkus"));
    if (login.equals("sven")) return Optional.of(new User("sven" , "Sven" , "Ruppert"));
    return Optional.empty();
  }

}
