package junit.org.rapidpm.vaadin.junit5;

import static java.lang.System.getProperties;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.vaadin.testbench.HasDriver;
import org.rapidpm.microservice.MainUndertow;

/**
 *
 */
public interface VaadinPageObject extends HasDriver {

  String DEFAULT_PROTOCOL = "http";
  String DEFAULT_IP = "127.0.0.1";

  String KEY_VAADIN_SERVER_PROTOCOL = "vaadin.server.protocol";
  String KEY_VAADIN_SERVER_IP = "vaadin.server.ip";
  String KEY_VAADIN_SERVER_PORT = "vaadin.server.port";
  String KEY_VAADIN_SERVER_WEBAPP = "vaadin.server.webapp";


  default BiFunction<String, String, String> property() {
    return (key , defaultValue) -> (String) getProperties().getOrDefault(key , defaultValue);
  }

  default Supplier<String> protocol() {
    return () -> property().apply(KEY_VAADIN_SERVER_PROTOCOL , DEFAULT_PROTOCOL);
  }

  default Supplier<String> ip() {
    return () -> property().apply(KEY_VAADIN_SERVER_IP , DEFAULT_IP);
  }

  default Supplier<String> port() {
    return () -> property().apply(KEY_VAADIN_SERVER_PORT , MainUndertow.DEFAULT_SERVLET_PORT + "");
  }

  default Supplier<String> webapp() {
    return () -> property().apply(KEY_VAADIN_SERVER_WEBAPP , MainUndertow.MYAPP);
  }


  default Supplier<String> baseURL() {
    return () -> protocol().get() + "://" + ip().get() + ":" + port().get();
  }

  default Supplier<String> url() {
    return () -> baseURL().get() + webapp().get() + "/";
  }

  default Supplier<String> urlRestartApp() {
    return () -> url().get() + "?restartApplication";
  }


}
