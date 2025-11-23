package com.skillmate.skillmate.config;

import java.io.IOException;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocaleContextFilter extends OncePerRequestFilter {

  private final CustomLocaleResolver localeResolver;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      Locale locale = LocaleUtils.normalize(localeResolver.resolveLocale(request));
      LocaleContextHolder.setLocale(locale, true);
      filterChain.doFilter(request, response);
    } finally {
      LocaleContextHolder.resetLocaleContext();
    }
  }
}
