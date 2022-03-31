package org.openapitools.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.model.ApiExceptionDetail;

//import org.openapitools.jackson.nullable.JsonNullable;


import javax.validation.Valid;
import javax.validation.constraints.*;







/**
 * Datos del error de sistema.
 */

@javax.annotation.Generated(value = "org.raise9.kernel.open.api.QuarkusOpenApiGenerator", date = "2022-03-31T06:51:16.451752200-05:00[America/Bogota]")
public class ModelApiException   {
  @JsonProperty("code")
  private String code;

  @JsonProperty("description")
  private String description;

  @JsonProperty("errorType")
  private String errorType;

  @JsonProperty("exceptionDetails")
  @Valid
  private List<ApiExceptionDetail> exceptionDetails = null;

  public ModelApiException code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Codigo de error de Sistema
   * @return code
  */



  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ModelApiException description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Descripcion del error de Sistema
   * @return description
  */



  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ModelApiException errorType(String errorType) {
    this.errorType = errorType;
    return this;
  }

  /**
   * Tipo de Error de Sistema
   * @return errorType
  */



  public String getErrorType() {
    return errorType;
  }

  public void setErrorType(String errorType) {
    this.errorType = errorType;
  }

  public ModelApiException exceptionDetails(List<ApiExceptionDetail> exceptionDetails) {
    this.exceptionDetails = exceptionDetails;
    return this;
  }

  public ModelApiException addExceptionDetailsItem(ApiExceptionDetail exceptionDetailsItem) {
    if (this.exceptionDetails == null) {
      this.exceptionDetails = new ArrayList<>();
    }
    this.exceptionDetails.add(exceptionDetailsItem);
    return this;
  }

  /**
   * Lista de detalles del error
   * @return exceptionDetails
  */


  @Valid

  public List<ApiExceptionDetail> getExceptionDetails() {
    return exceptionDetails;
  }

  public void setExceptionDetails(List<ApiExceptionDetail> exceptionDetails) {
    this.exceptionDetails = exceptionDetails;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelApiException _apiException = (ModelApiException) o;
    return Objects.equals(this.code, _apiException.code) &&
        Objects.equals(this.description, _apiException.description) &&
        Objects.equals(this.errorType, _apiException.errorType) &&
        Objects.equals(this.exceptionDetails, _apiException.exceptionDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, description, errorType, exceptionDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelApiException {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    errorType: ").append(toIndentedString(errorType)).append("\n");
    sb.append("    exceptionDetails: ").append(toIndentedString(exceptionDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

