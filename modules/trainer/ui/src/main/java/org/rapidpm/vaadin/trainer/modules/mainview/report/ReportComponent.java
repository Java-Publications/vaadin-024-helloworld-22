package org.rapidpm.vaadin.trainer.modules.mainview.report;

import com.vaadin.ui.*;
import org.rapidpm.vaadin.trainer.api.property.PropertyService;
import org.rapidpm.vaadin.trainer.api.report.ReportService;
import org.rapidpm.vaadin.trainer.api.report.Statistics;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.rapidpm.vaadin.ComponentIDGenerator.gridID;

/**
 *
 */
public class ReportComponent extends Composite {


  public static final String REPORT_STATISTITICS_GRID            = "report.component.statistics.grid";
  public static final String REPORT_STATISTITICS_GRID_ID         = "report.component.statistics.grid.column.forename";
  public static final String REPORT_STATISTITICS_GRID_FORENAME   = "report.component.statistics.grid.column.forename";
  public static final String REPORT_STATISTITICS_GRID_FAMILYNAME = "report.component.statistics.grid.column.familyname";
  public static final String REPORT_STATISTITICS_GRID_LAST_LOGIN = "report.component.statistics.grid.column.lastlogin";

  private @Inject PropertyService propertyService;
  private @Inject ReportService   reportService;


  private final Grid<Statistics> statisticsGrid = new Grid<>();

  public ReportComponent() {
    statisticsGrid.setColumnReorderingAllowed(true);
    statisticsGrid.setSizeFull();
  }

  private String property(String key) { return propertyService.resolve(key); }

  @PostConstruct
  private void postConstruct() {

    statisticsGrid.setId(gridID().apply(ReportComponent.class, REPORT_STATISTITICS_GRID));

    statisticsGrid.addColumn(Statistics::id)
                  .setCaption(property(REPORT_STATISTITICS_GRID_ID));
    statisticsGrid.addColumn(Statistics::foreName)
                  .setCaption(property(REPORT_STATISTITICS_GRID_FORENAME));
    statisticsGrid.addColumn(Statistics::familyName)
                  .setCaption(property(REPORT_STATISTITICS_GRID_FAMILYNAME));
    statisticsGrid.addColumn(Statistics::lastLogin)
                  .setCaption(property(REPORT_STATISTITICS_GRID_LAST_LOGIN));


    final Button clickMe = new Button("consume ReportService");
    clickMe.addClickListener((Button.ClickListener) event -> {
      List<Statistics> data = reportService.loadData();
      statisticsGrid.setItems(data);
    });

    final Button clearData = new Button("clear data");
    clearData.addClickListener((Button.ClickListener) event -> statisticsGrid.setItems(emptyList()));

    setCompositionRoot(
        new VerticalLayout(
            new HorizontalLayout(clickMe, clearData),
//            new Label(" implements? " + (new Service() instanceof DemoInterface )),
            statisticsGrid
        )
    );
  }



//  public static interface DemoInterface {
//    public String doWork();
//  }
//
////  public static class Service implements DemoInterface {
//  public static class Service  {
////    @Override
//    public String doWork() {
//      return "implements";
//    }
//  }
//
//
//
//
//


}

