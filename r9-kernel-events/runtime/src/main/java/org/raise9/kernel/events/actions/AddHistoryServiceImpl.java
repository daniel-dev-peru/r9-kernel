package org.raise9.kernel.events.actions;

import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.raise9.kernel.events.StatusEvent;
import org.raise9.kernel.utils.core.JsonMapper;

@Slf4j
@ApplicationScoped
public class AddHistoryServiceImpl implements AddHistoryService {

  @Inject
  RestClient restClient;

  @Inject
  JsonMapper jsonMapper;

  @Inject
  GetHistoryService getHistoryService;

  @Override
  public void execute(String type, StatusEvent status, String id, String data) {
    String typeLowerCase = type.toLowerCase();
    try {
      Request request = new Request("PUT", "/" + typeLowerCase + "/_doc/" + id);
      Map<String, Object> historyState = getHistoryService.execute(typeLowerCase, id);
      historyState.put(status.name(), jsonMapper.stringToJsonNode(data));
      historyState.put("lastState", status.name());
      request.setJsonEntity(jsonMapper.objectToString(historyState));
      restClient.performRequest(request);
      log.debug("Save history {} {} {} -> {}", typeLowerCase, status, id, data);
    } catch (Exception e) {
      log.error("Save history {} {} {}", typeLowerCase, status, id, e);
    }
  }


}
