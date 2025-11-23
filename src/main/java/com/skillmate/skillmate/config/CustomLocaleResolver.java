package com.skillmate.skillmate.config;

import java.util.List;
import java.util.Locale;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

public class CustomLocaleResolver extends AcceptHeaderLocaleResolver {

  public CustomLocaleResolver() {
    setDefaultLocale(Locale.forLanguageTag("pt-BR"));
    setSupportedLocales(List.of(Locale.forLanguageTag("pt-BR"), Locale.ENGLISH));
  }

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String langParam = request.getParameter("lang");
    if (StringUtils.hasText(langParam)) {
      Locale locale = parseLocale(langParam);
      if (locale != null) {
        return locale;
      }
    }
    return LocaleUtils.normalize(super.resolveLocale(request));
  }

  private Locale parseLocale(String langParam) {
    String normalized = langParam.replace("_", "-").trim().toLowerCase();
    if (normalized.startsWith("pt")) {
      return Locale.forLanguageTag("pt-BR");
    } else if (normalized.startsWith("en")) {
      return Locale.ENGLISH;
    }
    return null;
  }
}
