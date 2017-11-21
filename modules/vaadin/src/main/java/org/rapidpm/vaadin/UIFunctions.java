package org.rapidpm.vaadin;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HasComponents;

/**
 *
 */
public interface UIFunctions {

  public static BiFunction<HasComponents,String, Optional<Component>> componentWithID() {
    return (root, id) -> {
      for (Component child : root) {
        if (id.equals(child.getId())) {
          return Optional.of(child);
        } else if (child instanceof HasComponents) {
          Optional<Component> ret = componentWithID().apply((HasComponents) child, id);
          if (ret.isPresent()) return ret;
        }
      }
      // none was found
      return Optional.empty();
    };
  }

  public static Function<? extends Component, ? extends CustomComponent> wrap(){
    return (input) -> {
      final CustomComponent customComponent = new CustomComponent(input);
      customComponent.setSizeFull();
      return customComponent;
    };
  }

}
