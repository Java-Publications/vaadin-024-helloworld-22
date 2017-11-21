package org.rapidpm.vaadin.server.api;

import org.rapidpm.vaadin.trainer.api.security.user.User;
import com.vaadin.server.VaadinSession;

/**
 *
 */
public class SessionServiceImpl implements SessionService {
  @Override
  public boolean isUserPresent() {
    return
        (VaadinSession
             .getCurrent()
             .getAttribute(User.class) != null);
  }
}
