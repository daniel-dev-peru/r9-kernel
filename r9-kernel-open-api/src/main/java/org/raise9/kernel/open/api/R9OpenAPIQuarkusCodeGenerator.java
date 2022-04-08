package org.raise9.kernel.open.api;

import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.codegen.CliOption;
import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;
import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.TemplateManager;
import org.openapitools.codegen.api.TemplatePathLocator;
import org.openapitools.codegen.languages.AbstractJavaCodegen;
import org.openapitools.codegen.languages.features.BeanValidationFeatures;
import org.openapitools.codegen.languages.features.PerformBeanValidationFeatures;
import org.openapitools.codegen.templating.HandlebarsEngineAdapter;
import org.openapitools.codegen.templating.MustacheEngineAdapter;
import org.openapitools.codegen.templating.TemplateManagerOptions;
import org.openapitools.codegen.utils.StringUtils;
import org.openapitools.codegen.utils.URLPathUtils;

@Getter
@Setter
@Slf4j
public class R9OpenAPIQuarkusCodeGenerator extends AbstractJavaCodegen implements BeanValidationFeatures, PerformBeanValidationFeatures {
  public static final String TITLE = "title";
  public static final String SERVER_PORT = "serverPort";
  public static final String APP_NAME_QUARKUS = "appMainClass";
  public static final String APP_PATH_BASE = "appPathBase";
  public static final String CONFIG_PACKAGE = "configPackage";
  public static final String BASE_PACKAGE = "basePackage";
  public static final String INTERFACE_ONLY = "interfaceOnly";
  public static final String DELEGATE_PATTERN = "delegatePattern";
  public static final String SINGLE_CONTENT_TYPES = "singleContentTypes";
  public static final String VIRTUAL_SERVICE = "virtualService";
  public static final String SKIP_DEFAULT_INTERFACE = "skipDefaultInterface";

  public static final String ASYNC = "async";
  public static final String REACTIVE = "reactive";
  public static final String USE_TAGS = "useTags";
  public static final String QUARKUS_LIBRARY = "quarkus";
  public static final String IMPLICIT_HEADERS = "implicitHeaders";
  public static final String API_FIRST = "apiFirst";
  public static final String RETURN_SUCCESS_CODE = "returnSuccessCode";
  public static final String UNHANDLED_EXCEPTION_HANDLING = "unhandledException";
  public static final String OPEN_BRACE = "{";
  public static final String CLOSE_BRACE = "}";

  protected boolean delegatePattern = false;
  protected boolean delegateMethod = false;
  protected boolean reactive = false;
  protected boolean interfaceOnly = false;
  protected boolean singleContentTypes = false;
  protected boolean skipDefaultInterface = false;


  public static final String DATA_BASE = "database";
  protected boolean database = false;
  public static final String PORT_PACKAGE = "portPackage";
  protected String portPackage = "org.openapitools.domain.port";
  public static final String PORT_PACKAGE_IMPL = "portPackageImpl";
  protected String portImplPackage = "org.openapitools.domain.port";

  protected boolean useBeanValidation = true;
  protected boolean performBeanValidation = false;

  protected String title = "R9 OpenAPI Quarkus";
  protected String appNameBase = "QuarkusApplication";
  protected String appPathBase = "/core/api/v1/services";

  protected String configPackage = "org.openapitools.configuration";
  protected String invokePackage = "org.openapitools";
  protected String basePackage = "org.openapitools";


