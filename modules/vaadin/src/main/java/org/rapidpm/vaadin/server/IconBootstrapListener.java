package org.rapidpm.vaadin.server;

import java.util.function.Function;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.rapidpm.frp.model.serial.Pair;
import org.rapidpm.frp.model.serial.Quad;
import org.rapidpm.frp.model.serial.Tripel;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.shared.VaadinUriResolver;

/**
 * Adds link-tags for "add to homescreen" icons to the head-section of the
 * bootstrap page.
 * <p>
 * Generates links of the type
 * <p>
 * <pre>
 * {@code
 * <link rel="icon" sizes="96x96" href="VAADIN/themes/apptheme/icon-96.png">
 * <link rel="apple-touch-icon" sizes="192x192" href=
 * "VAADIN/themes/apptheme/icon-192.png">
 * }
 * </pre>
 * </p>
 */
public class IconBootstrapListener implements BootstrapListener {

  private Function<String, Quad<String, String, String, String>> iconFunct() {
    return (size) -> Quad.next("icon" , "image/png" , size , "/android-icon-" + size + IMAGE_TYPE);
  }

  private Stream<Quad<String, String, String, String>> iconStream() {
    final Function<String, Quad<String, String, String, String>> f = iconFunct();
    return Stream.of(
        f.apply("192x192") ,
        f.apply("32x32") ,
        f.apply("96x96") ,
        f.apply("16x16")
    );
  }

  private static final String IMAGE_TYPE = ".png";

  private Function<String, Tripel<String, String, String>> linkFunct() {
    return (size) -> Tripel.next("apple-touch-icon" , size , "icons/apple-icon-" + size + IMAGE_TYPE);
  }

  private Stream<Tripel<String, String, String>> linkStream() {
    final Function<String, Tripel<String, String, String>> f = linkFunct();
    return Stream.of(
        f.apply("57x57") ,
        f.apply("60x60") ,
        f.apply("72x72") ,
        f.apply("76x76") ,
        f.apply("114x114") ,
        f.apply("120x120") ,
        f.apply("144x144") ,
        f.apply("152x152") ,
        f.apply("180x180")
    );
  }
//    <link rel="manifest" href="/manifest.json">


  private Stream<Pair<String, String>> metaStream() {
    return Stream.of(
        Pair.next("msapplication-TileColor" , "#ffffff") ,
        Pair.next("msapplication-TileImage" , "icons/ms-icon-144x144.png") ,
        Pair.next("theme-color" , "#ffffff")
    );
  }

  @Override
  public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
    // NOP
  }

  @Override
  public void modifyBootstrapPage(BootstrapPageResponse response) {
    // Generate link-tags for "add to homescreen" icons
    final Document document = response.getDocument();
    final Element head = document.getElementsByTag("head")
                                 .get(0);
    final VaadinUriResolver uriResolver = response.getUriResolver();

    linkStream()
        .forEach(e -> head
            .appendChild(document
                             .createElement("link")
                             .attr("rel" , e.getT1())
                             .attr("sizes" , e.getT2())
                             .attr("href" , uriResolver
                                 .resolveVaadinUri(e.getT3()))));

    iconStream()
        .forEach(e -> head
            .appendChild(document
                             .attr("rel" , e.getT1())
                             .attr("sizes" , e.getT2())
                             .attr("href" , uriResolver
                                 .resolveVaadinUri(e.getT4()))));

    /*- Enable these to hide browser controls when app is started from homescreen: */
    head
        .appendChild(document
                         .createElement("meta")
                         .attr("name" , "mobile-web-app-capable")
                         .attr("content" , "yes"));

    head
        .appendChild(document
                         .createElement("meta")
                         .attr("name" , "apple-mobile-web-app-capable")
                         .attr("content" , "yes"));
  }

}