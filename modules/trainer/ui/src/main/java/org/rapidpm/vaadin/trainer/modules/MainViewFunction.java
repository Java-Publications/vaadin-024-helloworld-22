package org.rapidpm.vaadin.trainer.modules;

import java.util.function.Function;

import org.rapidpm.ddi.DI;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Composite;

/**
 *
 */
public interface MainViewFunction {


  public static <T> Function<T, T> inject(){
    return DI::activateDI;
  }

  public static <T extends AbstractComponent> Function<T, Composite> composite(){
    return Composite::new;
  }









}
