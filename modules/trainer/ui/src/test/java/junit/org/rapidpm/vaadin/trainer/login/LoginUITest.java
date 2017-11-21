package junit.org.rapidpm.vaadin.trainer.login;

import static org.rapidpm.vaadin.ComponentIDGenerator.buttonID;
import static org.rapidpm.vaadin.trainer.modules.mainview.menu.MenuComponent.MENU_POINT_DASHBOARD;

import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.rapidpm.vaadin.trainer.modules.login.LoginComponent;
import org.rapidpm.vaadin.trainer.modules.mainview.menu.MenuComponent;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;
import junit.org.rapidpm.vaadin.junit5.extensions.pageobject.PageObject;
import junit.org.rapidpm.vaadin.junit5.AbstractVaadinPageObject;
import junit.org.rapidpm.vaadin.junit5.VaadinTest;

/**
 *
 */

@VaadinTest
public class LoginUITest {


  public static class LoginUIPageObject extends AbstractVaadinPageObject {

    public LoginUIPageObject(WebDriver webDriver) {
      super(webDriver);
    }

    public Supplier<ButtonElement> buttonOK = () -> btn().id(LoginComponent.ID_BUTTON_OK);
    public Supplier<ButtonElement> buttonCancel = () -> btn().id(LoginComponent.ID_BUTTON_CANCEL);

    public Supplier<TextFieldElement> login = () -> textField().id(LoginComponent.ID_TEXTFIELD_LOGIN);
    public Supplier<PasswordFieldElement> password = () -> passwordField().id(LoginComponent.ID_PASSWORDFIELD_PASSWORD);

  }


  @DisplayName("Login positive")
  @Test
  void test001(@PageObject LoginUIPageObject pageObject) {
    pageObject.loadPage();

    pageObject.login.get().setValue("sven");
    pageObject.password.get().setValue("sven");

    pageObject.buttonOK.get().click();

    // Assert if new layout is loaded
    // the Caption "Dashboard" is not refactoring safe
    ButtonElement dashboard = pageObject.btn().id(buttonID().apply(MenuComponent.class, MENU_POINT_DASHBOARD));
    Assert.assertNotNull(dashboard);
  }


  @DisplayName("Login negative")
  @Test
  void test002(@PageObject LoginUIPageObject pageObject) {
    pageObject.loadPage();

    pageObject.login.get().setValue("Login");
    pageObject.password.get().setValue("XXX");

    pageObject.buttonOK.get().click();
    //ASSERT : check if Message is visible - Notification

    Assert.assertEquals("", pageObject.login.get().getValue());
    Assert.assertEquals("", pageObject.password.get().getValue());
  }

  @DisplayName("Login cancel")
  @Test
  void test003(@PageObject LoginUIPageObject pageObject) {
    pageObject.loadPage();

    pageObject.login.get().setValue("Login");
    pageObject.password.get().setValue("XXX");

    pageObject.buttonCancel.get().click();
    //ASSERT : check if Message is visible - Notification

    Assert.assertEquals("", pageObject.login.get().getValue());
    Assert.assertEquals("", pageObject.password.get().getValue());
  }
}
