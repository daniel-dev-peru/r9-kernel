package org.raise9.kernel.open.api;

import java.util.LinkedHashMap;
import java.util.Map;
import org.openapitools.codegen.CodegenParameter;

public abstract class HeaderPostProcessor implements ParamsExtensionPostProcessor {
  Map<String, Object> additionalProperties = new LinkedHashMap<>();

  public String getExtensionName() {
    return ParameterTypeExtensions.HEADER_PARAM.getExtension();
  }

  public void setType(CodegenParameter codegenParameter) {
    codegenParameter.isHeaderParam = true;
  }

  public boolean isAllowed(CodegenParameter parameter) {
    return parameter.isHeaderParam;
  }
}