package org.raise9.kernel.events.actions;


import org.raise9.kernel.events.StatusEvent;

public interface AddHistoryService {
  void execute(String type, StatusEvent status, String id, String data) ;
}
