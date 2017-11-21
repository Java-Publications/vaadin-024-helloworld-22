package dev.org.rapidpm.vaadin;

import com.vaadin.ui.*;
import org.rapidpm.ddi.DI;
import org.rapidpm.dependencies.core.logger.Logger;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.model.Result;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.System.getProperty;
import static org.rapidpm.ddi.DI.activateDI;
import static org.rapidpm.frp.model.Result.ofNullable;

/**
 *
 */
public class DevComponent extends Composite {

  public static final String SELECTED_CLASS  = "selected.class";
  public static final int    REFRESH_RATE_MS = 1_000;

  private final VerticalLayout  mainLayout         = new VerticalLayout();
  private final Panel           testComponentPanel = new Panel("Component Test Area");
  private final ComboBox<Class> classComboBox      = new ComboBox<>();
  private final Button          refresh            = new Button("refresh");

  private final CheckBox autoRefreshComponent = new CheckBox("autorefresh [" + (REFRESH_RATE_MS / 1_000) + "s]");
  private final Timer    timer                = new Timer(false);

  private Result<UI> ui = ofNullable(getUI(), "ui not init until now");

  private Consumer<Class<?>> refreshFkt() {
    return (clazz) -> ui
        .ifPresentOrElse(
            uiPresent -> switchFkt().andThen(uiPresent::access).apply(clazz),
            uiMissing -> Logger.getLogger(DevComponent.class).info("UI not present..")
        );
  }

  private Function<Class, Runnable> switchFkt() {
    return (clazz) ->
        () -> ((CheckedFunction<Class<?>, Component>)
            aClass -> (Component) activateDI(aClass))
            .apply(clazz)
            .ifPresentOrElse(
                success -> {
                  success.setSizeFull();
                  testComponentPanel.setContent(success);
                  ui.get().push();
                },
                Logger.getLogger(clazz)::warning
            );
  }


  public DevComponent() {
    classComboBox.setWidth(100, Unit.PERCENTAGE);
    classComboBox.addSelectionListener(event -> event.getSelectedItem()
                                                     .ifPresent(refreshFkt()::accept));

    refresh.setWidth(100, Unit.PERCENTAGE);

    refresh.addClickListener(e -> classComboBox
        .getSelectedItem()
        .ifPresent(refreshFkt()::accept));

    testComponentPanel.setSizeFull();


    mainLayout.addComponents(classComboBox,
                             autoRefreshComponent,
                             refresh,
                             testComponentPanel
    );
  }


  @Override
  public void attach() {
    super.attach();
    ui = Result.success(getUI());
  }

  @PostConstruct
  private void postConstruct() {
    setCompositionRoot(mainLayout);

    final List<Class> classStream = DI.getSubTypesWithoutInterfacesAndGeneratedOf(Composite.class)
                                      .stream()
                                      .filter(aClass -> aClass.getName().contains("org.rapidpm.vaadin"))
                                      .filter(aClass -> !aClass.getName().contains(this.getClass().getPackage().getName()))
                                      .collect(Collectors.toList());

    classComboBox.setItems(classStream);

    // explain in blog
    ((CheckedFunction<String, Class>) Class::forName)
        .apply(getProperty(SELECTED_CLASS))
        .ifPresent(classComboBox::setValue);


    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        Logger.getLogger(DevComponent.class).info("Timer Task is running..");
        ui.ifPresentOrElse(
            yes -> yes.access(() -> {
              if (autoRefreshComponent.getValue()) {
                classComboBox
                    .getSelectedItem()
                    .ifPresent(refreshFkt()::accept);
              }
            }),
            no -> Logger.getLogger(DevComponent.class).info("UI not present..")
        );
      }
    }, REFRESH_RATE_MS, REFRESH_RATE_MS);

  }
}
