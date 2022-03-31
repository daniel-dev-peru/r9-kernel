package org.raise9.kernel.web.filter.config;

import lombok.ToString;

@ToString
public class Header {

  private String key;
  private Boolean required;
  private String field;
  private String pattern;

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = ((result * 31) + ((this.field == null) ? 0 : this.field.hashCode()));
    result = ((result * 31) + ((this.required == null) ? 0 : this.required.hashCode()));
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof Header) == false) {
      return false;
    }
    Header rhs = ((Header) other);
    return (((this.field == rhs.field) || ((this.field != null) && this.field.equals(rhs.field))) && ((this.required == rhs.required) || ((this.required != null) && this.required.equals(rhs.required))));
  }

}