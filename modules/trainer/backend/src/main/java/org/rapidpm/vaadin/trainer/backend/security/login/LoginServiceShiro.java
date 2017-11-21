package org.rapidpm.vaadin.trainer.backend.security.login;

import static java.time.Duration.between;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.rapidpm.frp.model.Pair;
import org.rapidpm.vaadin.trainer.api.security.login.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class LoginServiceShiro implements LoginService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceShiro.class);

  private static final Map<String, Pair<LocalDateTime, Integer>> failedLogins = new ConcurrentHashMap<>();
  public static final int MAX_FAILED_LOGINS = 3;
  public static final int MINUTES_TO_WAIT = 1;
  public static final int MINUTES_TO_BE_CLEANED = 2;
  public static final int MILLISECONDS_TO_BE_CLEANED = 1_000 * 60 * MINUTES_TO_BE_CLEANED;

  public static final int MILLISECONDS_INITIAL_DELAY = 100;


  public static class FailedLoginCleaner {
    private final Timer failedLoginCleanUpTimer = new Timer();

    public FailedLoginCleaner(TimerTask tasknew) {
      failedLoginCleanUpTimer.schedule(tasknew , MILLISECONDS_INITIAL_DELAY , MILLISECONDS_TO_BE_CLEANED);
    }
  }

  private static final FailedLoginCleaner FAILED_LOGIN_CLEANER = new FailedLoginCleaner(new TimerTask() {
    @Override
    public void run() {
      LOGGER.debug(" start cleaning " + LocalDateTime.now());
      failedLogins
          .keySet()
          .forEach((String key) -> {
            Pair<LocalDateTime, Integer> pair = failedLogins.get(key);
            if (pair != null) {
              LOGGER.debug("work on login/pair = " + key + " - " + pair);
              final Duration duration = between(pair.getT1() , LocalDateTime.now());
              long minutes = duration.toMinutes();
              if (minutes > MINUTES_TO_BE_CLEANED) {
                failedLogins.remove(key); // start from zero
                LOGGER.debug("  ==>  cleaned key = " + key);
              }
            }
          });
    }
  });


  @Override
  public boolean check(String login , String password) {
    //TODO FAILED LOGIN Counter Rule
    if (failedLogins.containsKey(login)) {
      Pair<LocalDateTime, Integer> pair = failedLogins.get(login);
      LocalDateTime failedLoginDate = pair.getT1();
      Integer failedLoginCount = pair.getT2();
      if (failedLoginCount > MAX_FAILED_LOGINS) {
        LOGGER.debug("failedLoginCount > MAX_FAILED_LOGINS " + failedLoginCount);
        final Duration duration = between(failedLoginDate , LocalDateTime.now());
        long minutes = duration.toMinutes();
        if (minutes > MINUTES_TO_WAIT) {
          LOGGER.debug("minutes > MINUTES_TO_WAIT (remove login) " + failedLoginCount);
          failedLogins.remove(login); // start from zero
        }
        else {
          LOGGER.debug("failedLoginCount <= MAX_FAILED_LOGINS " + failedLoginCount);
          failedLogins.compute(
              login ,
              (s , faildPair) -> new Pair<>(LocalDateTime.now() , failedLoginCount + 1));
          return false;
        }
      }
      else {
        LOGGER.debug("failedLoginCount => " + login + " - " + failedLoginCount);
      }
    }

    final UsernamePasswordToken token = new UsernamePasswordToken(login , password);
    final Subject subject = SecurityUtils.getSubject();
    try {
      subject.login(token);
      printInfos(subject);
      failedLogins.remove(login);
    } catch (AuthenticationException e) {
      LOGGER.debug("login failed " + login);
      //e.printStackTrace();
      failedLogins.putIfAbsent(login , new Pair<>(LocalDateTime.now() , 0));
      failedLogins.compute(
          login ,
          (s , oldPair) -> new Pair<>(LocalDateTime.now() , oldPair.getT2() + 1));
    }
    return subject.isAuthenticated();
  }


  private void printInfos(Subject subject) {
//    List<String> roles = new ArrayList<>();
//    roles.add("parent");
//    System.out.println("subject = " + subject);
//    PrincipalCollection principals = subject.getPrincipals();
//    principals.forEach(p -> {
//      System.out.println("p = " + p);
//
//      boolean[] booleans = subject.hasRoles(roles);
//      for (boolean aBoolean : booleans) {
//        System.out.println("aBoolean = " + aBoolean);
//      }
//      System.out.println("write:WriteComponent = " + subject.isPermitted("write:WriteComponent"));
//      System.out.println("math:CalcComponent = " + subject.isPermitted("math:CalcComponent"));
//      System.out.println("report:* = " + subject.isPermitted("report:*"));
//      System.out.println("report:ReportDashboard = " + subject.isPermitted("report:ReportDashboard"));
//      System.out.println("report:xx = " + subject.isPermitted("report:xx"));
//
//
//    });

  }


}
