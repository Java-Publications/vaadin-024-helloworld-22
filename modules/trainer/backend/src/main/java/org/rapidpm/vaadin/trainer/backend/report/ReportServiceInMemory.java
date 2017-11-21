package org.rapidpm.vaadin.trainer.backend.report;

import org.rapidpm.vaadin.trainer.api.report.ReportService;
import org.rapidpm.vaadin.trainer.api.report.Statistics;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class ReportServiceInMemory implements ReportService {


  @Override
  public List<Statistics> loadData() {
    return Arrays
        .asList(
            new Statistics(1L, "Sven", "Ruppert", LocalDateTime.now()),
            new Statistics(1L, "Max", "Rimkus", LocalDateTime.now())
               );
  }

}
