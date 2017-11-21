package org.rapidpm.vaadin.server.api;

import org.apache.shiro.SecurityUtils;

/**
 *
 */
public class SecurityServiceImpl implements SecurityService {
  @Override
  public boolean isRemembered() {
    return SecurityUtils.getSubject().isRemembered();
  }
}

