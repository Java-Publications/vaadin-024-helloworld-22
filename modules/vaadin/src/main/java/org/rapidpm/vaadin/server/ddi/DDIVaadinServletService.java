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

import javax.annotation.PostConstruct;

import org.rapidpm.ddi.DI;
import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.UI;


public class DDIVaadinServletService extends VaadinServletService {


  public DDIVaadinServletService(VaadinServlet servlet ,
                                 DeploymentConfiguration deploymentConfiguration ,
                                 List<String> topLevelPackagesToActivated)
      throws ServiceException {

    super(servlet , deploymentConfiguration);

    topLevelPackagesToActivated
        .stream()
        .filter(pkg -> ! DI.isPkgPrefixActivated(pkg))
        .forEach(DI::activatePackages);

    addSessionInitListener(event -> event.getSession().addUIProvider(new DefaultUIProvider() {
      @Override
      public UI createInstance(final UICreateEvent event) {
        return DI.activateDI(event.getUIClass());
      }
    }));

    addSessionDestroyListener(event -> { });
  }


  @Override
  public void handleRequest(VaadinRequest request , VaadinResponse response) throws ServiceException {
    super.handleRequest(request , response);
  }

  @PostConstruct
  public void initialize() {
  }
}
