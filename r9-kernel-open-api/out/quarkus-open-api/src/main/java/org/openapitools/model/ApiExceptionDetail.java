package org.openapitools.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

//import org.openapitools.jackson.nullable.JsonNullable;


import javax.validation.Valid;
import javax.validation.constraints.*;







/**
 * Datos del error tecnico.
 */

@javax.annotation.Generated(value = "org.raise9.kernel.open.api.QuarkusOpenApiGenerator", date = "2022-03-31T06:51:16.451752200-05:00[America/Bogota]")
public class ApiExceptionDetail   {
  @JsonProperty("code")
  private String code;

  @JsonProperty("component")
  private String component;

  @JsonProperty("description")
  private String description;

  public ApiExceptionDetail code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Codigo de error del Detalle/Proveedor
   * @return code
  */



  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ApiExceptionDetail component(String component) {
    this.component = component;
    return this;
  }

  /**
   * Nombre del componente de falla
   * @return component
  */



  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public ApiExceptionDetail description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Descripcion del Detalle
   * @return description
  */



  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiExceptionDetail apiExceptionDetail = (ApiExceptionDetail) o;
    return Objects.equals(this.code, apiExceptionDetail.code) &&
        Objects.equals(this.component, apiExceptionDetail.component) &&
        Objects.equals(this.description, apiExceptionDetail.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, component, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiExceptionDetail {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    component: ").append(toIndentedString(component)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

