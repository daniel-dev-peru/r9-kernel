package org.raise9.kernel.events.actions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.raise9.kernel.utils.core.JsonMapper;

@Slf4j
@ApplicationScoped
public class GetHistoryLastStateServiceImpl implements GetHistoryLastStateService {

  @Inject
  RestClient restClient;

  @Inject
  JsonMapper jsonMapper;

  @Override
  public JsonNode execute(String type, String id) {
    String typeLowerCase = type.toLowerCase();
    try {
      Request request = new Request("GET", "/" + typeLowerCase + "/_doc/" + id);
      Response response = restClient.performRequest(request);
      String responseBody = EntityUtils.toString(response.getEntity());
      JsonNode data = jsonMapper.stringToJsonNode(responseBody);
      TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
      };
      JsonNode sourceData = data.get("_source");
      String
              lastState =
              sourceData.get("lastState")
                      .textValue();
      log.info("Get type : {} id : {} lastState : {}", typeLowerCase, id, lastState);
      return sourceData.get(lastState);
    } catch (Exception e) {
      return null;
    }
  }
}
