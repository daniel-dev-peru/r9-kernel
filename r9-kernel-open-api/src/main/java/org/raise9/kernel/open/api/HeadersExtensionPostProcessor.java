package org.raise9.kernel.open.api;/*    */

import java.util.Map;
import org.openapitools.codegen.CodegenOperation;

public class HeadersExtensionPostProcessor extends HeaderPostProcessor {
  public void processParameters(CodegenOperation operation) {
    super.processParameters(operation);
    this.additionalProperties.put("thereIsAHttpHeadersMapping", Boolean.TRUE);
  }


  public void setAdditionalProperties(Map<String, Object> additionalProperties) {
    this.additionalProperties = additionalProperties;
  }
}