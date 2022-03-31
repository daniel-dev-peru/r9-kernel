package org.raise9.kernel.web.filter.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"required", "headers"})
@ToString
@Generated("jsonschema2pojo")
public class WebRequestHeadersConfig {

  @JsonProperty("required")
  private Boolean required;
  @JsonProperty("headers")
  private List<Header> headers = new ArrayList<>();

  @JsonProperty("required")
  public Boolean getRequired() {
    return required;
  }

  @JsonProperty("required")
  public void setRequired(Boolean required) {
    this.required = required;
  }

  @JsonProperty("headers")
  public List<Header> getHeaders() {
    return headers;
  }

  @JsonProperty("headers")
  public void setHeaders(List<Header> headers) {
    this.headers = headers;
  }


  @Override
  public int hashCode() {
    int result = 1;
    result = ((result * 31) + ((this.headers == null) ? 0 : this.headers.hashCode()));
    result = ((result * 31) + ((this.required == null) ? 0 : this.required.hashCode()));
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof WebRequestHeadersConfig) == false) {
      return false;
    }
    WebRequestHeadersConfig rhs = ((WebRequestHeadersConfig) other);
    return (((this.headers == rhs.headers) || ((this.headers != null) && this.headers.equals(rhs.headers))) && ((this.required == rhs.required) || ((this.required != null) && this.required.equals(rhs.required))));
  }

}