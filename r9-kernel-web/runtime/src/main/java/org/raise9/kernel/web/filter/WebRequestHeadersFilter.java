package org.raise9.kernel.web.filter;

import io.quarkus.runtime.StartupEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.raise9.kernel.exceptions.ApiError;
import org.raise9.kernel.exceptions.ApiErrorDetail;
import org.raise9.kernel.utils.core.metadata.HttpDataCurrent;
import org.raise9.kernel.web.exceptions.R9WebFilterException;
import org.raise9.kernel.web.filter.config.Header;
import org.raise9.kernel.web.filter.config.WebRequestHeadersConfig;

@Slf4j
@Priority(14)
@Provider
@PreMatching
public class WebRequestHeadersFilter implements ContainerRequestFilter {

  private final String ERROR_CODE_HEADER = "109900";
  private final String ERROR_CODE_HEADER_INVALID = "109901";
  private final String CONFIG_PREFIX = "r9.system";

  @Inject
  HttpDataCurrent httpDataCurrent;

  private Map<String, Object> result = new HashMap<>();
  private WebRequestHeadersConfig webRequestHeadersConfig;

  void onStart(@Observes StartupEvent ev) {
    this.result = new HashMap<>();
    Config config = ConfigProvider.getConfig();
    for (String propertyName : config.getPropertyNames()) {
      String propertyNameLowerCase = propertyName;//.toLowerCase();
      if (!propertyNameLowerCase.startsWith(CONFIG_PREFIX)) {
        continue;
      }
      String effectivePropertyName = propertyNameLowerCase.substring(CONFIG_PREFIX.length() + 1)
              //.toLowerCase()
              .replaceAll("[^A-Za-z0-9.]", ".");
      String
              value =
              config.getOptionalValue(propertyName, String.class)
                      .orElse("");
      result.put(effectivePropertyName, value);
    }
    log.debug("Config of {} -> {}", CONFIG_PREFIX, result);
    webRequestHeadersConfig = new WebRequestHeadersConfig();
    webRequestHeadersConfig.setRequired(false);
    result.entrySet()
            .stream()
            .forEach(stringObjectEntry -> {
              if (stringObjectEntry.getKey()
                      .equals("required")) {
                webRequestHeadersConfig.setRequired(Boolean.valueOf(stringObjectEntry.getValue()
                        .toString()));
              }
              if (stringObjectEntry.getKey()
                      .contains(".name")) {
                String
                        f =
                        stringObjectEntry.getKey()
                                .substring(0, stringObjectEntry.getKey()
                                        .indexOf(".name"));
                Optional<Header>
                        hOptinal =
                        webRequestHeadersConfig.getHeaders()
                                .stream()
                                .filter(h -> {
                                  return h.getKey()
                                          .equals(f);
                                })
                                .findFirst();
                if (!hOptinal.isPresent()) {
                  Header newHeader = new Header();
                  newHeader.setKey(f);
                  newHeader.setField(stringObjectEntry.getValue()
                          .toString());
                  webRequestHeadersConfig.getHeaders()
                          .add(newHeader);
                } else {
                  Header h = hOptinal.get();
                  h.setField(stringObjectEntry.getValue()
                          .toString());
                }
              }
              if (stringObjectEntry.getKey()
                      .contains(".required")) {
                String
                        f =
                        stringObjectEntry.getKey()
                                .substring(0, stringObjectEntry.getKey()
                                        .indexOf(".required"));
                Optional<Header>
                        hOptinal =
                        webRequestHeadersConfig.getHeaders()
                                .stream()
                                .filter(h -> {
                                  return h.getKey()
                                          .equals(f);
                                })
                                .findFirst();
                if (!hOptinal.isPresent()) {
                  Header newHeader = new Header();
                  newHeader.setKey(f);
                  newHeader.setRequired(Boolean.valueOf(stringObjectEntry.getValue()
                          .toString()));
                  webRequestHeadersConfig.getHeaders()
                          .add(newHeader);
                } else {
                  Header h = hOptinal.get();
                  h.setRequired(Boolean.valueOf(stringObjectEntry.getValue()
                          .toString()));
                }
              }
              if (stringObjectEntry.getKey()
                      .contains(".pattern")) {
                String
                        f =
                        stringObjectEntry.getKey()
                                .substring(0, stringObjectEntry.getKey()
                                        .indexOf(".pattern"));
                Optional<Header>
                        hOptinal =
                        webRequestHeadersConfig.getHeaders()
                                .stream()
                                .filter(h -> {
                                  return h.getKey()
                                          .equals(f);
                                })
                                .findFirst();
                if (!hOptinal.isPresent()) {
                  Header newHeader = new Header();
                  newHeader.setKey(f);
                  newHeader.setPattern(stringObjectEntry.getValue()
                          .toString());
                  webRequestHeadersConfig.getHeaders()
                          .add(newHeader);
                } else {
                  Header h = hOptinal.get();
                  h.setPattern(stringObjectEntry.getValue()
                          .toString());
                }
              }
            });
    log.debug("Config filter onStart {}", webRequestHeadersConfig);
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    log.debug("Config filter request {}", webRequestHeadersConfig);
    if (!Objects.isNull(webRequestHeadersConfig.getRequired()) && webRequestHeadersConfig.getRequired()) {
      MultivaluedMap<String, String> headersRequest = containerRequestContext.getHeaders();
      Map<String, String> mapHeaders = new HashMap<>();
      List<Header> headerConfigs = webRequestHeadersConfig.getHeaders();
      for (Header headerConfig : headerConfigs) {
        if (!Objects.isNull(headerConfig.getRequired()) && headerConfig.getRequired()) {
          log.debug("Header {} exists {}", headerConfig.getField(), headersRequest.containsKey(headerConfig.getField()));
          if (!headersRequest.containsKey(headerConfig.getField())) {
            log.error("Error header " + headerConfig.getField() + " not detect");
            ApiErrorDetail apiErrorDetail = new ApiErrorDetail();
            apiErrorDetail.setComponent("WebRequestHeadersFilter");
            apiErrorDetail.setDescription("Error header " + headerConfig.getField());
            ApiError apiError = new ApiError(ERROR_CODE_HEADER,"Error header " + headerConfig.getField() + " not detect", Response.Status.BAD_REQUEST.getReasonPhrase(), Arrays.asList(apiErrorDetail));
            R9WebFilterException apiExceptionEntityRest = new R9WebFilterException(apiError);
            containerRequestContext.abortWith(Response.status(Response.Status.BAD_REQUEST)
                    .entity(apiExceptionEntityRest)
                    .type(MediaType.APPLICATION_JSON)
                    .build());
            return;
          }
        }
        String valueRequest = headersRequest.getFirst(headerConfig.getField());
        if (Objects.nonNull(valueRequest)) {
          Pattern p = Pattern.compile(headerConfig.getPattern());
          Matcher m = p.matcher(valueRequest);
          boolean b = m.matches();
          log.debug("Header {} value {} pattern {} valid {}", headerConfig.getField(), valueRequest, headerConfig.getPattern(), b);
          if (!b) {
            log.error("Error header " + headerConfig.getField() + " " + valueRequest + " pattern invalid " + headerConfig.getPattern());
            ApiErrorDetail apiErrorDetail = new ApiErrorDetail();
            apiErrorDetail.setComponent("WebRequestHeadersFilter");
            apiErrorDetail.setDescription("Error header " + headerConfig.getField());
            ApiError apiError = new ApiError(ERROR_CODE_HEADER_INVALID,"Error header " + headerConfig.getField() + " pattern invalid " + headerConfig.getPattern(), Response.Status.BAD_REQUEST.getReasonPhrase(), Arrays.asList(apiErrorDetail));

            R9WebFilterException apiExceptionEntityRest = new R9WebFilterException(apiError);
            containerRequestContext.abortWith(Response.status(Response.Status.BAD_REQUEST)
                    .entity(apiExceptionEntityRest)
                    .type(MediaType.APPLICATION_JSON)
                    .build());
            return;
          } else {
            mapHeaders.put(headerConfig.getKey(), valueRequest);
          }
        }

      }
      log.info("Headers valid {}", mapHeaders);
      headersRequest.forEach((key, values) -> {
        if (!mapHeaders.containsKey(key)) {
          mapHeaders.put(key, values.get(0));
        }
      });
      httpDataCurrent.setMapHeaders(mapHeaders);
    }
  }

}
