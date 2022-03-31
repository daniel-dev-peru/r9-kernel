package org.raise9.kernel.events;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class LoadEventProducerBeans {

  void onStart(@Observes StartupEvent event) {
    log.info("Start LoadEventProducerBeans extension");
  }

  void onStop(@Observes ShutdownEvent event) {
    log.info("Stop LoadEventProducerBeans extension");
  }
/*
  @Produces
  //@Singleton
  @ApplicationScoped
  public ThreadExecute threadExecute() {
    log.info("ThreadExecute created");
    return new ThreadExecute();
  }

  @Produces
  //@Singleton
  @ApplicationScoped
  public EventDataConvert eventDataConvert() {
    log.info("EventDataConvert created");
    return new EventDataConvertImpl();
  }

  @Produces
  //@Singleton
  @ApplicationScoped
  public StateHistoryEventApi historyService() {
    log.info("StateHistoryEventApi created");
    return new StateHistoryEventApiImpl();
  }

  @Produces
  //@Singleton
  @ApplicationScoped
  public ProduceEventFLow produceEventFLow() {
    log.info("ProduceEventFLow created");
    return new ProduceEventFLow();
  }

  @Produces
  //@Singleton
  @ApplicationScoped
  public EventsFlowConsumer eventsFlowConsumer() {
    log.info("EventsFlowConsumer created");
    return new EventsFlowConsumer();
  }
*/
}