  public R9OpenAPIQuarkusCodeGenerator() {
    super();

    /*modifyFeatureSet(features ->
            features.includeDocumentationFeatures(DocumentationFeature.Readme)
                    .wireFormatFeatures(EnumSet.of(WireFormatFeature.JSON, WireFormatFeature.XML, WireFormatFeature.Custom))
                    .securityFeatures(EnumSet.of(SecurityFeature.OAuth2_Implicit, SecurityFeature.OAuth2_AuthorizationCode,
                            SecurityFeature.OAuth2_ClientCredentials, SecurityFeature.OAuth2_Password, SecurityFeature.ApiKey,
                            SecurityFeature.BasicAuth))
                    .excludeGlobalFeatures(GlobalFeature.Callbacks, GlobalFeature.LinkObjects, GlobalFeature.ParameterStyling)
                    .includeGlobalFeatures(
            GlobalFeature.XMLStructureDefinitions).includeSchemaSupportFeatures(SchemaSupportFeature.Polymorphism)
                    .excludeParameterFeatures(ParameterFeature.Cookie));*/

    outputFolder = "generated-code/quarkus-open-api";

    embeddedTemplateDir = "quarkus-open-api";

    apiPackage = "org.openapitools.api";
    modelPackage = "org.openapitools.model";
    invokerPackage = "org.openapitools.api";
    artifactId = "openapi-quarkus";

    updateOption(CodegenConstants.INVOKER_PACKAGE, this.getInvokerPackage());
    updateOption(CodegenConstants.ARTIFACT_ID, this.getArtifactId());
    updateOption(CodegenConstants.API_PACKAGE, apiPackage);
    updateOption(CodegenConstants.MODEL_PACKAGE, modelPackage);

    modelDocTemplateFiles.remove("model_doc.mustache");
    apiDocTemplateFiles.remove("api_doc.mustache");

    apiTestTemplateFiles.clear();

    additionalProperties.put(JACKSON, "true");
    additionalProperties.put("openbrace", OPEN_BRACE);
    additionalProperties.put("closebrace", CLOSE_BRACE);

    cliOptions.add(new CliOption(TITLE, "server title name or client service name").defaultValue(title));
    cliOptions.add(new CliOption(CONFIG_PACKAGE, "configuration package for generated code").defaultValue(this.getConfigPackage()));
    cliOptions.add(new CliOption(BASE_PACKAGE, "base package (invokerPackage) for generated code").defaultValue(this.getBasePackage()));
    cliOptions.add(CliOption.newBoolean(DELEGATE_PATTERN, "Whether to generate the server files using the delegate pattern", delegatePattern));
    cliOptions.add(new CliOption(APP_NAME_QUARKUS, "Name class main of quarkus").defaultValue(appNameBase));
    cliOptions.add(new CliOption(APP_PATH_BASE, "Path Base quarkus app").defaultValue(appPathBase));
    cliOptions.add(CliOption.newBoolean(SKIP_DEFAULT_INTERFACE, "Whether to generate default implementations for java8 interfaces", skipDefaultInterface));
    cliOptions.add(CliOption.newBoolean(REACTIVE, "wrap responses in Uni/Multi smallrye types (quarkus only)", reactive));
    cliOptions.add(CliOption.newBoolean(VIRTUAL_SERVICE, "Generates the virtual service. For more details refer - https://github.com/virtualansoftware/virtualan/wiki"));
    cliOptions.add(CliOption.newBoolean(USE_BEANVALIDATION, "Use BeanValidation API annotations", useBeanValidation));
    cliOptions.add(CliOption.newBoolean(PERFORM_BEANVALIDATION, "Use Bean Validation Impl. to perform BeanValidation", performBeanValidation));

    supportedLibraries.put(QUARKUS_LIBRARY, "Quarkus Server application.");

    cliOptions.add(CliOption.newBoolean(DATA_BASE, "wrap database context", database));
    cliOptions.add(new CliOption(PORT_PACKAGE, "wrap port context").defaultValue(portPackage));
    cliOptions.add(new CliOption(PORT_PACKAGE_IMPL, "wrap port db impl context").defaultValue(portImplPackage));

    setLibrary(QUARKUS_LIBRARY);

    CliOption library = new CliOption(CodegenConstants.LIBRARY, CodegenConstants.LIBRARY_DESC).defaultValue(QUARKUS_LIBRARY);
    library.setEnum(supportedLibraries);
    cliOptions.add(library);
    cliOptions.add(new CliOption(APP_NAME_QUARKUS, "Name class main of quarkus").defaultValue(appNameBase));
    cliOptions.add(new CliOption(APP_PATH_BASE, "Path Base quarkus app").defaultValue(appPathBase));

    cliOptions.add(CliOption.newBoolean(REACTIVE, "wrap responses in Uni/Multi smallrye types (quarkus only)", reactive));

    /*
    //cliOptions.add(CliOption.newBoolean(USE_TAGS, "use tags for creating interface and controller classnames", useTags));
    //cliOptions.add(CliOption.newBoolean(USE_BEANVALIDATION, "Use BeanValidation API annotations", useBeanValidation));
    //cliOptions.add(CliOption.newBoolean(PERFORM_BEANVALIDATION, "Use Bean Validation Impl. to perform BeanValidation", performBeanValidation));
    //cliOptions.add(CliOption.newBoolean(IMPLICIT_HEADERS, "Skip header parameters in the generated API methods using @ApiImplicitParams annotation.", implicitHeaders));
    //cliOptions.add(CliOption.newBoolean(API_FIRST, "Generate the API from the OAI spec at server compile time (API first approach)", apiFirst));
    //cliOptions.add(CliOption.newBoolean(USE_OPTIONAL, "Use Optional container for optional parameters", useOptional));
    //cliOptions.add(CliOption.newBoolean(RETURN_SUCCESS_CODE, "Generated server returns 2xx code", returnSuccessCode));
    //cliOptions.add(CliOption.newBoolean(UNHANDLED_EXCEPTION_HANDLING, "Declare operation methods to throw a generic exception and allow unhandled exceptions (useful for Quarkus `@ServerExceptionMapper` directives).", unhandledException));
    apiTestTemplateFiles.clear();
    additionalProperties.put(JACKSON, "true");
    additionalProperties.put("openbrace", OPEN_BRACE);
    additionalProperties.put("closebrace", CLOSE_BRACE);
    if (additionalProperties.containsKey(DELEGATE_PATTERN)) {
      this.setDelegatePattern(Boolean.parseBoolean(additionalProperties.get(DELEGATE_PATTERN).toString()));
    }
    */
  }

