package org.raise9.kernel.utils.core;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class UtilsBeans {

  void onStart(@Observes StartupEvent event) {
    log.info("Start util extension");
  }

  void onStop(@Observes ShutdownEvent event) {
    log.info("Stop util extension");
  }

  @Produces
  @Singleton
  public JsonMapper jsonMapper() {
    log.info("JsonMapper created");
    return new JsonMapper();
  }


}
