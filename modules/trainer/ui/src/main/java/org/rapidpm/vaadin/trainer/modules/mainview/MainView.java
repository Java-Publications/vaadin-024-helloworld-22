package org.rapidpm.vaadin.trainer.modules.mainview;

import static com.vaadin.ui.themes.ValoTheme.MENU_ROOT;
import static org.rapidpm.ddi.DI.activateDI;
import static org.rapidpm.vaadin.ComponentIDGenerator.cssLayoutID;
import static org.rapidpm.vaadin.ComponentIDGenerator.horizontalLayoutID;
import static org.rapidpm.vaadin.ComponentIDGenerator.verticalLayoutID;
import static org.rapidpm.vaadin.trainer.modules.MainViewFunction.inject;

import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.rapidpm.ddi.DI;
import org.rapidpm.vaadin.trainer.api.property.PropertyService;
import org.rapidpm.vaadin.trainer.modules.mainview.dashboard.DashboardComponent;
import org.rapidpm.vaadin.trainer.modules.mainview.menu.MenuComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
public class MainView extends Composite {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainView.class);

  public static final String CONTENT_LAYOUT = "mainview.contentlayout";
  public static final String MENU_LAYOUT = "mainview.menulayout";
  public static final String MAIN_LAYOUT = "mainview.mainlayout";

  @Inject private PropertyService propertyService;

  private String property(String key) {
    return propertyService.resolve(key);
  }


  final CssLayout contentLayout = new CssLayout();
  final VerticalLayout menuLayout = new VerticalLayout();
  final HorizontalLayout mainLayout = new HorizontalLayout(menuLayout, contentLayout);

  @PostConstruct
  public void postConstruct() {
    contentLayout.setId(cssLayoutID().apply(MainView.class , CONTENT_LAYOUT));
    contentLayout.addComponent(activateDI(DashboardComponent.class));
    contentLayout.setSizeFull();


    menuLayout.setId(verticalLayoutID().apply(MainView.class , MENU_LAYOUT));
    menuLayout.setStyleName(MENU_ROOT);
    menuLayout.setWidth(100 , Sizeable.Unit.PERCENTAGE);
    menuLayout.setHeight(100 , Sizeable.Unit.PERCENTAGE);
    menuLayout.setSizeFull();
    menuLayout.addComponent(activateDI(new MenuComponent(contentLayout)));


    mainLayout.setId(horizontalLayoutID().apply(MainView.class , MAIN_LAYOUT));
    mainLayout.setSizeFull();
    mainLayout.setExpandRatio(menuLayout , 0.20f);
    mainLayout.setExpandRatio(contentLayout , 0.80f);

    setCompositionRoot(mainLayout);
  }

}
