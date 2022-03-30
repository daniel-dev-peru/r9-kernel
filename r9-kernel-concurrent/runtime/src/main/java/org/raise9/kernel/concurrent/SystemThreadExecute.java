package org.raise9.kernel.concurrent;

import io.quarkus.runtime.StartupEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@ApplicationScoped
@Getter
public class SystemThreadExecute {

  @ConfigProperty(name = "r9.system.web.layer.min", defaultValue = "10")
  int coreWebLayer;
  @ConfigProperty(name = "r9.system.web.layer.max", defaultValue = "100")
  int maxWebLayer;
  @ConfigProperty(name = "r9.system.web.layer.time", defaultValue = "100")
  int timeWebLayer;

  @ConfigProperty(name = "r9.system.core.layer.min", defaultValue = "10")
  int coreCoreLayer;
  @ConfigProperty(name = "r9.system.core.layer.max", defaultValue = "100")
  int maxCoreLayer;
  @ConfigProperty(name = "r9.system.core.layer.time", defaultValue = "100")
  int timeCoreLayer;

  @ConfigProperty(name = "r9.system.db.layer.min", defaultValue = "10")
  int coreDbLayer;
  @ConfigProperty(name = "r9.system.db.layer.max", defaultValue = "100")
  int maxDbLayer;
  @ConfigProperty(name = "r9.system.db.layer.time", defaultValue = "100")
  int timeDbLayer;

  @ConfigProperty(name = "r9.system.events.layer.min", defaultValue = "10")
  int coreEventLayer;
  @ConfigProperty(name = "r9.system.events.layer.max", defaultValue = "100")
  int maxEventLayer;
  @ConfigProperty(name = "r9.system.events.layer.time", defaultValue = "100")
  int timeEventLayer;

  ExecutorService executorWeb = null;
  ExecutorService executorCore = null;
  ExecutorService executorDatabase = null;
  ExecutorService executorEvents = null;

  void onStart(@Observes StartupEvent event) {

    ThreadPoolExecutor poolWeb = new ThreadPoolExecutor(coreWebLayer, maxWebLayer, timeWebLayer, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    executorWeb = poolWeb;
    log.info("ExecutorService web started core: {} max: {} time: {}",coreWebLayer, maxWebLayer, timeWebLayer);

    ThreadPoolExecutor poolCore = new ThreadPoolExecutor(coreCoreLayer, maxCoreLayer, timeCoreLayer, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    executorCore = poolCore;
    log.info("ExecutorService core started core: {} max: {} time: {}",coreCoreLayer, maxCoreLayer, timeCoreLayer);

    ThreadPoolExecutor poolDatabase = new ThreadPoolExecutor(coreDbLayer, maxDbLayer, timeDbLayer, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    executorDatabase = poolDatabase;
    log.info("ExecutorService db started core: {} max: {} time: {}",coreDbLayer, maxDbLayer, timeDbLayer);

    ThreadPoolExecutor poolEvents = new ThreadPoolExecutor(coreEventLayer, maxEventLayer, timeEventLayer, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    executorEvents = poolEvents;
    log.info("ExecutorService events started core: {} max: {} time: {}",coreEventLayer, maxEventLayer, timeEventLayer);

  }

}
