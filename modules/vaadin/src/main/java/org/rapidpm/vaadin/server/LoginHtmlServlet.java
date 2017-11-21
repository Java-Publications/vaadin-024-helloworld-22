package org.rapidpm.vaadin.server;

import static org.apache.commons.io.IOUtils.copy;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.failure;
import static org.rapidpm.frp.model.Result.success;
import static org.rapidpm.vaadin.server.LoginHtmlServlet.ICONS;
import static org.rapidpm.vaadin.server.LoginHtmlServlet.LOGIN_HTML;
import static org.rapidpm.vaadin.server.LoginHtmlServlet.LOGO_192X192_PNG;
import static org.rapidpm.vaadin.server.LoginHtmlServlet.LOGO_96X96_PNG;
import static org.rapidpm.vaadin.server.LoginHtmlServlet.SLASH;

import java.io.InputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rapidpm.frp.functions.CheckedFunction;

@WebServlet(
    asyncSupported = true,
    urlPatterns = {
        SLASH + LOGIN_HTML ,
        SLASH + LOGO_96X96_PNG ,
        SLASH + LOGO_192X192_PNG
    })
public class LoginHtmlServlet extends HttpServlet {

  public static final String SLASH = "/";
  public static final String ICONS = "icons" + SLASH;

  public static final String LOGIN_HTML = "login.html";
  public static final String LOGO_96X96_PNG = ICONS + "logo-96x96.png";
  public static final String LOGO_192X192_PNG = ICONS + "logo-192x192.png";

  @Override
  protected void doGet(HttpServletRequest req , HttpServletResponse resp) {
    serveLoginHtml(req , resp);
  }

  @Override
  protected void doPost(HttpServletRequest req , HttpServletResponse resp) {
    serveLoginHtml(req , resp);
  }

  private void serveLoginHtml(HttpServletRequest request , HttpServletResponse response) {
    response.setCharacterEncoding("utf-8");
    final String url = request.getRequestURL().toString();
    System.out.println("url = " + url);
    match(
        matchCase(() -> failure("nothing matched " + url)) ,
        matchCase(() -> url.contains(LOGIN_HTML) , () -> success(SLASH + LOGIN_HTML)) ,
        matchCase(() -> url.contains(LOGO_96X96_PNG) , () -> success(SLASH + LOGO_96X96_PNG)) ,
        matchCase(() -> url.contains(LOGO_192X192_PNG) , () -> success(SLASH + LOGO_192X192_PNG))
    )
        .ifPresentOrElse(success -> {
                           ((CheckedFunction<String, InputStream>) LoginHtmlServlet.class::getResourceAsStream)
                               .apply(success)
                               .ifPresentOrElse(
                                   ((CheckedFunction<InputStream, Integer>)
                                       inputStream -> copy(inputStream ,
                                                           response.getOutputStream()))::apply ,
                                   failed -> {
                                     //logging ->
                                     System.out.println("failed = " + failed);
                                   }
                               );
                         } ,
                         failed -> {
                           //logging ->
                           System.out.println("failed = " + failed);
                         }


        );
  }
}
