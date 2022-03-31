package org.raise9.kernel.events.actions;

import java.util.Map;

public interface GetHistoryService {
  Map<String, Object> execute(String type, String id);
}
