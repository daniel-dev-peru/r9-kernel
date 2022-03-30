package org.raise9.kernel.utils.core.error;
/*
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class ApplicationExceptionGeneralHandler implements ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception e) {
    log.info("Error on application ");
    log.error("Error on application detail {}" , e.getMessage() , e);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(e.getMessage())
            .build();

  }
}*/