  @Override
  public void processOpts() {
    log.info("Init processOpts invokePackage {} | apiPackage {} | basePackage {} | modelPackage {}", invokePackage, apiPackage, basePackage, modelPackage);
    additionalProperties.put("jdk8-default-interface", !this.skipDefaultInterface);
    this.setBasePackage((String) additionalProperties.get(CodegenConstants.INVOKER_PACKAGE));
    if (additionalProperties.containsKey(BASE_PACKAGE)) {
      this.setBasePackage((String) additionalProperties.get(BASE_PACKAGE));
    } else {
      additionalProperties.put(BASE_PACKAGE, basePackage);
    }
    if (additionalProperties.containsKey(CONFIG_PACKAGE)) {
      this.setConfigPackage((String) additionalProperties.get(CONFIG_PACKAGE));
    } else {
      additionalProperties.put(CONFIG_PACKAGE, configPackage);
    }
    this.interfaceOnly = false;
    if (!this.interfaceOnly) {
      if (QUARKUS_LIBRARY.equals(library)) {
        if (additionalProperties.containsKey(APP_NAME_QUARKUS)) {
          String nameApp = (String) additionalProperties.get(APP_NAME_QUARKUS);
          supportingFiles.add(new SupportingFile("openapiQuarkus.mustache", (sourceFolder + File.separator + basePackage).replace(".", File.separator), nameApp + ".java"));
        } else {
          supportingFiles.add(new SupportingFile("openapiQuarkus.mustache", (sourceFolder + File.separator + basePackage).replace(".", File.separator), "QuarkusApplication.java"));
        }
        supportingFiles.add(new SupportingFile("application.mustache", ("src.main.resources").replace(".", File.separator), "application.properties"));
        supportingFiles.add(new SupportingFile("homeController.mustache", (sourceFolder + File.separator + configPackage).replace(".", File.separator), "HomeController.java"));
      }
    }

    additionalProperties.put("isDelegate", "true");
    apiTemplateFiles.put("apiDelegate.mustache", "Delegate.java");
    //apiTemplateFiles.put("services.mustache", "Service.java");
   /* log.info("----------------------------------");
    List<Pair<String, String>> configOptions = additionalProperties.entrySet().stream().filter(e -> !Arrays.asList(API_FIRST, "hideGenerationTimestamp").contains(e.getKey())).filter(e -> cliOptions.stream().map(CliOption::getOpt).anyMatch(opt -> opt.equals(e.getKey()))).map(e -> Pair.of(e.getKey(), e.getValue().toString())).collect(Collectors.toList());
    additionalProperties.put(BASE_PACKAGE, basePackage);
    log.info("Set base package to invoker package ({})", basePackage);
    if (additionalProperties.containsKey(BASE_PACKAGE)) {
      this.setBasePackage((String) additionalProperties.get(BASE_PACKAGE));
    } else {
      additionalProperties.put(BASE_PACKAGE, basePackage);
    }
    additionalProperties.put("configOptions", configOptions);
    log.info("configOptions {}", configOptions);
    log.info("BASE_PACKAGE {}", additionalProperties.get(BASE_PACKAGE));
    log.info("INVOKER_PACKAGE {}", additionalProperties.get(CodegenConstants.INVOKER_PACKAGE));
    log.info("----------------------------------");
    if (!additionalProperties.containsKey(BASE_PACKAGE) && additionalProperties.containsKey(CodegenConstants.INVOKER_PACKAGE)) {
      this.setBasePackage((String) additionalProperties.get(CodegenConstants.INVOKER_PACKAGE));
      additionalProperties.put(BASE_PACKAGE, basePackage);
      log.info("Set base package to invoker package ({})", basePackage);
    }
    log.info("----------------------------------");
    if (additionalProperties.containsKey(BASE_PACKAGE)) {
      this.setBasePackage((String) additionalProperties.get(BASE_PACKAGE));
    } else {
      additionalProperties.put(BASE_PACKAGE, basePackage);
    }
    log.info("----------------------------------");
    if (additionalProperties.containsKey(TITLE)) {
      this.setTitle((String) additionalProperties.get(TITLE));
    }
    if (additionalProperties.containsKey(CONFIG_PACKAGE)) {
      this.setConfigPackage((String) additionalProperties.get(CONFIG_PACKAGE));
    } else {
      additionalProperties.put(CONFIG_PACKAGE, configPackage);
    }
    if (additionalProperties.containsKey(APP_NAME_QUARKUS)) {
      this.setAppNameBase((String) additionalProperties.get(APP_NAME_QUARKUS));
      log.info("APP_NAME_QUARKUS {}", this.getAppNameBase());
    } else {
      additionalProperties.put(APP_NAME_QUARKUS, appNameBase);
    }
    if (additionalProperties.containsKey(APP_PATH_BASE)) {
      this.setAppPathBase((String) additionalProperties.get(APP_PATH_BASE));
      log.info("APP_PATH_BASE {}", this.getAppPathBase());
    } else {
      additionalProperties.put(APP_PATH_BASE, appPathBase);
    }
    if (additionalProperties.containsKey(CONFIG_PACKAGE)) {
      this.setConfigPackage((String) additionalProperties.get(CONFIG_PACKAGE));
    } else {
      additionalProperties.put(CONFIG_PACKAGE, configPackage);
    }
    if (additionalProperties.containsKey(BASE_PACKAGE)) {
      this.setBasePackage((String) additionalProperties.get(BASE_PACKAGE));
    } else {
      additionalProperties.put(BASE_PACKAGE, basePackage);
    }
    if (additionalProperties.containsKey(DELEGATE_PATTERN)) {
      this.setDelegatePattern(Boolean.parseBoolean(additionalProperties.get(DELEGATE_PATTERN).toString()));
    }
    if (additionalProperties.containsKey(SINGLE_CONTENT_TYPES)) {
      this.setSingleContentTypes(Boolean.parseBoolean(additionalProperties.get(SINGLE_CONTENT_TYPES).toString()));
    }
    if (additionalProperties.containsKey(SKIP_DEFAULT_INTERFACE)) {
      this.setSkipDefaultInterface(Boolean.parseBoolean(additionalProperties.get(SKIP_DEFAULT_INTERFACE).toString()));
    }
    supportingFiles.add(new SupportingFile("application.mustache", ("src.main.resources").replace(".", File.separator), "application.properties"));
    supportingFiles.add(new SupportingFile("homeController.mustache", (sourceFolder + File.separator + configPackage).replace(".", File.separator), "HomeController.java"));
    additionalProperties.put("delegate-method", true);
    additionalProperties.put("isDelegate", "true");
    apiTemplateFiles.put("apiDelegate.mustache", "Delegate.java");
    additionalProperties.put("jdk8-default-interface", true);
    additionalProperties.put("lambdaRemoveDoubleQuote", (Mustache.Lambda) (fragment, writer) -> writer.write(fragment.execute().replaceAll("\"", Matcher.quoteReplacement(""))));
    additionalProperties.put("lambdaEscapeDoubleQuote", (Mustache.Lambda) (fragment, writer) -> writer.write(fragment.execute().replaceAll("\"", Matcher.quoteReplacement("\\\""))));
    additionalProperties.put("lambdaRemoveLineBreak", (Mustache.Lambda) (fragment, writer) -> writer.write(fragment.execute().replaceAll("\\r|\\n", "")));
    additionalProperties.put("lambdaTrimWhitespace", new TrimWhitespaceLambda());
    additionalProperties.put("lambdaSplitString", new SplitStringLambda());
    additionalProperties.put("jdk8-default-interface", true);
*/
    super.processOpts();
/*
    if (additionalProperties.containsKey(APP_NAME_QUARKUS)) {
      //additionalProperties.put("basePackage",basePackage);
      String nameApp = (String) additionalProperties.get(APP_NAME_QUARKUS);
      supportingFiles.add(new SupportingFile("openapiQuarkus.mustache", (sourceFolder + File.separator + basePackage).replace(".", File.separator), nameApp + ".java"));
    } else {
      supportingFiles.add(new SupportingFile("openapiQuarkus.mustache", (sourceFolder + File.separator + basePackage).replace(".", File.separator), "QuarkusApplication.java"));
    }
*/
    log.info("Finish processOpts invokePackage {} apiPackage {} basePackage {} modelPackage {}", invokePackage, apiPackage, basePackage, modelPackage);

  }

