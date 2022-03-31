package org.raise9.kernel.open.api;/*    */

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

public interface ParamsPostProcessor {
  void processParameters(CodegenOperation paramCodegenOperation);

  default List<CodegenParameter> filterParametersByType(CodegenOperation operation) {
    return operation.allParams

            .stream()
            .filter(this::isAllowed)
            .collect(Collectors.toList());
  }

  default CodegenParameter buildCodeGenParameter(boolean hasMore, boolean required, String dataType, String baseName, String paramName) {
    CodegenParameter newCodParam = new CodegenParameter();
    //newCodParam.hasMore = hasMore;
    newCodParam.required = required;
    newCodParam.dataType = dataType;
    newCodParam.baseName = baseName;
    newCodParam.paramName = paramName;
    return newCodParam;
  }

  void setType(CodegenParameter paramCodegenParameter);

  boolean isAllowed(CodegenParameter paramCodegenParameter);

  default void replaceWithNewGroupedCodegenParameter(CodegenOperation operation, CodegenParameter newCodParam) {
    setType(newCodParam);
    operation.allParams.add(0, newCodParam);
  }

  default void setAdditionalProperties(Map<String, Object> additionalProperties) {
  }
}