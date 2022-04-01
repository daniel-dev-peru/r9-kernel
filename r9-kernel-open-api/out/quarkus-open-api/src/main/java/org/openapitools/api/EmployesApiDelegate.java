package org.openapitools.api;



import javax.ws.rs.*;
import javax.ws.rs.core.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.SseElementType;

import org.openapitools.model.CreateUserRequest;
import org.openapitools.model.ModelApiException;

import javax.validation.Valid;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;


import io.quarkus.arc.DefaultBean;
import javax.enterprise.context.Dependent;

import javax.validation.Valid;
import javax.validation.constraints.*;

import javax.enterprise.context.ApplicationScoped;





@Slf4j
class EmployesApiDelegateConfiguration {

  @Dependent
  @Produces
  @DefaultBean
  public EmployesApiDelegate employesApiDelegate() {
    log.info("Bean EmployesApiDelegate");
    return new EmployesApiDelegate() {
    };
  }

}


/**
 * A delegate to be called by the {@link EmployesApiController}}.
/api/v1/core
 */
@javax.annotation.Generated(value = "org.raise9.kernel.open.api.QuarkusOpenApiGenerator", date = "2022-03-31T23:12:07.187455500-05:00[America/Bogota]")
public interface EmployesApiDelegate {


    /**
     * PUT /employes/core/data : create user
     * Create user
     *
     * @param requestId id de solicitud (optional)
     * @param transactionId id de transaccion completa (optional)
     * @param createUserRequest  (optional)
     * @return User created (status code 201)
     *         or Los datos proporcionados no son validos. (status code 400)
     *         or Unauthorized (status code 401)
     *         or Ocurrio un error inesperado. Por favor contactarse con el Soporte o la transaccion ha expirado. (status code 500)
     * @see EmployesApi#createEmplCore2
     */
    default   Uni<Void> createEmplCore2(String requestId, String transactionId, CreateUserRequest createUserRequest )  {
        Uni<Void> result = Uni.createFrom().voidItem();    
        return result;
    }

}



