package org.raise9.kernel.web.filter;

import java.io.IOException;
import java.util.Arrays;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.microprofile.config.ConfigProvider;
import org.raise9.kernel.utils.core.GenerateCorrelation;

@Slf4j
@Priority(12)
@Provider
@PreMatching
public class ValidateAndAddTransactionIdFilter implements ContainerRequestFilter, ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String
            appName =
            ConfigProvider.getConfig()
                    .getValue("quarkus.application.name", String.class);

    String
            transactionId =
            requestContext.getHeaders()
                    .getFirst("transaction-id");
    if (ObjectUtils.allNull(transactionId)) {
      transactionId = GenerateCorrelation.generate(appName);
    }
    log.debug("ValidateAndAddTransactionIdFilter request: {}", transactionId);
    requestContext.getHeaders()
            .put("transaction-id", Arrays.asList(transactionId));
  }

  @Override
  public void filter(ContainerRequestContext reqContext, ContainerResponseContext containerResponseContext) throws IOException {
    String transactionId = null;
    if (reqContext.getHeaders()
            .containsKey("transaction-id")) {
      transactionId =
              reqContext.getHeaders()
                      .getFirst("transaction-id");
    }
    log.debug("ValidateAndAddTransactionIdFilter response: {}", transactionId);
    containerResponseContext.getHeaders()
            .put("transaction-id", Arrays.asList(transactionId));
  }

}