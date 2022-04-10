package org.raise9.kernel.open.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Paths;
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
import org.apache.commons.text.StringEscapeUtils;
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
import org.openapitools.codegen.utils.ModelUtils;
import org.openapitools.codegen.utils.StringUtils;
import org.openapitools.codegen.utils.URLPathUtils;

@Getter
@Setter
@Slf4j
public class R9OpenAPIQuarkusCodeGenerator extends AbstractJavaCodegen implements BeanValidationFeatures, PerformBeanValidationFeatures {

  private final HandlebarsEngineAdapter handlebarsEngineAdapter = new HandlebarsEngineAdapter();
  private final MustacheEngineAdapter mustacheEngineAdapter = new MustacheEngineAdapter();
  private final TemplatePathLocator locator = new ResourceTemplateLoader();

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

  public static final String ENTITIES_PACKAGE = "entitiesPackage";
  protected String entities_package = "org.openapitools.entities";


  public static final String TYPE_DATABASE = "typeDatabaseExtension";
  protected String typedatabaseDefault = null;


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

  Map<String, Schema> schemasEntitiesMapTemp = new HashMap<>();

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
    cliOptions.add(new CliOption(ENTITIES_PACKAGE, "wrap port context").defaultValue(entities_package));
    cliOptions.add(new CliOption(TYPE_DATABASE, "wrap port context").defaultValue(typedatabaseDefault));

    setLibrary(QUARKUS_LIBRARY);

    CliOption library = new CliOption(CodegenConstants.LIBRARY, CodegenConstants.LIBRARY_DESC).defaultValue(QUARKUS_LIBRARY);
    library.setEnum(supportedLibraries);
    cliOptions.add(library);
    cliOptions.add(new CliOption(APP_NAME_QUARKUS, "Name class main of quarkus").defaultValue(appNameBase));
    cliOptions.add(new CliOption(APP_PATH_BASE, "Path Base quarkus app").defaultValue(appPathBase));

    cliOptions.add(CliOption.newBoolean(REACTIVE, "wrap responses in Uni/Multi smallrye types (quarkus only)", reactive));
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
    super.processOpts();
    log.info("Finish processOpts invokePackage {} apiPackage {} basePackage {} modelPackage {}", invokePackage, apiPackage, basePackage, modelPackage);

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

    Map<String, Schema> schemasMapTemp = new HashMap<>();
    Map<String, Schema> schemasMap = openAPI.getComponents().getSchemas();

