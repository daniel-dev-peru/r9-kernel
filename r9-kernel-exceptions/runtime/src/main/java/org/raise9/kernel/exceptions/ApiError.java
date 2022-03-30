package org.raise9.kernel.exceptions;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

  private String code;

  private String description;

  private String errorType;

  private List<ApiErrorDetail> exceptionDetails = null;

}