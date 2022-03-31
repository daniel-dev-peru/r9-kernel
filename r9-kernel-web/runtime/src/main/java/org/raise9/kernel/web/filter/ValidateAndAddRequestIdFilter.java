package org.raise9.kernel.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
@Priority(10)
@Provider
@PreMatching
public class ValidateAndAddRequestIdFilter implements ContainerRequestFilter, ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String requestId = requestContext.getHeaders().getFirst("request-id");
    if (ObjectUtils.allNull(requestId)) {
      UUID uuid = UUID.randomUUID();
      requestId = uuid.toString();
    }
    log.debug("ValidateAndAddRequestIdFilter request: {}", requestId);
    requestContext.getHeaders().put("request-id", Arrays.asList(requestId));
  }

  @Override
  public void filter(ContainerRequestContext reqContext, ContainerResponseContext containerResponseContext) throws IOException {
    String requestId = null;
    if (reqContext.getHeaders()
            .containsKey("request-id")) {
      requestId =
              reqContext.getHeaders()
                      .getFirst("request-id");
    }
    log.debug("ValidateAndAddRequestIdFilter response: {}", requestId);
    containerResponseContext.getHeaders().put("request-id", Arrays.asList(requestId));
  }



}