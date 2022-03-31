package org.raise9.kernel.events;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.raise9.kernel.events.actions.AddHistoryService;
import org.raise9.kernel.events.actions.GetHistoryLastStateService;
import org.raise9.kernel.events.actions.GetHistoryService;

@Slf4j
@ApplicationScoped
public class StateHistoryEventApiImpl implements StateHistoryEventApi {

  @Inject
  AddHistoryService addHistoryService;

  @Inject
  GetHistoryService getHistoryService;

  @Inject
  GetHistoryLastStateService getHistoryLastStateService;

  @Override
  public void add(String type, StatusEvent status, String id, String data) {
    addHistoryService.execute(type, status, id, data);
  }

  @Override
  public Map<String, Object> get(String type, String id) {
    return getHistoryService.execute(type, id);
  }

  @Override
  public JsonNode getLastState(String type, String id) {
    return getHistoryLastStateService.execute(type, id);
  }

}