  @Override
  public CodegenType getTag() {
    return CodegenType.SERVER;
  }

  @Override
  public String getName() {
    return "quarkus-open-api";
  }

  @Override
  public String getHelp() {
    return "Generates a Java Quarkus Server application using the OpenApi.";
  }

  @Override
  public CodegenModel fromModel(String name, Schema model) {
    CodegenModel ms = super.fromModel(name,model);
    Set<String> imports = ms.imports.stream().filter(s -> !(s.equals("ApiModel")||s.equals("ApiModelProperty"))).collect(Collectors.toSet());
    ms.imports = imports;
    return ms;
  }

  @Override
  public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
    super.postProcessModelProperty(model,property);
    Set<String> imports = model.imports.stream().filter(s -> !(s.equals("ApiModel")||s.equals("ApiModelProperty"))).collect(Collectors.toSet());
    model.imports = imports;
  }

  private List<Operation> getOperations(OpenAPI openAPI) {
    return openAPI.getPaths().values().stream().filter(Objects::nonNull).flatMap(this::getAllPossibleOperations).filter(Objects::nonNull).collect(Collectors.toList());
  }

  private Stream<Operation> getAllPossibleOperations(PathItem pathItem) {
    return Stream.of(pathItem.getGet(), pathItem.getPost(), pathItem.getDelete(), pathItem.getHead(), pathItem.getOptions(), pathItem.getPatch(), pathItem.getPut(), pathItem.getTrace());
  }

  private final HandlebarsEngineAdapter handlebarsEngineAdapter = new HandlebarsEngineAdapter();
  private final MustacheEngineAdapter mustacheEngineAdapter = new MustacheEngineAdapter();
  private final TemplatePathLocator locator = new ResourceTemplateLoader();
  static class ResourceTemplateLoader implements TemplatePathLocator {
    @Override
    public String getFullTemplatePath(String relativeTemplateFile) {
      log.info("ResourceTemplateLoader relativeTemplateFile : {}" ,relativeTemplateFile);
      return Paths.get("quarkus-open-api","", relativeTemplateFile).toString();
    }
  }

  @Override
  public void preprocessOpenAPI(OpenAPI openAPI) {

    getOperations(openAPI).forEach(operation -> {
      Map<String, Object> extesions = operation.getExtensions();
      for (Map.Entry<String,Object> m :extesions.entrySet()  ) {
        if(m.getKey().equals("x-java-r9-header")){
          log.info("x-java-r9-header active on operation {}" , operation.getOperationId());
          Map<String, Schema> getSchemas = openAPI.getComponents().getSchemas();
          for (Map.Entry<String, Schema> c :getSchemas.entrySet() ) {
            if(c.getKey().contains("Header")){
              Map<String, Object> getSchemasTemp =null;
              if(null == c.getValue().getExtensions()){
                getSchemasTemp = new HashMap<>();
              }else{
                getSchemasTemp = c.getValue().getExtensions();
              }
              getSchemasTemp.put("x-java-r9-header","true");
              c.getValue().setExtensions(getSchemasTemp);
              log.info("Detect Custom Header : {}", c.getKey() + " extensions "+ c.getValue().getExtensions());
            }
          }
        }
      }
    });
    super.preprocessOpenAPI(openAPI);
    if (!additionalProperties.containsKey(TITLE)) {
      String title = openAPI.getInfo().getTitle();
      if (title != null) {
        title = title.trim().replace(" ", "-");
        if (title.toUpperCase(Locale.ROOT).endsWith("API")) {
          title = title.substring(0, title.length() - 3);
        }
        this.title = StringUtils.camelize(sanitizeName(title), true);
      }
      additionalProperties.put(TITLE, this.title);
    }
    if (!additionalProperties.containsKey(SERVER_PORT)) {
      URL url = URLPathUtils.getServerURL(openAPI, serverVariableOverrides());
      this.additionalProperties.put(SERVER_PORT, URLPathUtils.getPort(url, 8080));
    }
/*
    if (openAPI.getPaths() != null) {
      for (Map.Entry<String, PathItem> openAPIGetPathsEntry : openAPI.getPaths().entrySet()) {
        // String pathname = openAPIGetPathsEntry.getKey();
        //LOGGER.info("preprocessOpenAPI pathnamee : {}", openAPIGetPathsEntry.getKey());
        PathItem path = openAPIGetPathsEntry.getValue();
        if (path.readOperations() != null) {
          for (Operation operation : path.readOperations()) {
            //LOGGER.info("preprocessOpenAPI operation : {}", operation.getDescription());
            if (operation.getTags() != null) {
              List<Map<String, String>> tags = new ArrayList<>();
              for (String tag : operation.getTags()) {
                //LOGGER.info("preprocessOpenAPI operation : {} | tag : {}", operation.getDescription(), tag);
                Map<String, String> value = new HashMap<>();
                value.put("tag", tag);
                tags.add(value);
              }
              if (operation.getTags().size() > 0) {
                String tag = operation.getTags().get(0);
                //LOGGER.info("preprocessOpenAPI operation : {} > tag : {}", operation.getDescription(), tag);
                operation.setTags(Arrays.asList(tag));
              }
              operation.addExtension("x-tags", tags);
            }
          }
        }
      }
    }*/
  }

  @Override
  public Map<String, Object> postProcessOperationsWithModels(Map<String, Object> objs, List<Object> allModels) {



    Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
    if (operations != null) {
      List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");
      for (final CodegenOperation operation : ops) {
        log.info("->  operation summary {} - returnType {}", operation.summary , operation.returnType);
        List<CodegenResponse> responses = operation.responses;
        if (responses != null) {
          for (final CodegenResponse resp : responses) {
            if ("0".equals(resp.code)) {
              resp.code = "200";
            }
            doDataTypeAssignment(resp.dataType, new DataTypeAssigner() {
              @Override
              public void setReturnType(final String returnType) {
                resp.dataType = returnType;
              }
              @Override
              public void setReturnContainer(final String returnContainer) {
                resp.containerType = returnContainer;
              }
            });
          }
        }
        doDataTypeAssignment(operation.returnType, new DataTypeAssigner() {
          @Override
          public void setReturnType(final String returnType) {
            operation.returnType = returnType;
          }
          @Override
          public void setReturnContainer(final String returnContainer) {
            operation.returnContainer = returnContainer;
          }
        });
        if(operation.getHasVendorExtensions()){
          Map<String, Object> vendorExtensions = operation.vendorExtensions;
          List<CodegenParameter> parameters = new ArrayList<>();
          if (vendorExtensions != null) {
            for (Map.Entry<String,Object> m :vendorExtensions.entrySet()  ) {
              if (m.getKey().equals("x-java-r9-header")) {
                String className = String.valueOf(m.getValue());
                className = className.substring(className.lastIndexOf('/')+1,className.length()-1);
                Schema atlasSchema = openAPI.getComponents().getSchemas().get(className);
                CodegenModel ms = super.fromModel(className, atlasSchema);
                CodegenParameter newCodParam = new CodegenParameter();
                newCodParam.required = true;
                newCodParam.isHeaderParam = false;
                newCodParam.dataType = ms.classname;
                newCodParam.baseName = ms.classname;
                newCodParam.paramName = ms.classVarName;
                parameters.add(newCodParam);
              }
            }
            if(!parameters.isEmpty()){
              parameters.forEach(ms -> {
                operation.vendorExtensions.put("x-java-r9-header", "");
                operation.vendorExtensions.put("x-java-r9-header-instance", modelPackage + "." + ms.baseName + " " + ms.paramName);
                operation.vendorExtensions.put("x-java-r9-header-param", ms.paramName);
                operation.vendorExtensions.put("x-java-r9-header-value", " = httpDataCurrent.getHeadersFromRequest(" + modelPackage + "." +  ms.baseName + ".class);");
              });
              List<CodegenParameter> allParams = operation.allParams;
              List<CodegenParameter> allParamsTemp = new ArrayList<>();
              for (CodegenParameter c:allParams  ) {
                if(!c.isHeaderParam){
                  allParamsTemp.add(c);
                }
              }
              allParams.clear();
              allParams.addAll(allParamsTemp);
            }
          }
        }
      }
    }


    Map<String, Object> extesionsParent = openAPI.getExtensions();
    if(null != extesionsParent){
      for (Map.Entry<String,Object> m : extesionsParent.entrySet()  ) {
        //getOperations(openAPI).forEach(operation -> {

        // Map<String, Object> operations = (Map<String, Object>) objs.get("operations");

        List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");
        for (final CodegenOperation operation : ops) {
          log.info("->  operation summary {} - returnType {}", operation.summary, operation.returnType);

          if(m.getKey().equals("x-java-r9-data-driven")){
            Boolean v = (Boolean)m.getValue();
            if(v){
              String portsPackage = (String) additionalProperties.get("portPackage");
              String portsPackageInput = portsPackage + ".input";
              String portInputUseCaseName = StringUtils.camelize(sanitizeName(operation.operationId), true);
              portInputUseCaseName = portInputUseCaseName.substring(0, 1).toUpperCase() + portInputUseCaseName.substring(1) + "UseCase";
              String portsPackageOutput = portsPackage + ".output";
              String portName = StringUtils.camelize(sanitizeName(operation.operationId), true);
              portName = portName.substring(0, 1).toUpperCase() + portName.substring(1) + "Port";
              String portsPackageDbOutput = (String)additionalProperties.get("portPackageImpl");
              String portDbName = StringUtils.camelize(sanitizeName(operation.operationId), true)+ "DbPort";
              portDbName = portDbName.substring(0, 1).toUpperCase() + portDbName.substring(1) ;

              {
                String packagePortInputUseCase = (sourceFolder + File.separator + portsPackageInput).replace(".", File.separator);
                log.info("x-java-r9-data-driven usecases active on operation {} | iteractor : {} | portsPackageInput: {} | Folder: {}", operation.operationId, portInputUseCaseName, portsPackageInput, packagePortInputUseCase);

                TemplateManagerOptions opts = new TemplateManagerOptions(false, false);
                TemplateManager manager = new TemplateManager(opts, mustacheEngineAdapter, new TemplatePathLocator[]{locator});
                Map<String, Object> data = new HashMap<>();
                data.put("packagePortInput", portsPackageInput);
                data.put("classname", portInputUseCaseName);
                //data.put("varNameOperation", StringUtils.camelize(sanitizeName(operation.operationId), true)+ "UseCase");
                data.put("dbportClass", portsPackageOutput+"."+portName );
                data.put("dbportClassVar", StringUtils.camelize(sanitizeName(operation.operationId), true)+ "Port");
                data.put("operation", operation);
                operation.vendorExtensions.put("x-java-r9-data-driven", true);
                operation.vendorExtensions.put("x-java-r9-data-driven-class-invoke", portsPackageInput+"."+portInputUseCaseName );
                operation.vendorExtensions.put("x-java-r9-data-driven-class-var", StringUtils.camelize(sanitizeName(operation.operationId), true)+ "UseCase");
                try {
                  manager.write(data, "portInput.mustache", new File(outputFolder + "/" + packagePortInputUseCase + "/" + portInputUseCaseName + ".java"));
                } catch (Exception e) {
                  log.error("x-java-r9-data-driven error generate : {} ", e.getLocalizedMessage());
                }
              }
              {
                String packagePortOutputUseCase = (sourceFolder + File.separator + portsPackageOutput).replace(".", File.separator);
                {
                  log.info("x-java-r9-data-driven ports active on operation {} | iteractor : {} | portsPackageOutput: {} | Folder: {}", operation.operationId, portName, portsPackageOutput, packagePortOutputUseCase);
                  TemplateManagerOptions opts = new TemplateManagerOptions(false, false);
                  TemplateManager manager = new TemplateManager(opts, mustacheEngineAdapter, new TemplatePathLocator[]{locator});
                  Map<String, Object> data = new HashMap<>();
                  data.put("packagePortOutput", portsPackageOutput);
                  data.put("classname", portName);
                  data.put("varNameOperation", StringUtils.camelize(sanitizeName(operation.operationId), true) + "Port");
                  data.put("operation", operation);
                  try {
                    manager.write(data, "portOutput.mustache", new File(outputFolder + "/" + packagePortOutputUseCase + "/" + portName + ".java"));
                  } catch (Exception e) {
                    log.error("x-java-r9-data-driven error generate : {} ", e.getLocalizedMessage());
                  }
                }
                log.info("x-java-r9-data-driven active database  : {} ", additionalProperties.get("database"));
                Boolean database = (Boolean)additionalProperties.get("database");
                if(database){
                  String packageDbPortOutputUseCase = (sourceFolder + File.separator + portsPackageDbOutput).replace(".", File.separator);
                  log.info("x-java-r9-data-driven dbports class  active on operation {} | iteractor : {} | portsPackageOutput: {} | Folder: {}", operation.operationId, portDbName, portsPackageDbOutput, packageDbPortOutputUseCase);
                  TemplateManagerOptions opts = new TemplateManagerOptions(false, false);
                  TemplateManager manager = new TemplateManager(opts, mustacheEngineAdapter, new TemplatePathLocator[]{locator});
                  Map<String, Object> data = new HashMap<>();
                  data.put("packagePortOutput", portsPackageDbOutput);
                  data.put("classname", portDbName);
                  data.put("importimplement", portsPackageOutput+".*");
                  data.put("implementclassname", portName);
                  //data.put("varNameOperation", StringUtils.camelize(sanitizeName(operation.operationId), true));
                  data.put("operation", operation);
                  try {
                    manager.write(data, "portDbOutput.mustache", new File(outputFolder + "/" + packageDbPortOutputUseCase + "/" + portDbName + ".java"));
                  } catch (Exception e) {
                    log.error("x-java-r9-data-driven error generate : {} ", e.getLocalizedMessage());
                  }
                }
              }
            }
          }
          if(m.getKey().equals("x-java-r9-events-choreography")){
            Boolean v = (Boolean)m.getValue();
            if(v){
            /*
            String iteractorPackage = (String)additionalProperties.get("iteractorPackage");
            String portsPackage = (String)additionalProperties.get("portPackage");
            String portsPackageInput = portsPackage + ".input";
            String portsPackageOutput = portsPackage + ".output";

            String portInputUseCaseName = StringUtils.camelize(sanitizeName(operation.getOperationId()), true);
            portInputUseCaseName = portInputUseCaseName.substring(0, 1).toUpperCase() + portInputUseCaseName.substring(1)+ "UseCase";
            //additionalProperties.put("classname",nameFile);
            additionalProperties.put("packagePortInput",portsPackageInput);
            String packagePortInputUseCase = (sourceFolder + File.separator + portsPackageInput).replace(".", File.separator);
            log.info("x-java-r9-events-choreography active on operation {} | iteractor : {} | portsPackageInput: {} | Folder: {}" , operation.getOperationId(), portInputUseCaseName , portsPackageInput, packagePortInputUseCase);

            TemplateManagerOptions opts = new TemplateManagerOptions(false,false);
            TemplateManager manager = new TemplateManager(opts, mustacheEngineAdapter, new TemplatePathLocator[]{ locator });

            Map<String, Object> data = new HashMap<>();
            data.put("packagePortInput",portsPackageInput);
            data.put("classname",portInputUseCaseName);

            //Path target = Files.get("test-templatemanager");

            try {
              File written = manager.write(data, "portOutput.mustache", new File(outputFolder+"/"+packagePortInputUseCase+"/"+portInputUseCaseName + ".java"));
            } catch (Exception e){

            }
            */
            }
          }


        }


        //});
      }
    }


    return super.postProcessOperationsWithModels(objs, allModels);
  }
