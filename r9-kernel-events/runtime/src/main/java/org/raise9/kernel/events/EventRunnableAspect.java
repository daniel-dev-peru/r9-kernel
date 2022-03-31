package org.raise9.kernel.events;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import lombok.extern.slf4j.Slf4j;
import org.raise9.kernel.concurrent.SystemThreadExecute;
import org.raise9.kernel.utils.core.metadata.EventMetaDataCurrent;

@Slf4j
@Interceptor
@EventRunnable
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 10)
public class EventRunnableAspect implements Serializable {

  @Inject
  ProduceEventFLow produceEventFLow;

  @Inject
  EventDataConvert eventDataConvert;

  @Inject
  SystemThreadExecute execute;

  @Inject
  EventMetaDataCurrent eventMetaDataCurrent;

  @AroundInvoke
  Object interceptor(InvocationContext context) throws Exception {
    Method m = context.getMethod();
    EventRunnable eventRunnable = m.getAnnotation(EventRunnable.class);
    ConsumeEvent consumeEvent = m.getAnnotation(ConsumeEvent.class);
    log.debug("EventRunnableAspect start : {} - {}", eventRunnable.eventType(), consumeEvent.value());
    EventTransactionSync event = (EventTransactionSync) context.getParameters()[0];
    EventTransactionSync eventWrap = new EventTransactionSync();
    eventWrap.setType(consumeEvent.value());
    eventWrap.setPrincipal(event);
    eventWrap.setStatus(eventRunnable.eventType());
    eventWrap.setCd(eventRunnable.objectType());
    event.setCd(eventRunnable.objectType());
    log.debug("Pre valid start {} - {} -> {}", eventRunnable.eventType(), event.getStatus(), consumeEvent.value());
    boolean validCreate = eventDataConvert.valid(eventWrap);
    if (validCreate) {
      //Thread.sleep(5000);
      Object de = eventDataConvert.objectData(event);
      event.setObject(de);
      try {
        if (event.getStatus() == StatusEvent.CREATE) {
          //Thread.sleep(5000);
          produceEventFLow.send(event.getType(), event.getTransactionId(), event.getRequestId(), de, StatusEvent.PROCESSING);
        }
        log.info("Execute start {} -> {}", eventRunnable.eventType(), consumeEvent.value());
        Map<String, String> mapHeaders = event.getMapHeaders();
        log.info("Headers async {}", mapHeaders);
        eventMetaDataCurrent.setMapHeaders(mapHeaders);
        eventMetaDataCurrent.getHeadersFromRequest(eventRunnable.header());

        Uni<EventTransactionSync>
                o =
                ((Uni<EventTransactionSync>) context.proceed())
                        .runSubscriptionOn(execute.getExecutorEvents())
                        .invoke((s) -> {
                          if (event.getStatus() == StatusEvent.CREATE) {
                            /*try {
                              Thread.sleep(5000);
                            } catch (InterruptedException e) {
                              e.printStackTrace();
                            }*/
                            produceEventFLow.send(event.getType(), event.getTransactionId(), event.getRequestId(), de, StatusEvent.SUCCESS);
                          }
                        })
                        .runSubscriptionOn(execute.getExecutorEvents())
                ;
        return o;
      } catch (Exception e) {
        log.error("Error on proceed event {} - {} error: {} ", eventRunnable.eventType(), consumeEvent.value(), e.getCause(), e);
        if (event.getStatus() == StatusEvent.CREATE) {
          produceEventFLow.send(event.getType(), event.getTransactionId(), event.getRequestId(), event.getObject(), StatusEvent.FAILED);
        }
      }
      return Uni.createFrom()
              .voidItem();
    } else {
      return Uni.createFrom()
              .voidItem();
    }
  }

}