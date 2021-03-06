/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.rapidpm.vaadin.server.ddi;

import java.util.List;

import javax.servlet.ServletException;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

public abstract class DDIVaadinServlet extends VaadinServlet {

  @Override
  protected void servletInitialized() throws ServletException {
    super.servletInitialized();

  }

  //add Metrics here

  @Override
  protected VaadinServletService createServletService(final DeploymentConfiguration deploymentConfiguration) throws ServiceException {
    final DDIVaadinServletService service = new DDIVaadinServletService(this, deploymentConfiguration, topLevelPackagesToActivate());
    service.init();
    return service;
  }

  /**
   * return a list of pkg names that are available for Injection
   *
   * @return return
   */
  public abstract List<String> topLevelPackagesToActivate();

}