/*
@Override
    @SuppressWarnings({"static-method", "unchecked"})
    public Map<String, Object> postProcessAllModels(Map<String, Object> objs) {
        if (this.useOneOfInterfaces) {
            // First, add newly created oneOf interfaces
            for (CodegenModel cm : addOneOfInterfaces) {
                Map<String, Object> modelValue = new HashMap<>(additionalProperties());
                modelValue.put("model", cm);

                List<Map<String, String>> importsValue = new ArrayList<>();
                Map<String, Object> objsValue = new HashMap<>();
                objsValue.put("models", Collections.singletonList(modelValue));
                objsValue.put("package", modelPackage());
                objsValue.put("imports", importsValue);
                objsValue.put("classname", cm.classname);
                objsValue.putAll(additionalProperties);
                objs.put(cm.name, objsValue);
            }

            // Gather data from all the models that contain oneOf into OneOfImplementorAdditionalData classes
            // (see docstring of that class to find out what information is gathered and why)
            Map<String, OneOfImplementorAdditionalData> additionalDataMap = new HashMap<String, OneOfImplementorAdditionalData>();
            for (Map.Entry<String, Object> modelsEntry : objs.entrySet()) {
                Map<String, Object> modelsAttrs = (Map<String, Object>) modelsEntry.getValue();
                List<Object> models = (List<Object>) modelsAttrs.get("models");
                List<Map<String, String>> modelsImports = (List<Map<String, String>>) modelsAttrs.getOrDefault("imports", new ArrayList<Map<String, String>>());
                for (Object _mo : models) {
                    Map<String, Object> mo = (Map<String, Object>) _mo;
                    CodegenModel cm = (CodegenModel) mo.get("model");
                    if (cm.oneOf.size() > 0) {
                        cm.vendorExtensions.put("x-is-one-of-interface", true);
                        for (String one : cm.oneOf) {
                            if (!additionalDataMap.containsKey(one)) {
                                additionalDataMap.put(one, new OneOfImplementorAdditionalData(one));
                            }
                            additionalDataMap.get(one).addFromInterfaceModel(cm, modelsImports);
                        }
                        // if this is oneOf interface, make sure we include the necessary imports for it
                        addImportsToOneOfInterface(modelsImports);
                    }
                }
            }

            // Add all the data from OneOfImplementorAdditionalData classes to the implementing models
            for (Map.Entry<String, Object> modelsEntry : objs.entrySet()) {
                Map<String, Object> modelsAttrs = (Map<String, Object>) modelsEntry.getValue();
                List<Object> models = (List<Object>) modelsAttrs.get("models");
                List<Map<String, String>> imports = (List<Map<String, String>>) modelsAttrs.get("imports");
                for (Object _implmo : models) {
                    Map<String, Object> implmo = (Map<String, Object>) _implmo;
                    CodegenModel implcm = (CodegenModel) implmo.get("model");
                    String modelName = toModelName(implcm.name);
                    if (additionalDataMap.containsKey(modelName)) {
                        additionalDataMap.get(modelName).addToImplementor(this, implcm, imports, addOneOfInterfaceImports);
                    }
                }
            }
        }

        return objs;
    }

 */

  private void doDataTypeAssignment(String returnType, DataTypeAssigner dataTypeAssigner) {
    final String rt = returnType;
    if (rt == null) {
      dataTypeAssigner.setReturnType("Void");
    } else if (rt.startsWith("List")) {
      int end = rt.lastIndexOf(">");
      if (end > 0) {
        dataTypeAssigner.setReturnType(rt.substring("List<".length(), end).trim());
        dataTypeAssigner.setReturnContainer("List");
      }
    } else if (rt.startsWith("Map")) {
      int end = rt.lastIndexOf(">");
      if (end > 0) {
        dataTypeAssigner.setReturnType(rt.substring("Map<".length(), end).split(",", 2)[1].trim());
        dataTypeAssigner.setReturnContainer("Map");
      }
    } else if (rt.startsWith("Set")) {
      int end = rt.lastIndexOf(">");
      if (end > 0) {
        dataTypeAssigner.setReturnType(rt.substring("Set<".length(), end).trim());
        dataTypeAssigner.setReturnContainer("Set");
      }
    }
  }

  private interface DataTypeAssigner {
    void setReturnType(String returnType);
    void setReturnContainer(String returnContainer);
  }

  @Override
  public void postProcess() {
    log.info("Compile complete");
  }

}