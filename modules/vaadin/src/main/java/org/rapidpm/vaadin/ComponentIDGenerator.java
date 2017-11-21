package org.rapidpm.vaadin;

import java.util.Locale;
import java.util.function.BiFunction;

import org.rapidpm.frp.functions.TriFunction;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
public interface ComponentIDGenerator {
  static TriFunction<Class, Class, String, String> genericID() {
    return (uiClass, componentClass, label)
        -> (uiClass.getSimpleName()
            + "-" + componentClass.getSimpleName()
            + "-" + label.replace(" ", "-"))
        .toLowerCase(Locale.US);
  }

  static BiFunction<Class, String, String> gridID() {
    return (uiClass, label) -> genericID().apply(uiClass, Grid.class, label);
  }

  static BiFunction<Class, String, String> buttonID() {
    return (uiClass, label) -> genericID().apply(uiClass, Button.class, label);
  }

  static BiFunction<Class, String, String> labelID() {
    return (uiClass, label) -> genericID().apply(uiClass, Label.class, label);
  }

  static BiFunction<Class, String, String> textfieldID() {
    return (uiClass, label) -> genericID().apply(uiClass, TextField.class, label);
  }

  static BiFunction<Class, String, String> passwordID() {
    return (uiClass, label) -> genericID().apply(uiClass, PasswordField.class, label);
  }

  static BiFunction<Class, String, String> cssLayoutID() {
    return (uiClass, label) -> genericID().apply(uiClass, CssLayout.class, label);
  }

  static BiFunction<Class, String, String> verticalLayoutID() {
    return (uiClass, label) -> genericID().apply(uiClass, VerticalLayout.class, label);
  }

  static BiFunction<Class, String, String> horizontalLayoutID() {
    return (uiClass, label) -> genericID().apply(uiClass, HorizontalLayout.class, label);
  }
}
