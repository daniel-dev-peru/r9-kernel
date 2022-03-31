package org.raise9.kernel.open.api;

import java.util.stream.Stream;

public enum ParameterTypeExtensions {
  HEADER_PARAM("x-quarkus-header", "header"),
  QUERY_PARAM("x-quarkus-query", "query"),
  UNKNOWN("", "");

  private final String extension;
  private final String parameterType;

  ParameterTypeExtensions(String extension, String parameterType) {
    this.extension = extension;
    this.parameterType = parameterType;
  }

  public static ParameterTypeExtensions getByString(String extension) {
    return Stream.of(values()).filter(parameterTypeExtensions -> parameterTypeExtensions.getExtension()
            .equals(extension))
            .findFirst()
            .orElse(UNKNOWN);
  }

  public String getExtension() {
    return this.extension;
  }

  public String getParameterType() {
    return this.parameterType;
  }
}

