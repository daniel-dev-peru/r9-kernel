package org.raise9.kernel.open.api;

import org.openapitools.codegen.CodegenParameter;

public abstract class QueryPostProcessor implements ParamsExtensionPostProcessor {
  public String getExtensionName() {
    return ParameterTypeExtensions.QUERY_PARAM.getExtension();
  }


  public void setType(CodegenParameter codegenParameter) {
    codegenParameter.isQueryParam = true;
  }


  public boolean isAllowed(CodegenParameter parameter) {
    return parameter.isQueryParam;
  }

}