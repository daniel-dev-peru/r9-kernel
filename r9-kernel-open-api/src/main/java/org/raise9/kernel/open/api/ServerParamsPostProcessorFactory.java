package org.raise9.kernel.open.api;

import java.util.Optional;

public class ServerParamsPostProcessorFactory implements ParamsPostProcessorFactory {

  public Optional<ParamsPostProcessor> getParameterProcessor(String type) {

    switch (ParameterTypeExtensions.getByString(type)) {

      case HEADER_PARAM:

        return Optional.of(new HeadersExtensionPostProcessor());

      case QUERY_PARAM:

        return Optional.of(new QueryExtensionPostProcessor());

    }

    return Optional.empty();

  }

}
