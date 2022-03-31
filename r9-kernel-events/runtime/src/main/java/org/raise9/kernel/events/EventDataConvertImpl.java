package org.raise9.kernel.events;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.raise9.kernel.utils.core.JsonMapper;


@Slf4j
@Singleton
public class EventDataConvertImpl implements EventDataConvert {

  @Inject
  JsonMapper jsonMapper;

  @Override
  public <T> EventTransactionSync<T> processEventData(EventTransactionSync event) {
    EventTransactionSync eventTransactionSync = new EventTransactionSync();
    EventTransactionSync eventFather = event.getPrincipal();
    eventTransactionSync.setTransactionId(eventFather.getTransactionId());
    eventTransactionSync.setRequestId(eventFather.getRequestId());
    String type = eventFather.getType();
    StatusEvent status = eventFather.getStatus();
    log.debug("Process Event Data : {} - {} | {} - {}", event.getType(), event.getStatus(), type, status);
    eventTransactionSync.setObject(jsonMapper.convertObject(eventFather.getData(), event.getCd()));
    return eventTransactionSync;
  }

  @Override
  public boolean valid(EventTransactionSync event) {
    EventTransactionSync eventTransactionSync = new EventTransactionSync();
    EventTransactionSync eventFather = event.getPrincipal();
    eventTransactionSync.setTransactionId(eventFather.getTransactionId());
    eventTransactionSync.setRequestId(eventFather.getRequestId());
    String type = eventFather.getType();
    StatusEvent status = eventFather.getStatus();
    if (event.getType()
            .equals(type) && event.getStatus()
            .name()
            .equals(status.name())) {
      log.info("Is valid event data : {} {} {} - {} | {} - {}", eventTransactionSync.getRequestId(), eventTransactionSync.getTransactionId(), event.getType(), event.getStatus(), type, status);
      return true;
    }
    return false;
  }

  @Override
  public <T> T objectData(EventTransactionSync e) {
    return jsonMapper.convertObject(e.getData(), e.getCd());
  }
}
