package org.raise9.kernel.open.api;

import java.util.Optional;

public interface ParamsPostProcessorFactory {
  Optional<ParamsPostProcessor> getParameterProcessor(String paramString);
}
