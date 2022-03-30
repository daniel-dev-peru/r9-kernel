package org.raise9.kernel.utils.core.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiError {

  @JsonProperty("code")
  private String code;

  @JsonProperty("description")
  private String description;

  @JsonProperty("errorType")
  private String errorType;

  @JsonProperty("exceptionDetails")
  private List<ApiErrorDetail> exceptionDetails = null;

}