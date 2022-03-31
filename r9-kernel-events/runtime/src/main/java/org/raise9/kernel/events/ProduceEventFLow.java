package org.raise9.kernel.events;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import java.util.Map;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.raise9.kernel.concurrent.SystemThreadExecute;
import org.raise9.kernel.utils.core.JsonMapper;
import org.raise9.kernel.utils.core.metadata.HeaderMetadata;
import org.raise9.kernel.utils.core.metadata.HttpDataCurrent;

@Slf4j
@Singleton
public class ProduceEventFLow {

  @ConfigProperty(name = "quarkus.application.name", defaultValue = "core")
  String appName;

  @Inject
  @Channel("send-data-out")
  //@Broadcast
  Emitter<String> emitterStart;

  @Inject
  JsonMapper jsonMapper;

  @Inject
  StateHistoryEventApi historyData;

  @Inject
  SystemThreadExecute execute;

  @Inject
  HttpDataCurrent httpDataCurrent;

  void onStart(@Observes StartupEvent event) {
    log.info("Started ProduceEventFLow {}", appName);
  }

  public Uni<Void> sendReactive(String type, EventConvert data) {
    return Uni.createFrom()
            .<String>emitter(s -> {
              HeaderMetadata header = httpDataCurrent.getHeaders();
              Map<String, String> mapHeaders = httpDataCurrent.getMapHeaders();
              log.debug("Produce new event type : {} , requestId: {}, transactionId: {} ", type, header.getRequestId(), header.getTransactionId());
              EventTransactionSync eventTransactionSync = new EventTransactionSync();
              eventTransactionSync.setTransactionId(header.getTransactionId());
              eventTransactionSync.setData(jsonMapper.objectToJsonNode(data));
              eventTransactionSync.setRequestId(header.getRequestId());
              eventTransactionSync.setType(type);
              eventTransactionSync.setMapHeaders(mapHeaders);
              eventTransactionSync.setStatus(StatusEvent.CREATE);
              String dataJson = jsonMapper.objectToString(eventTransactionSync);

              historyData.add(type, StatusEvent.CREATE, header.getRequestId(), dataJson);

              s.complete(dataJson);
            })
            .runSubscriptionOn(execute.getExecutorEvents())
            .invoke(dataJson -> {
              emitterStart.send(dataJson);
            })
            //.runSubscriptionOn(execute.emit())
            .invoke((dataJson) -> {
              log.info("Send new event : {} ", dataJson);
            })
            .flatMap(r -> Uni.createFrom()
                    .voidItem())
            .runSubscriptionOn(execute.getExecutorEvents())
            ;
  }

  public void send(String type, String uuid_core, String requestId, Object data, StatusEvent status) {
    EventTransactionSync eventTransactionSync = new EventTransactionSync();
    eventTransactionSync.setTransactionId(uuid_core);
    eventTransactionSync.setData(jsonMapper.objectToJsonNode(data));
    eventTransactionSync.setRequestId(requestId);
    eventTransactionSync.setType(type);
    eventTransactionSync.setStatus(status);
    String dataJson = jsonMapper.objectToString(eventTransactionSync);
    historyData.add(type, status, requestId, dataJson);
    emitterStart.send(dataJson);
    log.debug("Send data status : {} {} {}", type, requestId, status);
  }
}
