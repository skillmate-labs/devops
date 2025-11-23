package com.skillmate.skillmate.config;

import java.util.Locale;

public final class LocaleUtils {

  private LocaleUtils() {
  }

  public static Locale normalize(Locale locale) {
    if (locale == null) {
      return Locale.forLanguageTag("pt-BR");
    }
    String language = locale.getLanguage().toLowerCase();
    if ("en".equals(language)) {
      return Locale.ENGLISH;
    } else if ("pt".equals(language)) {
      return Locale.forLanguageTag("pt-BR");
    }
    return Locale.forLanguageTag("pt-BR");
  }
}
