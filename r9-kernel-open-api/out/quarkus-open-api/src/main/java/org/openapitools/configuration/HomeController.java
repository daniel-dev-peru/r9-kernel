package org.openapitools.configuration;

/*
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.IOException;
import java.io.InputStream;

import java.net.URI;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Controller
public class HomeController {

    private static YAMLMapper yamlMapper = new YAMLMapper();

    @Value("classpath:/openapi.yaml")
    private Resource openapi;

    @Bean
    public String openapiContent() throws IOException {
        try(InputStream is = openapi.getInputStream()) {
            return StreamUtils.copyToString(is, Charset.defaultCharset());
        }
    }

    @GetMapping(value = "/openapi.yaml", produces = "application/vnd.oai.openapi")
    @ResponseBody
    public String openapiYaml() throws IOException {
        return openapiContent();
    }

    @GetMapping(value = "/openapi.json", produces = "application/json")
    @ResponseBody
    public Object openapiJson() throws IOException {
        return yamlMapper.readValue(openapiContent(), Object.class);
    }

    @Bean
    RouterFunction<ServerResponse> index() {
        return route(
            GET("/"),
            req -> ServerResponse.temporaryRedirect(URI.create("swagger-ui/index.html?url=../openapi.json")).build()
        );
    }


}
*/