    for (Map.Entry<String,Schema> m :schemasMap.entrySet()  ) {
      if(null!=m.getValue().getExtensions() && m.getValue().getExtensions().containsKey("x-java-r9-rest-entity")&& !((Boolean)m.getValue().getExtensions().get("x-java-r9-rest-entity"))){
        log.info("preprocessOpenAPI m : {} {} " , m.getKey() , m.getValue().getExtensions());
        schemasEntitiesMapTemp.put(m.getKey(), m.getValue());
      }else{
        schemasMapTemp.put(m.getKey(), m.getValue());
      }
    }
    openAPI.getComponents().setSchemas(schemasMapTemp);;

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

  }

  @Override
  public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
    //log.info("postProcessModelProperty model {} {} {}" , model.getName(), model.getClassVarName(), model.getDescription());
    super.postProcessModelProperty(model,property);
    Set<String> imports = model.imports.stream().filter(s -> !(s.equals("ApiModel")||s.equals("ApiModelProperty"))).collect(Collectors.toSet());
    model.imports = imports;
  }

  @Override
  public CodegenModel fromModel(String name, Schema model) {
    CodegenModel ms = super.fromModel(name,model);
    /*Map<String, Object> vendorExtensions = ms.vendorExtensions;
    if(null!=vendorExtensions && vendorExtensions.containsKey("x-java-r9-rest-entity")&& !((Boolean)vendorExtensions.get("x-java-r9-rest-entity"))){
      log.info("Entity model ms {} - {} {}", ms.getName(), ms.description, ms.classname);
      //ms.classname = name;
      //ms.classVarName = name;
      //ms.classFilename = name;
      //ms.classname = this.toModelName(name);
      //ms.classVarName = this.toVarName(name);
     // ms.classFilename = this.toModelFilename(name);
      log.info("modelTemplateFiles.keySet() {}" , modelTemplateFiles.keySet() );
    }*/
    Set<String> imports = ms.imports.stream().filter(s -> !(s.equals("ApiModel")||s.equals("ApiModelProperty"))).collect(Collectors.toSet());
    ms.imports = imports;
    //log.info("fromModel model b {} - {} ", name, ms.classname);
    return ms;
  }

  @Override
  public String toModelName(String name){
    //log.info("toModelName name a {} " , name);
    String nameN = null;
    Map<String, Schema> schemas = ModelUtils.getSchemas(openAPI);
    if((null!=schemas.get(name))){
      String sanitizedName = this.sanitizeName(name);
      String nameWithPrefixSuffix = sanitizedName;
      if (!org.apache.commons.lang3.StringUtils.isEmpty(this.modelNamePrefix)) {
        nameWithPrefixSuffix = this.modelNamePrefix + "_" + sanitizedName;
      }
      String camelizedName = org.openapitools.codegen.utils.StringUtils.camelize(nameWithPrefixSuffix);
      String modelName;
      if (this.isReservedWord(camelizedName)) {
        modelName = "Model" + camelizedName;
        log.warn("{} (reserved word) cannot be used as model name. Renamed to {}", camelizedName, modelName);
        nameN = modelName;
      } else if (camelizedName.matches("^\\d.*")) {
        modelName = "Model" + camelizedName;
        log.warn("{} (model name starts with number) cannot be used as model name. Renamed to {}", name, modelName);
        nameN = modelName;
      } else {
        nameN = camelizedName;
      }
    }else{
      name = super.toModelName(name);
    }
    //log.info("toModelName name b {} " , nameN);
    return nameN;
  }

  private List<Operation> getOperations(OpenAPI openAPI) {
    return openAPI.getPaths().values().stream().filter(Objects::nonNull).flatMap(this::getAllPossibleOperations).filter(Objects::nonNull).collect(Collectors.toList());
  }

  private Stream<Operation> getAllPossibleOperations(PathItem pathItem) {
    return Stream.of(pathItem.getGet(), pathItem.getPost(), pathItem.getDelete(), pathItem.getHead(), pathItem.getOptions(), pathItem.getPatch(), pathItem.getPut(), pathItem.getTrace());
  }

  private static String getUnicodeCharacterOfChar(String ch) {
    return String.format("%040x", new BigInteger(1, ch.getBytes(/*YOUR_CHARSET?*/)));
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
    List<Map<String, Object>> listConvertsObjects = new ArrayList<>();
    Map<String, Object> extesionsParent = openAPI.getExtensions();
    if(null != extesionsParent){
      for (Map.Entry<String,Object> m : extesionsParent.entrySet()  ) {
        List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");
        for (final CodegenOperation operation : ops) {
          log.info("->  operation summary {} - returnType {}", operation.summary, operation.returnType);
          if(m.getKey().equals("x-java-r9-data-driven") && m.getValue().equals(true)){
            Boolean v = (Boolean)m.getValue();
            log.info("x-java-r9-data-driven active database  : {} ", additionalProperties.get("database"));
            Boolean database = (Boolean) additionalProperties.get("database");
            if(v){


              String entities_package = (String) additionalProperties.get(ENTITIES_PACKAGE);

              String portsPackage = (String) additionalProperties.get("portPackage");
              String portsPackageInput = portsPackage + ".input";
              String portInputUseCaseName = StringUtils.camelize(sanitizeName(operation.operationId), true);
              portInputUseCaseName = portInputUseCaseName.substring(0, 1).toUpperCase() + portInputUseCaseName.substring(1) + "UseCase";
              String portsPackageOutput = portsPackage + ".output";
              String portName = StringUtils.camelize(sanitizeName(operation.operationId), true);
              portName = portName.substring(0, 1).toUpperCase() + portName.substring(1) + "Port";
              String portsPackageDbOutput = (String) additionalProperties.get("portPackageImpl");
              String portDbName = StringUtils.camelize(sanitizeName(operation.operationId), true) + "DbPort";
              portDbName = portDbName.substring(0, 1).toUpperCase() + portDbName.substring(1);

              {
                Map<String, Object> entities = new HashMap<>();
                List<CodegenParameter> bodyParams = operation.bodyParams;
                Map<String, Schema> schemas = ModelUtils.getSchemas(openAPI);
                CodegenModel cmResponse = null;
                CodegenModel cmEntotyDomain = null;
                CodegenModel cmEntotyPortModel = null;
                Map<String, Object> vendorExtensionsEntity = null;
                Map<String, Object> vendorExtensionsDomain = null;
                Map<String, Object> vendorExtensionsEntityDb = null;

                if (null != bodyParams) {
                  for (CodegenParameter cp : bodyParams) {
                    cmResponse = this.fromModel(cp.baseType, schemas.get(cp.baseType));
                    vendorExtensionsEntity = cmResponse.vendorExtensions;
                    log.info("vendorExtensionsEntity : {}", vendorExtensionsEntity.keySet());
                  }
                  log.info("schemasEntitiesMapTemp : {}", schemasEntitiesMapTemp.keySet());
                  if (null != schemasEntitiesMapTemp || schemasEntitiesMapTemp.size() > 0) {
                    log.info("x-java-r9-entity-domian containsKey : {}", vendorExtensionsEntity.get("x-java-r9-entity-domian"));
                    cmEntotyDomain = this.fromModel(String.valueOf(vendorExtensionsEntity.get("x-java-r9-entity-domian")), schemasEntitiesMapTemp.get(vendorExtensionsEntity.get("x-java-r9-entity-domian")));
                    vendorExtensionsDomain = cmEntotyDomain.vendorExtensions;

                    log.info("x-java-r9-convert-entity-db containsKey : {}", vendorExtensionsDomain.containsKey("x-java-r9-convert-entity-db"));
                    if (null != vendorExtensionsDomain && vendorExtensionsDomain.containsKey("x-java-r9-convert-entity-db")) {
                      String nameDomain = String.valueOf(vendorExtensionsDomain.get("x-java-r9-convert-entity-db"));
                      cmEntotyPortModel = this.fromModel(nameDomain, schemasEntitiesMapTemp.get(nameDomain));
                      vendorExtensionsEntityDb = cmEntotyPortModel.vendorExtensions;
                      log.info("vendorExtensionsEntityDb : {}", vendorExtensionsEntityDb.keySet());
                    }
                  }
                }
                log.info("Exists cmResponse : {}", null!=cmResponse);
                log.info("Exists cmEntotyDomain : {}", null!=cmEntotyDomain);
                log.info("Exists cmEntotyPortModel : {}", null!=cmEntotyPortModel);
                if (null != cmResponse && null != cmEntotyDomain && null != cmEntotyPortModel) {
                  log.info("Not create Entity rest select : {}", cmResponse.getName());
                  log.info("Create Entity domain select : {}", cmEntotyDomain.getName());
                  log.info("Create Entity port select : {}", cmEntotyPortModel.getName());
                  log.info("Database config state : {}", database);
                  entities.put("entityRest",cmResponse);
                  entities.put("entityDomain",cmEntotyDomain);
                  entities.put("entityDatabase",cmEntotyPortModel);
                  listConvertsObjects.add(entities);
                }

                if (null!=cmEntotyDomain && null!= cmEntotyPortModel &&  database) {
                  {
                    String foldersModelDbPort = (sourceFolder + File.separator + portsPackageDbOutput).replace(".", File.separator) + File.separator + "model";
                    String packageModelDbPort = portsPackageOutput + ".model";
                    entities.put("entityDatabase-import",packageModelDbPort);

                    log.info("x-java-r9-convert-entity-db entity db class  active on operation {} | iteractor : {} | portsPackageOutput: {} | Folder: {}", operation.operationId, portDbName, packageModelDbPort, foldersModelDbPort);
                    TemplateManagerOptions opts = new TemplateManagerOptions(false, false);
                    TemplateManager manager = new TemplateManager(opts, mustacheEngineAdapter, new TemplatePathLocator[]{locator});
                    Map<String, Object> data = new HashMap<>();

                    cmEntotyPortModel.classname = cmEntotyPortModel.getName();

                    data.put("packageModelPort", packageModelDbPort);
                    data.put("model", cmEntotyPortModel);
                    List<String> listArrayListImport = new ArrayList<>();
                    listArrayListImport.add("java.util.*;");
                    data.put("imports", listArrayListImport);
                    {
                      String entityName = String.valueOf(vendorExtensionsEntityDb.get("x-java-r9-db-name"));
                      log.info("Entity Name : {}" , entityName);
                      String type_database = (String) additionalProperties.get(TYPE_DATABASE);
                      log.info("Type database : {}" , type_database);
                      switch (type_database){
                        case "mongo":
                          log.info("x-java-r9-convert-entity-db entity db");
                          data.put("annotation_database_mongo",true);
                          data.put("annotation_database_entity",entityName);
                          data.put("extens_database", " extends PanacheMongoEntity ");
                          break;
                      }

                    }

                    try {
                      manager.write(data, "modeldbport.mustache", new File(outputFolder + "/" + foldersModelDbPort + "/" + cmEntotyPortModel.getName() + ".java"));
                    } catch (Exception e) {
                      log.error("x-java-r9-convert-entity-db error generate : {} ", e.getLocalizedMessage());
                    }
                  }
                  {
                    String foldersEntitesDbPort = (sourceFolder + File.separator + entities_package).replace(".", File.separator);
                    entities.put("entityDomain-import",entities_package);
                    log.info("x-java-r9-entity-domian entity db class  active on operation {} | iteractor : {} | portsPackageOutput: {} | Folder: {}", operation.operationId, portDbName, entities_package, foldersEntitesDbPort);
                    TemplateManagerOptions opts = new TemplateManagerOptions(false, false);
                    TemplateManager manager = new TemplateManager(opts, mustacheEngineAdapter, new TemplatePathLocator[]{locator});
                    Map<String, Object> data = new HashMap<>();

                    cmEntotyDomain.classname = cmEntotyDomain.getName();

                    data.put("packageModelPort", entities_package);
                    data.put("model", cmEntotyDomain);

                    List<String> listArrayListImport = new ArrayList<>();
                    listArrayListImport.add("java.util.*;");
                    data.put("imports", listArrayListImport);

                    try {
                      manager.write(data, "domainModel.mustache", new File(outputFolder + "/" + foldersEntitesDbPort + "/" + cmEntotyDomain.getName() + ".java"));
                    } catch (Exception e) {
                      log.error("x-java-r9-entity-domian error generate : {} ", e.getLocalizedMessage());
                    }
                  }
                }

              }

              {

                {
                  String packagePortInputUseCase = (sourceFolder + File.separator + portsPackageInput).replace(".", File.separator);
                  log.info("x-java-r9-data-driven usecases active on operation {} | iteractor : {} | portsPackageInput: {} | Folder: {}", operation.operationId, portInputUseCaseName, portsPackageInput, packagePortInputUseCase);
                  TemplateManagerOptions opts = new TemplateManagerOptions(false, false);
                  TemplateManager manager = new TemplateManager(opts, mustacheEngineAdapter, new TemplatePathLocator[]{locator});
                  Map<String, Object> data = new HashMap<>();
                  data.put("packagePortInput", portsPackageInput);
                  data.put("classname", portInputUseCaseName);
                  data.put("dbportClass", portsPackageOutput + "." + portName);
                  data.put("dbportClassVar", StringUtils.camelize(sanitizeName(operation.operationId), true) + "Port");
                  data.put("operation", operation);
                  List<String> listArrayListImport = new ArrayList<>();
                  listArrayListImport.add(modelPackage + ".*;");
                  data.put("importModel", listArrayListImport);
                  operation.vendorExtensions.put("x-java-r9-data-driven", true);
                  operation.vendorExtensions.put("x-java-r9-data-driven-class-invoke", portsPackageInput + "." + portInputUseCaseName);
                  operation.vendorExtensions.put("x-java-r9-data-driven-class-var", StringUtils.camelize(sanitizeName(operation.operationId), true) + "UseCase");
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
                    List<String> listArrayListImport = new ArrayList<>();
                    listArrayListImport.add(modelPackage + ".*;");
                    data.put("importModel", listArrayListImport);
                    try {
                      manager.write(data, "portOutput.mustache", new File(outputFolder + "/" + packagePortOutputUseCase + "/" + portName + ".java"));
                    } catch (Exception e) {
                      log.error("x-java-r9-data-driven error generate : {} ", e.getLocalizedMessage());
                    }
                  }
                  if (database) {
                    String packageDbPortOutputUseCase = (sourceFolder + File.separator + portsPackageDbOutput).replace(".", File.separator);
                    log.info("x-java-r9-data-driven dbports class  active on operation {} | iteractor : {} | portsPackageOutput: {} | Folder: {}", operation.operationId, portDbName, portsPackageDbOutput, packageDbPortOutputUseCase);
                    TemplateManagerOptions opts = new TemplateManagerOptions(false, false);
                    TemplateManager manager = new TemplateManager(opts, mustacheEngineAdapter, new TemplatePathLocator[]{locator});
                    Map<String, Object> data = new HashMap<>();
                    data.put("packagePortOutput", portsPackageDbOutput);
                    data.put("classname", portDbName);
                    data.put("importimplement", portsPackageOutput + ".*");
                    data.put("implementclassname", portName);
                    //data.put("varNameOperation", StringUtils.camelize(sanitizeName(operation.operationId), true));
                    data.put("operation", operation);
                    List<String> listArrayListImport = new ArrayList<>();
                    listArrayListImport.add(modelPackage + ".*;");
                    data.put("importModel", listArrayListImport);
                    try {
                      manager.write(data, "portDbOutput.mustache", new File(outputFolder + "/" + packageDbPortOutputUseCase + "/" + portDbName + ".java"));
                    } catch (Exception e) {
                      log.error("x-java-r9-data-driven error generate : {} ", e.getLocalizedMessage());
                    }
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
        /*
                  entities.put("entityRest",cmResponse);
                  entities.put("entityDomain",cmResponse);
                  entities.put("entityDatabase",cmEntotyPortModel);
        * */

        //converMap
        if(listConvertsObjects.size()>0){
          List<String> methodsList = new ArrayList<>();
          TemplateManagerOptions opts = new TemplateManagerOptions(false, false);
          TemplateManager manager = new TemplateManager(opts, mustacheEngineAdapter, new TemplatePathLocator[]{locator});
          String folderConfigPackage = (sourceFolder + File.separator + configPackage).replace(".", File.separator);

          Map<String, Object> data = new HashMap<>();
          data.put("package", configPackage);
          data.put("classname", "UtilConvertMapping");

          List<String> listArrayListImport = new ArrayList<>();

          List<Map<String,Map<String, Object>>> ob = new ArrayList<>();

          for (Map<String, Object> entitiesConvert : listConvertsObjects){
            CodegenModel entityRest = (CodegenModel)entitiesConvert.get("entityRest");
            log.info("entityRest {}" ,entityRest.classname);
            CodegenModel entityDomain = (CodegenModel)entitiesConvert.get("entityDomain");
            log.info("entityDomain {}" ,entityDomain.classname);
            CodegenModel entityDatabase = (CodegenModel)entitiesConvert.get("entityDatabase");
            log.info("entityDatabase {}" ,entityDatabase.classname);

            String methodA = "from" + entityDomain.classname + "To" +  entityRest.getClassFilename() ;
            String methodB = "from" + entityRest.getClassFilename() + "To" +  entityDomain.classname;

            String methodC = "from" + entityDatabase.classname + "To" +  entityDomain.classname ;
            String methodD = "from" + entityDomain.classname + "To" +  entityDatabase.classname;

            if(!(methodsList.contains(methodA) ||  methodsList.contains(methodB)||  methodsList.contains(methodC)||  methodsList.contains(methodD))){
              log.info("methodA {}" ,methodA);
              log.info("methodB {}" ,methodB);
              methodsList.add(methodA);
              methodsList.add(methodB);

              //entities.put("entityDomain-import",entities_package);
              //entities.put("entityDatabase-import",packageModelDbPort);
              listArrayListImport.add(entitiesConvert.get("entityDomain-import").toString()+".*; ");
              listArrayListImport.add(entitiesConvert.get("entityDatabase-import").toString()+".*; ");
              {
                Map<String, Map<String, Object>> convert = new HashMap<>();
                Map<String, Object> mff = new HashMap<>();
                mff.put("returndata", entityRest.getClassFilename());
                mff.put("nameMethod", methodA);
                mff.put("param", entityDomain.classname);
                convert.put("convert",mff);
                ob.add(convert);
              }
              {
                Map<String, Map<String, Object>> convert = new HashMap<>();
                Map<String, Object> mff = new HashMap<>();
                mff.put("returndata", entityDomain.classname);
                mff.put("nameMethod", methodB);
                mff.put("param", entityRest.getClassFilename());
                convert.put("convert",mff);
                ob.add(convert);
              }
              {
                Map<String, Map<String, Object>> convert = new HashMap<>();
                Map<String, Object> mff = new HashMap<>();
                mff.put("returndata", entityDatabase.classname);
                mff.put("nameMethod", methodC);
                mff.put("param", entityDomain.classname);
                convert.put("convert",mff);
                ob.add(convert);
              }
              {
                Map<String, Map<String, Object>> convert = new HashMap<>();
                Map<String, Object> mff = new HashMap<>();
                mff.put("mappging-id", true);
                mff.put("returndata", entityDomain.classname);
                mff.put("nameMethod", methodD);
                mff.put("param", entityDatabase.classname);
                convert.put("convert",mff);
                ob.add(convert);
              }

            }
          }
          listArrayListImport.add(modelPackage + ".*;");
          data.put("imports", listArrayListImport);

          data.put("converts", ob);

          try {
            manager.write(data, "converMap.mustache", new File(outputFolder + "/" + folderConfigPackage + "/UtilConvertMapping.java"));
          } catch (Exception e) {
            log.error("x-java-r9-data-driven error generate : {} ", e.getLocalizedMessage());
          }

        }

      }
    }
    return super.postProcessOperationsWithModels(objs, allModels);
  }

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
  public void postProcess() {
    log.info("Compile complete");
  }

  private interface DataTypeAssigner {
    void setReturnType(String returnType);
    void setReturnContainer(String returnContainer);
  }
  static class ResourceTemplateLoader implements TemplatePathLocator {
    @Override
    public String getFullTemplatePath(String relativeTemplateFile) {
      log.info("ResourceTemplateLoader relativeTemplateFile : {}" ,relativeTemplateFile);
      return Paths.get("quarkus-open-api","", relativeTemplateFile).toString();
    }
  }

}