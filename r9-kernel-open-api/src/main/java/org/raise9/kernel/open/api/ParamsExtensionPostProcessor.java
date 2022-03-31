package org.raise9.kernel.open.api;/*    */

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ParamsExtensionPostProcessor
/*    */   extends ParamsPostProcessor
/*    */ {
/*    */   String getExtensionName();
/*    */   
/*    */   default String getClassName(String refValue) {
/* 42 */     return refValue.substring(refValue.lastIndexOf('/') + 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default String getRefValue(CodegenOperation operation) {
/* 53 */     return (String)((HashMap)operation.vendorExtensions.get(getExtensionName())).get("$ref");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default String getObjectName(String className) {
/* 62 */     return className.substring(0, 1).toLowerCase(Locale.getDefault()) + className.substring(0, 1).toLowerCase(Locale.getDefault());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default void processParameters(CodegenOperation operation) {
/* 75 */     List<CodegenParameter> processableParams = filterParametersByType(operation);
/*    */     
/* 77 */     if (processableParams.isEmpty()) {
/* 78 */       throw new IllegalArgumentException(MessageFormat.format("Declared extension: {0} has not parameters to process.", new Object[] {
/* 79 */               getExtensionName()
/*    */             }));
/*    */     }
/* 82 */     operation.allParams.removeAll(processableParams);
/* 83 */     String refValue = getRefValue(operation);
/* 84 */     String className = getClassName(refValue);
/* 85 */     String objectName = getObjectName(className);
/* 86 */     replaceWithNewGroupedCodegenParameter(operation, buildCodeGenParameter(true, true, className, objectName, objectName));
/*    */   }
/*    */ }