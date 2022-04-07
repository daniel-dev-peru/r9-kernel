package org.raise9.kernel.open.api;

import org.junit.Test;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;

/***
 * This test allows you to easily launch your code generation software under a
 * debugger.
 * Then run this test under debug mode. You will be able to step through your
 * java code
 * and then see the results in the out directory.
 *
 * To experiment with debugging your code generator:
 * 1) Set a break point in R9OpenAPIQuarkusCodeGenerator.java in the
 * postProcessOperationsWithModels() method.
 * 2) To launch this test in Eclipse: right-click | Debug As | JUnit Test
 *
 */
public class R9OpenAPIQuarkusCodeGeneratorTest {

  // use this test to launch you code generator in the debugger.
  // this allows you to easily set break points in MyclientcodegenGenerator.
  @Test
  public void launchCodeGenerator() {

    /*
     * R9OpenAPIQuarkusCodeGenerator quarkusOpenApiGenerator = new
     * R9OpenAPIQuarkusCodeGenerator();
     * quarkusOpenApiGenerator.setAppNameBase("TestApp");
     * 
     * OpenAPI openAPI = new OpenAPIParser()
     * .readLocation(
     * "../../../modules/openapi-generator/src/test/resources/2_0/petstore.yaml",
     * null, new ParseOptions()).getOpenAPI();
     * 
     * ClientOptInput input = new ClientOptInput();
     * input.openAPI(openAPI);
     * input.config(quarkusOpenApiGenerator);
     * 
     * DefaultGenerator generator = new DefaultGenerator();
     * 
     * generator.setGeneratorPropertyDefault(CodegenConstants.MODELS, "false");
     * generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, "false");
     * generator.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, "false");
     * generator.setGeneratorPropertyDefault(CodegenConstants.APIS, "true");
     * generator.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES,
     * "false");
     * generator.opts(input).generate();
     */

    // to understand how the 'openapi-generator-cli' module is using
    // 'CodegenConfigurator', have a look at the 'Generate' class:
    // https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-cli/src/main/java/org/openapitools/codegen/cmd/Generate.java
    final CodegenConfigurator configurator = new CodegenConfigurator()
        .setGeneratorName("quarkus-open-api") // use this codegen library
        // .setInputSpec("src/main/resources/ejemplo.yml") // sample OpenAPI file
        .setInputSpec("src/main/resources/openapi3.yaml") // sample OpenAPI file

        // .setInputSpec("../../../modules/openapi-generator/src/test/resources/2_0/petstore.yaml")
        // // sample OpenAPI file
        // .setInputSpec("https://raw.githubusercontent.com/openapitools/openapi-generator/master/modules/openapi-generator/src/test/resources/2_0/petstore.yaml")
        // // or from the server
        .setOutputDir("out/quarkus-open-api")

        // .addAdditionalProperty("delegatePattern",true)
        // .addAdditionalProperty("reactive",true)
        .addAdditionalProperty("booleanGetterPrefix", "is")
        // .addAdditionalProperty("dateLibrary","java8")
        // .addAdditionalProperty("library","quarkus")
        .addAdditionalProperty("apiPackage", "org.r9.app.api.adapter.input.web")
        .addAdditionalProperty("modelPackage", "org.r9.app.api.adapter.input.model")
        .addAdditionalProperty("configPackage", "org.r9.app.config")
        .addAdditionalProperty("invokerPackage", "org.r9.app")

        .addAdditionalProperty("iteractorPackage", "org.r9.app.api.domain.input.iteractor")
        .addAdditionalProperty("portPackage", "org.r9.app.api.domain.output.port")

        .addAdditionalProperty("appMainClass", "CoreUserApplication")
        .addAdditionalProperty("appPathBase", "/api/v1/core"); // output directory

    final ClientOptInput clientOptInput = configurator.toClientOptInput();

    DefaultGenerator generator = new DefaultGenerator();
    generator.opts(clientOptInput).generate();
  }
}