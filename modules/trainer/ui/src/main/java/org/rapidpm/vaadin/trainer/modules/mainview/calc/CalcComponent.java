package org.rapidpm.vaadin.trainer.modules.mainview.calc;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.*;
import com.vaadin.ui.Label;
import org.rapidpm.frp.model.serial.Pair;
import org.rapidpm.vaadin.trainer.api.property.PropertyService;
import org.rapidpm.vaadin.trainer.modules.mainview.calc.basics.CalcBasicsService.BasicArithmeticTask;
import org.rapidpm.vaadin.trainer.modules.mainview.calc.basics.CalcBasicsService.Operator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.vaadin.icons.VaadinIcons.*;
import static com.vaadin.shared.ui.ContentMode.HTML;
import static java.lang.String.valueOf;
import static org.rapidpm.vaadin.ComponentIDGenerator.*;
import static org.rapidpm.vaadin.trainer.modules.mainview.calc.basics.CalcBasicsService.nextRndNoDivTask;
import static org.rapidpm.vaadin.trainer.modules.mainview.calc.basics.CalcBasicsService.nextTask;

/**
 *
 */
public class CalcComponent extends Composite {

  public static final String BTN_NEXT          = "calc.component.btn.next";
  public static final String BTN_OK            = "calc.component.btn.ok";
  public static final String TF_RESULT_MACHINE = "calc.component.tf.result.machine";
  public static final String TF_RESULT_USER    = "calc.component.tf.result.user";
  public static final String TF_VALUE_ONE      = "calc.component.tf.value.one";
  public static final String TF_VALUE_TWO      = "calc.component.tf.value.two";
  public static final String LABEL_OPERATOR    = "calc.component.lbl.operator";

  public static final String CALC_COMPONENT_CHART_RESULT         = "calc.component.chart.result";
  public static final String CALC_COMPONENT_CHART_RESULT_TITLE_X = "calc.component.chart.result.title.x";
  public static final String CALC_COMPONENT_CHART_RESULT_TITLE_Y = "calc.component.chart.result.title.y";


  public static final String BTN_NEXT_ID          = buttonID().apply(CalcComponent.class, BTN_NEXT);
  public static final String BTN_CALC_ID          = buttonID().apply(CalcComponent.class, BTN_OK);
  public static final String TF_RESULT_MACHINE_ID = textfieldID().apply(CalcComponent.class, TF_RESULT_MACHINE);
  public static final String TF_RESULT_USER_ID    = textfieldID().apply(CalcComponent.class, TF_RESULT_USER);
  public static final String TF_VALUE_ONE_ID      = textfieldID().apply(CalcComponent.class, TF_VALUE_ONE);
  public static final String TF_VALUE_TWO_ID      = textfieldID().apply(CalcComponent.class, TF_VALUE_TWO);
  public static final String LABEL_OPERATOR_ID    = labelID().apply(CalcComponent.class, LABEL_OPERATOR);

  public static final String CHART_RESULT_ID = genericID().apply(CalcComponent.class, Chart.class, LABEL_OPERATOR);
  public static final int COLUMNS = 7;
  public static final int ROWS    = 3;
  private final GridLayout grid = new GridLayout(COLUMNS, ROWS);
  private final TextField valueOne      = new TextField();
  private final TextField valueTwo      = new TextField();
  private final TextField humanResult   = new TextField();
  private final TextField machineResult = new TextField();
  private final Label resultLabel = new Label();
  private final Label operator    = new Label(PLUS.getHtml(), HTML);
  private final Button buttonNext      = new Button();
  private final Button buttonCalculate = new Button();
  private final Chart      chart  = new Chart(ChartType.BAR);
  private final ListSeries series = new ListSeries();

  //  @Inject private CalcBasicsService calcBasicsService;
  @Inject private PropertyService propertyService;
  private BasicArithmeticTask task;
  private Pair<Integer, Integer> results = Pair.next(0, 0);

  private String property(String key) { return propertyService.resolve(key);}

  @PostConstruct
  protected void postConstruct() {
    setCompositionRoot(createMathGrid());
  }

