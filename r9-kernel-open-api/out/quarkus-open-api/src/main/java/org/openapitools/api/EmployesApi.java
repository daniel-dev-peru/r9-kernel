/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
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

import javax.validation.Valid;
import javax.validation.constraints.*;


import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import pe.com.eberos.fm.utils.core.extension.metadata.HttpDataCurrent;

/*

/api/v1/core

*/
//@javax.annotation.Generated(value = "org.raise9.kernel.open.api.QuarkusOpenApiGenerator", date = "2022-03-31T23:12:07.187455500-05:00[America/Bogota]")
//@Validated

//@Api(value = "employes", description = "the employes API")

@Slf4j
@Path("/")
public class EmployesApi {
    

    @Inject
    EmployesApiDelegate employesApiDelegate;


    @Inject
    HttpDataCurrent httpDataCurrent;

    EmployesApiDelegate getDelegate() {
        return employesApiDelegate;
    }

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
     */
    @PUT
    @Path("/employes/core/data")
    @Consumes(value = { "application/json" })
    @Produces(value = { "application/json" })
    public    Uni<Void> createEmplCore2(@HeaderParam(value = "request-id") String requestId,@HeaderParam(value = "transaction-id") String transactionId,@RequestBody @Valid CreateUserRequest createUserRequest ) {
        log.info("createEmplCore2 start");
        return getDelegate().createEmplCore2(requestId, transactionId, createUserRequest);
    }

}
