package org.raise9.kernel.events.actions;

import com.fasterxml.jackson.databind.JsonNode;

public interface GetHistoryLastStateService {
  JsonNode execute(String type, String id);
}
