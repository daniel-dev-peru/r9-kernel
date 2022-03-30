package org.raise9.kernel.utils.core;

import java.util.UUID;

public class GenerateCorrelation {
  public static final String DEFAULT_CLIENT = "<default>";

  public static String generate(String base) {
    UUID uuid = UUID.randomUUID();
    return base + "-" + uuid.toString();
  }

}