package org.raise9.kernel.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiErrorDetail {

  private String code;

  private String component;

  private String description;

}