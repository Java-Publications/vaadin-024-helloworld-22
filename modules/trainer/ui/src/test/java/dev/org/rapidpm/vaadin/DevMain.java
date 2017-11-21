package dev.org.rapidpm.vaadin;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import org.rapidpm.ddi.DI;
import org.rapidpm.ddi.ResponsibleFor;
import org.rapidpm.ddi.implresolver.ClassResolver;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.dependencies.core.logger.Logger;
import org.rapidpm.dependencies.core.logger.LoggingService;
import org.rapidpm.microservice.Main;
import org.rapidpm.vaadin.server.ui.JumpstartUIComponentFactory;

import javax.inject.Inject;

/**
 *
 */
public class DevMain implements HasLogger {

  public static void main(String[] args) {
    DI.activatePackages("org.rapidpm");
    DI.activatePackages("dev.org.rapidpm");

    final LoggingService logger = Logger.getLogger(DevMain.class);
    logger.warning("Starting the Vaadin Component Testing App");
    Main.deploy();
  }


  @ResponsibleFor(JumpstartUIComponentFactory.class)
  public static class DevClassResolver implements ClassResolver<JumpstartUIComponentFactory> {
    @Override
    public Class<? extends JumpstartUIComponentFactory> resolve(Class<JumpstartUIComponentFactory> interf) {
      return DevUIComponentFactory.class;
    }
  }

  public static class DevUIComponentFactory implements JumpstartUIComponentFactory {

    private @Inject DevComponent component;

    @Override
    public Component createComponentToSetAsContent(VaadinRequest vaadinRequest) {
      return component;
    }
  }
}
