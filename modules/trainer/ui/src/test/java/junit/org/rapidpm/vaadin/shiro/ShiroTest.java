package junit.org.rapidpm.vaadin.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * admin = *
 * math = CalcComponent
 * write = WriteComponent
 * report = mathreport, writereport
 * <p>
 * parent = math:*, write:*, report:*
 * child = math:*, write:*
 */
public class ShiroTest {

  private Subject subject;

  @BeforeEach
  void setUp() {

    IniSecurityManagerFactory factory = new IniSecurityManagerFactory("./src/test/resources/shiro-test.ini");
    SecurityManager securityManager = factory.getInstance();
    SecurityUtils.setSecurityManager(securityManager);
    subject = SecurityUtils.getSubject();
  }

  @AfterEach
  void tearDown() {
//    subject.logout();
  }

  @Test
  void test001() {
    subject.login(new UsernamePasswordToken("max", "max"));
    Assert.assertTrue(subject.isAuthenticated());
    Assert.assertEquals("max", subject.getPrincipal());

    Assert.assertTrue(subject.hasRole("child"));
    Assert.assertTrue(subject.isPermitted("math:*"));
    Assert.assertTrue(subject.isPermitted("math:CalcComponent"));
    Assert.assertTrue(subject.isPermitted("math:HoppelPoppel"));
  }
}
