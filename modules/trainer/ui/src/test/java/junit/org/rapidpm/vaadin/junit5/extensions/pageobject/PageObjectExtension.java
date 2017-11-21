package junit.org.rapidpm.vaadin.junit5.extensions.pageobject;

import static junit.org.rapidpm.vaadin.junit5.VaadinPageObject.KEY_VAADIN_SERVER_IP;
import static junit.org.rapidpm.vaadin.junit5.extensions.ExtensionFunctions.ipSupplierLocalIP;
import static junit.org.rapidpm.vaadin.junit5.extensions.testcontainers.TestcontainersExtension.webdriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.WebDriver;
import junit.org.rapidpm.vaadin.junit5.AbstractVaadinPageObject;

/**
 *
 */
public class PageObjectExtension implements ParameterResolver, BeforeAllCallback {

  @Override
  public boolean supportsParameter(ParameterContext parameterContext , ExtensionContext extensionContext)
//  public boolean supports(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().isAnnotationPresent(PageObject.class);
  }

  @Override
  public AbstractVaadinPageObject resolveParameter(ParameterContext parameterContext , ExtensionContext extensionContext)
//  public AbstractVaadinPageObject resolve(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {

    Supplier<WebDriver> webDriverSupplier = webdriver().apply(extensionContext);

    Class<?> pageObject = parameterContext.getParameter().getType();
    try {
      Constructor<?> constructor =
          pageObject.getConstructor(WebDriver.class);
      return AbstractVaadinPageObject.class.cast(constructor.newInstance(webDriverSupplier.get()));
    } catch (NoSuchMethodException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException e) {
      e.printStackTrace();
      throw new ParameterResolutionException("was not able to create PageObjectInstance " , e);
    }
  }

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
//  public void beforeAll(ContainerExtensionContext context) throws Exception {
    System.setProperty(KEY_VAADIN_SERVER_IP , ipSupplierLocalIP.get());
  }

}
