package org.raise9.kernel.events;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

public interface StateHistoryEventApi {
  void add(String type, StatusEvent status, String id, String data);

  Map<String, Object> get(String type, String id);

  JsonNode getLastState(String type, String id);
}
