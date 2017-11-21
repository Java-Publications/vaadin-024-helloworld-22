package org.rapidpm.vaadin.trainer.modules.mainview.dashboard;

import com.vaadin.ui.*;
import org.rapidpm.vaadin.trainer.api.property.PropertyService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 */
public class DashboardComponent extends Composite {

  private @Inject PropertyService propertyService;


  public static final String CLICK_ME = "dashboard.component.btn.clickme";
  public static final String LABEL    = "dashboard.component.btn.label";

  private final Layout layout  = new VerticalLayout();
  private final Label  label   = new Label();
  private final Button clickMe = new Button();


  public DashboardComponent() {
    layout.addComponents(label, clickMe);
    setCompositionRoot(layout);
  }

  @PostConstruct
  private void postConstruct() {
    label.setCaption(propertyService.resolve(LABEL));
    clickMe.setCaption(propertyService.resolve(CLICK_ME));

    clickMe.addClickListener(e -> Notification.show("and here we are.. "));

  }
}