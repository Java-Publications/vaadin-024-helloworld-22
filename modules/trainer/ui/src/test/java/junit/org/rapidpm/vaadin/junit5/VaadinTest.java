package junit.org.rapidpm.vaadin.junit5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import junit.org.rapidpm.vaadin.junit5.extensions.container.ServletContainerExtension;
import junit.org.rapidpm.vaadin.junit5.extensions.testcontainers.TestcontainersExtension;
import junit.org.rapidpm.vaadin.junit5.extensions.pageobject.PageObjectExtension;

/**
 *
 */

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
//Order is important top / down
@ExtendWith(ServletContainerExtension.class)
@ExtendWith(TestcontainersExtension.class)
@ExtendWith(PageObjectExtension.class)
public @interface VaadinTest {
}
