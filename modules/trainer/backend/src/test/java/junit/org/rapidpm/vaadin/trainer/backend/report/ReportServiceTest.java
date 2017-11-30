package junit.org.rapidpm.vaadin.trainer.backend.report;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedBiFunction;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.functions.CheckedSupplier;
import org.rapidpm.frp.model.Result;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.rnorth.visibleassertions.VisibleAssertions.assertEquals;

/**
 *
 */
public class ReportServiceTest implements HasLogger {

  private Result<PostgreSQLContainer> postgreSQLContainer;

  @BeforeEach
  void setUp() {
    postgreSQLContainer = ((CheckedSupplier<PostgreSQLContainer>) PostgreSQLContainer::new).get();
    postgreSQLContainer.ifPresentOrElse(
        GenericContainer::start,
        (Runnable) Assert::fail
    );
    postgreSQLContainer.ifPresent(c -> {
      final Flyway flyway = flyway().apply(c);
      flyway.clean();
//      flyway.baseline();
      flyway.migrate();
    });
  }

  @AfterEach
  void tearDown() {
//    postgreSQLContainer.ifPresentOrElse(
//        GenericContainer::stop,
//        logger()::warning
//    );
  }


  public Function<JdbcDatabaseContainer, Flyway> flyway() {
    return (container) -> {
      final Flyway flyway = new Flyway();
      flyway.setDataSource(container.getJdbcUrl(),
                           container.getUsername(),
                           container.getPassword()
      );
      return flyway;
    };
  }

  public Function<JdbcDatabaseContainer, HikariDataSource> datasource() {
    return (container) -> {
      final HikariConfig hikariConfig = new HikariConfig();
      hikariConfig.setJdbcUrl(container.getJdbcUrl());
      hikariConfig.setUsername(container.getUsername());
      hikariConfig.setPassword(container.getPassword());
      return new HikariDataSource(hikariConfig);
    };
  }

  public CheckedFunction<HikariDataSource, Statement> createStatement() {
    return ds -> ds.getConnection().createStatement();
  }

  public Function<JdbcDatabaseContainer, Result<Statement>> statement() {
    return datasource().andThen(createStatement());
  }

  private Supplier<Result<Statement>> stmt() {
    return () -> statement().apply(postgreSQLContainer.get());
  }

  private CheckedBiFunction<Statement, String, ResultSet> query(){
    return (statement, sql) -> {
      assertEquals("sql was executed", statement.execute(sql), true);
      return statement.getResultSet();
    };
  }

  private CheckedFunction<ResultSet, ResultSet> next(){
    return (rs) -> {
      rs.next();
      return rs;
    };
  }



  @Test
  void test001() {
    final String testQueryString = postgreSQLContainer.get().getTestQueryString();
    logger().info("testQueryString -> " + testQueryString);

    stmt()
        .get()
        .thenCombine(testQueryString, query())
        .map(next())
        .flatMap((CheckedFunction<Result<ResultSet>, Integer>) r -> r.get().getInt(1))
        .ifPresentOrElse(
            success -> assertEquals("A basic SELECT query succeeds", 1, success),
            (Consumer<String>) Assert::fail
        );
  }


  @Test
  void test002() {

    final String sql = "SELECT count(*) FROM comp_math_basic";
    logger().info("sql -> " + sql);

    stmt()
        .get()
        .thenCombine(sql, query())
        .map(next())
        .flatMap((CheckedFunction<Result<ResultSet>, Integer>) r -> r.get().getInt(1))
        .ifPresentOrElse(
            success -> assertEquals("Count must be 1", success, 1),
            (Consumer<String>) Assert::fail
        );
  }

  @Test
  void test003() {
    //speedment query






  }
}
