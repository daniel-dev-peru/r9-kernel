package org.raise9.kernel.web.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

@Slf4j
@Priority(11)
@Provider
@PreMatching
public class LogFilter implements ContainerRequestFilter, ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext reqContext) throws IOException {
    UriInfo uriInfo = reqContext.getUriInfo();
    logData(uriInfo, reqContext, null);
  }

  @Override
  public void filter(ContainerRequestContext reqContext, ContainerResponseContext resContext) throws IOException {
    UriInfo uriInfo = reqContext.getUriInfo();
    logData(uriInfo, reqContext, resContext);
  }

  private void logData(UriInfo uriInfo, ContainerRequestContext reqContext, ContainerResponseContext resContext) {
    boolean isRequest = false;
    if (Objects.isNull(resContext)) {
      isRequest = true;
    }
    String requestId = null;
    if (reqContext.getHeaders()
            .containsKey("request-id")) {
      requestId =
              reqContext.getHeaders()
                      .getFirst("request-id");
    }
    String
            path =
            uriInfo.getAbsolutePath()
                    .getPath();

    if (isRequest) {
      log.info("Http connection type: request - request-id : {} route path: {} - method: {} - readers : {} debug: {}", requestId, path, reqContext.getMethod(), reqContext.getHeaders()
              .toString(), log.isDebugEnabled());

      if (log.isDebugEnabled()) {
        if (reqContext.hasEntity()) {
          try {
            byte[] content = IOUtils.toByteArray(reqContext.getEntityStream());
            String body = new String(content, StandardCharsets.UTF_8);
            InputStream secondClone = new ByteArrayInputStream(content);
            log.debug("Request Request-id 2 : {} - body : {}", requestId, body);
            reqContext.setEntityStream(secondClone);
          } catch (IOException e) {
            log.error("Error get data on request : {} ", requestId, e);
          }
        }
      }
    } else {


      boolean hasEntity = false;
      if (log.isDebugEnabled()) {
        try {
          hasEntity = resContext.hasEntity();
          if (hasEntity) {

            OutputStream stream = resContext.getEntityStream();
            if (stream instanceof ByteArrayOutputStream) {
              ByteArrayOutputStream out = (ByteArrayOutputStream) stream;
              byte[] content = out.toByteArray();
              String body = new String(content, StandardCharsets.UTF_8);
              OutputStream secondClone = new ByteArrayOutputStream();
              out.write(content);
              log.debug("Response Request-id : {} - body : {}", requestId, body);
              resContext.setEntityStream(secondClone);
            }else{
              log.debug("Response Request-id : {} - type class : {}", requestId, stream.getClass());
            }
          }
        } catch (Exception e) {
          log.error("Error get data on response : {} ", requestId, e);
        }
      }

      log.info("Http connection type: response - request-id : {} route path: {} - method: {} - status: {} - readers : {}  hasEntity: {} debug: {}", requestId, path, reqContext.getMethod(), resContext.getStatus(), resContext.getHeaders()
              .toString(), hasEntity, log.isDebugEnabled());

    }
  }

}