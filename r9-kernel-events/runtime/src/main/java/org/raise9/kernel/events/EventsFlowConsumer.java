package org.raise9.kernel.events;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.raise9.kernel.concurrent.SystemThreadExecute;
import org.raise9.kernel.utils.core.JsonMapper;

@Default
@Slf4j
@ApplicationScoped
public class EventsFlowConsumer {

  @Inject
  EventBus eventBus;

  @Inject
  JsonMapper jsonMapper;

  @Inject
  SystemThreadExecute execute;

  @Incoming(value = "send-data-start-in")
  public CompletionStage<Void> consume(Message<String> msg) {
    log.debug("Starting event-flow event-data type: {}", msg.getPayload());
    EventTransactionSync event = jsonMapper.convertObjectFromString(msg.getPayload(), EventTransactionSync.class);
    log.debug("Started event-flow event-data type: {}", event.getTransactionId());
    Uni<Void>
            es =
            eventBus.<EventTransactionSync>request(event.getType(), event)
                    .onItem()
                    .transformToUni((message, processEmitter) -> {
                      EventTransactionSync eventResponse = message.body();
                      processEmitter.complete(eventResponse);
                    })
                    .runSubscriptionOn(execute.getExecutorEvents())
                    .flatMap(o -> {
                      log.debug("End event-flow event-data type: {}", event.getTransactionId());
                      return Uni.createFrom()
                              .voidItem();
                    })
                    .runSubscriptionOn(execute.getExecutorEvents())
            ;
    return es.subscribeAsCompletionStage();
  }


}