  private GridLayout createMathGrid() {
    createStructure();
    customizeFields();

    createChart();

    buttonCalculate.addClickListener((Button.ClickListener) event -> {
      String valueOneValue = valueOne.getValue();
      String valueTwoValue = valueTwo.getValue();

      //NFE
      Integer a = Integer.valueOf(valueOneValue);
      Integer b = Integer.valueOf(valueTwoValue);

      Float x = task.operator().fkt().apply(a, b);

      machineResult.setValue(valueOf(x));

      //NFE
      Float wasRight = Float.valueOf(humanResult.getValue()) - x;
      if (wasRight == 0) {
        resultLabel.setCaption(CHECK_CIRCLE.getHtml());
        resultLabel.setCaptionAsHtml(true);
        results = Pair.next(results.getT1() + 1, results.getT2());
      } else {
        resultLabel.setCaption(CLOSE_CIRCLE.getHtml());
        resultLabel.setCaptionAsHtml(true);
        results = Pair.next(results.getT1(), results.getT2() + 1);
      }
      series.setData(results.getT1(), results.getT2());
      chart.drawChart();
      resultLabel.setCaptionAsHtml(true);
      buttonCalculate.setEnabled(false);
      buttonNext.setEnabled(true);
    });

    buttonNext.addClickListener((Button.ClickListener) event -> {
      clearInputFields();

      buttonCalculate.setEnabled(true);
      buttonNext.setEnabled(false);

      task = nextRndNoDivTask().apply(10);
      operator.setValue(task.operator().sign());
      valueOne.setValue(valueOf(task.a()));
      valueTwo.setValue(valueOf(task.b()));
    });

    return grid;
  }

  private void customizeFields() {
    buttonNext.setCaption(property(BTN_NEXT));
    buttonNext.setId(BTN_NEXT_ID);

    buttonCalculate.setCaption(property(BTN_OK));
    buttonCalculate.setId(BTN_CALC_ID);

    machineResult.setId(TF_RESULT_MACHINE_ID);
    machineResult.setReadOnly(true);

    humanResult.setId(TF_RESULT_USER_ID);
    valueOne.setId(TF_VALUE_ONE_ID);
    valueTwo.setId(TF_VALUE_TWO_ID);

    operator.setId(LABEL_OPERATOR_ID);

    clearInputFields();

    buttonCalculate.setEnabled(true);
    buttonNext.setEnabled(false);

    task = nextTask().apply(Operator.ADD, 10);
    valueOne.setValue(valueOf(task.a()));
    valueTwo.setValue(valueOf(task.b()));
  }

  private void createStructure() {
    grid.addComponent(valueOne, 0, 0);
    grid.addComponent(operator, 1, 0);
    grid.addComponent(valueTwo, 2, 0);
    grid.addComponent(new Label("="), 3, 0);
    grid.addComponent(humanResult, 4, 0);
    grid.addComponent(buttonCalculate, 5, 0);
    grid.addComponent(buttonNext, 6, 0);

    grid.addComponent(machineResult, 4, 1);
    grid.addComponent(resultLabel, 5, 1);

    grid.addComponent(chart, 0, 2, 6, 2);
  }

  private void createChart() {
    chart.setId(CHART_RESULT_ID);
    final Configuration configuration = chart.getConfiguration();

    configuration.setTitle(property(CALC_COMPONENT_CHART_RESULT_TITLE_X));
    configuration.getLegend().setEnabled(false);
//    configuration.getLegend().setEnabled(true);

    final XAxis xAxis = configuration.getxAxis();
    xAxis.setCategories(
        property("calc.component.chart.result.good"),
        property("calc.component.chart.result.bad")
                       );
    xAxis.setShowEmpty(false);
//    xAxis.setShowEmpty(true);

    final YAxis yAxis = configuration.getyAxis();
    yAxis.setTitle(property("calc.component.chart.result.yAxis"));
//    yAxis.setTitle("a nice Title .. uups no PropertyService used");
//    yAxis.setTitle(property("calc.component.chart.result.title.y"));
//    yAxis.setTitle(property(CALC_COMPONENT_CHART_RESULT_TITLE_Y));

    series.setData(results.getT1(), results.getT2());
    configuration.setSeries(series);

    configuration.setScrollbar(new Scrollbar(false));
  }

  private void clearInputFields() {
    valueOne.setValue("");
    valueTwo.setValue("");

    humanResult.setValue("");
    machineResult.setValue("");

    resultLabel.setCaption("");
  }

}
